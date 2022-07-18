package eci.server.DocumentModule.service;

import eci.server.DocumentModule.dto.DocumentCreateRequest;
import eci.server.DocumentModule.dto.DocumentReadDto;
import eci.server.DocumentModule.dto.DocumentTempCreateRequest;
import eci.server.DocumentModule.dto.DocumentUpdateRequest;
import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.DocumentModule.exception.DocumentNotFoundException;
import eci.server.DocumentModule.repository.*;
import eci.server.ItemModule.dto.item.ItemCreateResponse;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.ProjectModule.dto.project.ProjectTempCreateUpdateResponse;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ReleaseModule.dto.ReleaseCreateRequest;
import eci.server.ReleaseModule.dto.ReleaseDto;
import eci.server.ReleaseModule.dto.ReleaseUpdateRequest;
import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.exception.ReleaseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentService {

    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final DocumentRepository documentRepository;
    private final DocTagRepository docTagRepository;
    private final DocClassification1Repository docClassification1Repository;
    private final DocClassification2Repository docClassification2Repository;
    private final MemberRepository memberRepository;
    private final DocumentAttachmentRepository documentAttachmentRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;


    /**
     * 임시저장 release
     * @param req
     * @return
     */
    @Transactional
    public ProjectTempCreateUpdateResponse tempCreate(DocumentTempCreateRequest req) {

        Document document = documentRepository.save(
                DocumentTempCreateRequest.toEntity(
                        req,
                        docClassification1Repository,
                        docClassification2Repository,
                        memberRepository,
                        docTagRepository
                )
        );

        if(!(req.getAttachments().size()==0)) {
            uploadAttachments(document.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(document);


        return new ProjectTempCreateUpdateResponse(document.getId());
    }

    /**
     * 임시저장 release
     * @param req
     * @return
     */
    @Transactional
    public ProjectTempCreateUpdateResponse tempReviseCreate(DocumentTempCreateRequest req,
                                                            Long oldDocId) {

        Document newDocument = documentRepository.save(
                DocumentTempCreateRequest.toEntity(
                        req,
                        docClassification1Repository,
                        docClassification2Repository,
                        memberRepository,
                        docTagRepository
                )
        );

        if(!(req.getAttachments().size()==0)) {
            uploadAttachments(newDocument.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(newDocument);

        ///////// 기존과 달라지는 부분 시작
        Document oldDocument
                = documentRepository.findById(oldDocId).orElseThrow(
                DocumentNotFoundException::new
        );

        newDocument.setReviseTargetDoc(oldDocument);

        oldDocument.reviseProgressTrue();
        ///////// 기존과 달라지는 부분 끝


        return new ProjectTempCreateUpdateResponse(newDocument.getId());
    }

    /**
     * release 찐 저장
     * @param req
     * @return
     */
    @Transactional
    public NewItemCreateResponse create(DocumentCreateRequest req) {

        Document document = documentRepository.save(
                DocumentCreateRequest.toEntity(
                        req,
                        docClassification1Repository,
                        docClassification2Repository,
                        memberRepository,
                        docTagRepository
                )
        );

        if(!(req.getAttachments().size()==0)){
            uploadAttachments(document.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(document);


        return new NewItemCreateResponse(document.getId());
    }


    /**
     * release revise 되는 거 찐 저장
     *
     *
     *
     *      * 새로 만들어진 new doc에
     *      * targetDoc으로 old doc 등록
     *      * old doc의 revise - progress = true
     *
     * @param req
     * @return
     */
    @Transactional
    public NewItemCreateResponse reviseCreate(DocumentCreateRequest req, Long oldDocId) {

        Document newDocument = documentRepository.save(
                DocumentCreateRequest.toEntity(
                        req,
                        docClassification1Repository,
                        docClassification2Repository,
                        memberRepository,
                        docTagRepository
                )
        );

        if(!(req.getAttachments().size()==0)){
            uploadAttachments(newDocument.getAttachments(), req.getAttachments());
        }


        saveTrueAttachment(newDocument);

        ///////// 기존과 달라지는 부분 시작
        Document oldDocument
                = documentRepository.findById(oldDocId).orElseThrow(
                DocumentNotFoundException::new
        );

        newDocument.setReviseTargetDoc(oldDocument);

        oldDocument.reviseProgressTrue();
        ///////// 기존과 달라지는 부분 끝

        return new NewItemCreateResponse(newDocument.getId());
    }


    /**
     * 수정 (임시저장 이어서 수정)
     * @param id
     */
    @Transactional

    public ProjectTempCreateUpdateResponse update(
            Long id, DocumentUpdateRequest req
    ) {

        Document document = documentRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);


        Document.FileUpdatedResult result = document.update(
                req,
                memberRepository,
                docTagRepository,
                docClassification1Repository,
                docClassification2Repository
        );


        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );
        return new ProjectTempCreateUpdateResponse(id);

    }



    /**
     * 임시저장 종료 후 찐저장으로 전환
     * @param id
     */


    @Transactional
    public ProjectTempCreateUpdateResponse tempEnd(
            Long id, DocumentUpdateRequest req) {

        Document document = documentRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);

        Document.FileUpdatedResult result = document.tempEnd(
                req,
                memberRepository,
                docTagRepository,
                docClassification1Repository,
                docClassification2Repository
        );

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        saveTrueAttachment(document);

        return new ProjectTempCreateUpdateResponse(id);

    }


    @Transactional
    public void delete(Long id) {
        Document document = documentRepository
                .findById(id).orElseThrow(DocumentNotFoundException::new);
        deleteDocumentAttachments(document.getAttachments());

        documentRepository.delete(document);
    }


    // read one project
    public DocumentReadDto read(Long id){
        Document document = documentRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository
                                .findByDocumentOrderByIdAsc(document),
                        routeProductRepository,
                        routeOrderingRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);


        if (routeDtoList.size() > 0) {
            RouteOrdering routeOrdering =
                    routeOrderingRepository.findByDocumentOrderByIdAsc
                            (document).get(
                            routeOrderingRepository
                                    .findByDocumentOrderByIdAsc(document)
                                    .size() - 1
                    );


            return DocumentReadDto.toDto(
                    document,
                    routeOrdering,
                    routeProductRepository,
                    defaultImageAddress
            );
        }
        else{
            return DocumentReadDto.noRouteToDto(
                    document,
                    defaultImageAddress
            );
        }
    }



    ///////////////////// 문서 관련 메소드들

    private void deleteDocumentAttachments(List<DocumentAttachment> DocumentAttachments) {
        DocumentAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void saveTrueAttachment(Document target) {
        documentAttachmentRepository.findByDocument(target).
                forEach(
                        i->i.setSave(true)
                );
    }

    private void uploadAttachments(List<DocumentAttachment> attachments, List<MultipartFile> filedAttachments) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 파일이 가지는 unique name 을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    private void deleteAttachments(List<DocumentAttachment> attachments) {
        for(DocumentAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }




}
