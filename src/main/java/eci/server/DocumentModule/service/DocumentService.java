package eci.server.DocumentModule.service;

import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.service.co.CoService;
import eci.server.DocumentModule.dto.DocumentCreateRequest;
import eci.server.DocumentModule.dto.DocumentReadDto;
import eci.server.DocumentModule.dto.DocumentTempCreateRequest;
import eci.server.DocumentModule.dto.DocumentUpdateRequest;
import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.DocumentModule.exception.DocumentNotFoundException;
import eci.server.DocumentModule.repository.*;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.ProjectModule.dto.project.ProjectTempCreateUpdateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
     * ???????????? release
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
                        docTagRepository,
                        documentAttachmentRepository
                )
        );

        if(!(req.getAttachments().size()==0)) {
            uploadAttachments(document.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(document);


        return new ProjectTempCreateUpdateResponse(document.getId());
    }

    /**
     * ???????????? release
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
                        docTagRepository,
                        documentAttachmentRepository
                )
        );

        if(!(req.getAttachments().size()==0)) {
            uploadAttachments(newDocument.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(newDocument);

        ///////// ????????? ???????????? ?????? ??????
        Document oldDocument
                = documentRepository.findById(oldDocId).orElseThrow(
                DocumentNotFoundException::new
        );

        newDocument.setReviseTargetDoc(oldDocument);

        oldDocument.reviseProgressTrue();
        ///////// ????????? ???????????? ?????? ???


        return new ProjectTempCreateUpdateResponse(newDocument.getId());
    }

    /**
     * release ??? ??????
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
                        docTagRepository,
                        documentAttachmentRepository
                )
        );

        if(!(req.getAttachments().size()==0)){
            uploadAttachments(document.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(document);


        return new NewItemCreateResponse(document.getId());
    }


    /**
     * release revise ?????? ??? ??? ??????
     *
     *
     *
     *      * ?????? ???????????? new doc???
     *      * targetDoc?????? old doc ??????
     *      * old doc??? revise - progress = true
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
                        docTagRepository,
                        documentAttachmentRepository
                )
        );

        if(!(req.getAttachments().size()==0)){


            uploadAttachments(
                    newDocument.getAttachments(),
                    req.getAttachments());
        }


        saveTrueAttachment(newDocument);

        ///////// ????????? ???????????? ?????? ??????
        Document oldDocument
                = documentRepository.findById(oldDocId).orElseThrow(
                DocumentNotFoundException::new
        );

        newDocument.setReviseTargetDoc(oldDocument);

        oldDocument.reviseProgressTrue();
        ///////// ????????? ???????????? ?????? ???

        return new NewItemCreateResponse(newDocument.getId());
    }


    /**
     * ?????? (???????????? ????????? ??????)
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
     * ???????????? ?????? ??? ??????????????? ??????
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
                    defaultImageAddress,
                    documentRepository
            );
        }
        else{
            return DocumentReadDto.noRouteToDto(
                    document,
                    defaultImageAddress
            );
        }
    }



    ///////////////////// ?????? ?????? ????????????

    private void deleteDocumentAttachments(List<DocumentAttachment> DocumentAttachments) {
        DocumentAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void saveTrueAttachment(Document target) {
        documentAttachmentRepository.findByDocument(target).
                forEach(
                        i->i.setSave(true)
                );
    }

    /**
    // ?????? ?????? ?????? ???
    // newDocument.getAttachments() ??? duplicate = true
     ??? ?????????
    // ???????????? ??????.
    **/
    private void uploadAttachments
    (List<DocumentAttachment> attachments,
     List<MultipartFile> filedAttachments) {

        List<DocumentAttachment> neededToBeUploaded =
                attachments.stream().filter(
                        documentAttachment -> //duplicate = false ??? ?????? ????????? ??? ??????!
                                !documentAttachment.isDuplicate()
                )
                .collect(Collectors.toList());

        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
        IntStream.range(0, neededToBeUploaded.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        neededToBeUploaded.get(i).getUniqueName()
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
