package eci.server.ReleaseModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.ItemModule.dto.item.ItemCreateResponse;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.*;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ReleaseModule.dto.ReleaseCreateRequest;
import eci.server.ReleaseModule.dto.ReleaseDto;
import eci.server.ReleaseModule.dto.ReleaseTempCreateRequest;
import eci.server.ReleaseModule.dto.ReleaseUpdateRequest;
import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import eci.server.ReleaseModule.exception.ReleaseNotFoundException;
import eci.server.ReleaseModule.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ReleaseTypeRepository releaseTypeRepository;
    private final ReleaseOrganizationRepository releaseOrganizationRepository;
    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final ChangeOrderRepository changeOrderRepository;
    private final NewItemRepository newItemRepository;
    private final MemberRepository memberRepository;
    private final ReleaseAttachmentRepository releaseAttachmentRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final ReleaseOrganizationReleaseRepository releaseOrganizationReleaseRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;


    /**
     * 임시저장 release
     * @param req
     * @return
     */
    @Transactional
    public ProjectTempCreateUpdateResponse tempCreate(ReleaseTempCreateRequest req) {

        Releasing release = releaseRepository.save(
                ReleaseTempCreateRequest.toEntity(
                        req,
                        memberRepository,
                        newItemRepository,
                        changeOrderRepository,
                        releaseTypeRepository,
                        releaseOrganizationRepository,
                        attachmentTagRepository
                )
        );
        if(!(req.getTag().size()==0)) {
            uploadAttachments(release.getAttachments(), req.getAttachments());
        }

        return new ProjectTempCreateUpdateResponse(release.getId());
    }

    /**
     * release 찐 저장
     * @param req
     * @return
     */
    @Transactional
    public ItemCreateResponse create(ReleaseCreateRequest req) {

        Releasing release = releaseRepository.save(
                ReleaseCreateRequest.toEntity(
                        req,
                        memberRepository,
                        newItemRepository,
                        changeOrderRepository,
                        releaseTypeRepository,
                        releaseOrganizationRepository,
                        attachmentTagRepository,
                        releaseOrganizationReleaseRepository
                )
        );

        if(!(req.getTag().size()==0)) {
            uploadAttachments(release.getAttachments(), req.getAttachments());
        }


        return new ItemCreateResponse(release.getId());
    }

    /**
     * 수정 (임시저장 이어서 수정)
     * @param id
     */
    @Transactional

    public ProjectTempCreateUpdateResponse update(Long id, ReleaseUpdateRequest req) {

        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);


        Releasing.FileUpdatedResult result = release.update(
                req,
                memberRepository,
                attachmentTagRepository,
                newItemRepository,
                changeOrderRepository,
                releaseOrganizationRepository,
                releaseTypeRepository,
                releaseOrganizationReleaseRepository
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
            Long id, ReleaseUpdateRequest req) {

        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        Releasing.FileUpdatedResult result = release.tempEnd(
                req,
                ReleaseCreateRequest.ProjectNumber(release.getId()),
                memberRepository,
                attachmentTagRepository,
                newItemRepository,
                changeOrderRepository,
                releaseOrganizationRepository,
                releaseTypeRepository
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

    @Transactional
    public void delete(Long id) {
        Releasing release = releaseRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        deleteReleaseAttachments(release.getAttachments());
        releaseRepository.delete(release);
    }

    ////// 읽기

    // read one project
    public ReleaseDto read(Long id){
        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByReleaseOrderByIdAsc(release),
                        routeProductRepository,
                        routeOrderingRepository,
                        bomRepository,
                        preliminaryBomRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);


        if (routeDtoList.size() > 0) {
            RouteOrdering routeOrdering =
                    routeOrderingRepository.findByReleaseOrderByIdAsc
                            (release).get(
                            routeOrderingRepository.findByReleaseOrderByIdAsc(release)
                                    .size() - 1
                    );


            return ReleaseDto.toDto(
                    release,
                    routeOrdering,
                    routeOrderingRepository,
                    routeProductRepository,
                    attachmentTagRepository,
                    bomRepository,
                    preliminaryBomRepository,
                    defaultImageAddress
            );
        }
        else{
            return ReleaseDto.noRoutetoDto(
                    release,
                    routeOrderingRepository,
                    routeProductRepository,
                    attachmentTagRepository,
                    bomRepository,
                    preliminaryBomRepository,
                    defaultImageAddress
            );
        }
    }


    ///////////////////// 문서 관련 메소드들

    private void deleteReleaseAttachments(List<ReleaseAttachment> releaseAttachments) {
        releaseAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void saveTrueAttachment(Releasing target) {
        releaseAttachmentRepository.findByRelease(target).
                forEach(
                        i->i.setSave(true)
                );
    }

    private void uploadAttachments(List<ReleaseAttachment> attachments, List<MultipartFile> filedAttachments) {
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

    private void deleteAttachments(List<ReleaseAttachment> attachments) {
        for(ReleaseAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }


}
