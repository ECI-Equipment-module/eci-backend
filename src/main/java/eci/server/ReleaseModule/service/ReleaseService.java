package eci.server.ReleaseModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.ItemModule.dto.item.ItemCreateResponse;
import eci.server.ItemModule.dto.item.ItemUpdateResponse;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
     * ???????????? release
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
        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(release.getAttachments(), req.getAttachments());
        }

        return new ProjectTempCreateUpdateResponse(release.getId());
    }

    /**
     * release ??? ??????
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

        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(release.getAttachments(), req.getAttachments());
        }

        saveTrueAttachment(release);


        return new ItemCreateResponse(release.getId());
    }

    /**
     * ?????? (???????????? ????????? ??????)
     * @param id
     */
    @Transactional

    public ItemUpdateResponse update(Long id, ReleaseUpdateRequest req) {

        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        List<Long> oldTags = produceOldNewTagComment(release, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(release, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(release, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(release, req).getNewComment();
        List<ReleaseAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(release, req).getTargetAttachmentsForTagAndComment();


        Releasing.FileUpdatedResult result = release.update(
                req,
                memberRepository,
                attachmentTagRepository,
                newItemRepository,
                changeOrderRepository,
                releaseOrganizationRepository,
                releaseTypeRepository,
                releaseOrganizationReleaseRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );


        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        Long routeId = -1L;
        if(routeOrderingRepository.findByNewItemOrderByIdAsc(release.getNewItem()).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository
                    .findByNewItemOrderByIdAsc(release.getNewItem()).get
                            (
                                    routeOrderingRepository.findByNewItemOrderByIdAsc(release.getNewItem()).size()-1
                            );
            routeId = routeOrdering.getId();
        }

        return new ItemUpdateResponse(id, routeId);

    }

    /**
     * ???????????? ?????? ??? ??????????????? ??????
     * @param id
     */


    @Transactional
    public ProjectTempCreateUpdateResponse tempEnd(
            Long id, ReleaseUpdateRequest req) {

        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        List<Long> oldTags = produceOldNewTagComment(release, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(release, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(release, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(release, req).getNewComment();
        List<ReleaseAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(release, req).getTargetAttachmentsForTagAndComment();



        Releasing.FileUpdatedResult result = release.tempEnd(
                req,
                ReleaseCreateRequest.ProjectNumber(release.getId()),
                memberRepository,
                attachmentTagRepository,
                newItemRepository,
                changeOrderRepository,
                releaseOrganizationRepository,
                releaseTypeRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        saveTrueAttachment(release);

        return new ProjectTempCreateUpdateResponse(id);

    }

    @Transactional
    public void delete(Long id) {
        Releasing release = releaseRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        deleteReleaseAttachments(release.getAttachments());
        releaseRepository.delete(release);
    }

    ////// ??????

    // read one project
    public ReleaseDto read(Long id){
        Releasing release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByReleaseOrderByIdAsc(release),
                        routeProductRepository,
                        routeOrderingRepository,
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


    ///////////////////// ?????? ?????? ????????????

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
        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
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


    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<ReleaseAttachment> targetAttachmentsForTagAndComment;
    }

    public OldNewTagCommentUpdatedResult produceOldNewTagComment(
            Releasing entity, //update ????????? ?????????
            ReleaseUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<ReleaseAttachment> attachments
                = entity.getAttachments();

        List<ReleaseAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (attachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<ReleaseAttachment> oldAttachments = entity.getAttachments();
            for (ReleaseAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                    // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            int standardIdx = targetAttachmentsForTagAndComment.size();

            oldDocTag.addAll(req.getAddedTag().subList(0, standardIdx));
            newDocTag.addAll(req.getAddedTag().subList(standardIdx, req.getAddedTag().size()));

            oldDocComment.addAll(req.getAddedAttachmentComment().subList(0, standardIdx));
            newDocComment.addAll(req.getAddedAttachmentComment().subList(standardIdx, req.getAddedTag().size()));

        }
        return new OldNewTagCommentUpdatedResult(

                oldDocTag,
                newDocTag,
                oldDocComment,
                newDocComment,

                targetAttachmentsForTagAndComment
        );
    }



}
