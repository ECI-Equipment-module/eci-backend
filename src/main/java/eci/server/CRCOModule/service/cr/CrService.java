package eci.server.CRCOModule.service.cr;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.cr.CrCreateRequest;
import eci.server.CRCOModule.dto.cr.CrReadDto;
import eci.server.CRCOModule.dto.cr.CrTempCreateRequest;
import eci.server.CRCOModule.dto.cr.CrUpdateRequest;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.exception.CrNotFoundException;
import eci.server.CRCOModule.exception.CrReasonNotFoundException;
import eci.server.CRCOModule.exception.CrUpdateImpossibleException;
import eci.server.CRCOModule.repository.cr.CrAttachmentRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.ProjectTempCreateUpdateResponse;
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
public class CrService {

    private final FileService fileService;
    private final CrReasonRepository crReasonRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final CrSourceRepository crSourceRepository;
    private final CrImportanceRepository crImportanceRepository;
    private final MemberRepository memberRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final NewItemRepository newItemRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final CrAttachmentRepository crAttachmentRepository;
    @Value("${default.image.address}")
    private String defaultImageAddress;

    @Transactional
    public NewItemCreateResponse tempCreate(CrTempCreateRequest req) {

        Long crReasonId = req.getCrReasonId();

        ChangeRequest changeRequest = changeRequestRepository.save(

                CrTempCreateRequest.toEntity(
                        req,
                        crReasonId,
                        crReasonRepository,
                        crSourceRepository,
                        crImportanceRepository,
                        memberRepository,
                        attachmentTagRepository,
                        newItemRepository
                )
        );

        if(req.getAttachments()!=null && req.getAttachments().size()>0) {
            uploadAttachments(changeRequest.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(changeRequest.getId());
    }

    @Transactional
    public NewItemCreateResponse create(CrCreateRequest req) {

        Long crReasonId = req.getCrReasonId();
        if (crReasonId==null){
            throw new CrReasonNotFoundException();
        }
        if(crReasonId==1L&&!(req.getCrReason().isBlank())) {
            if ((!req.getCrReason().isBlank()) || !(req.getCrReason() == null)) {
                CrReason addCrReason = crReasonRepository.save(
                        new CrReason(
                                req.getCrReason()
                        )
                );
                crReasonId = addCrReason.getId();
            }
        }

        ChangeRequest changeRequest = changeRequestRepository.save(

                CrCreateRequest.toEntity(
                        req,
                        crReasonId,
                        crReasonRepository,
                        crSourceRepository,
                        crImportanceRepository,
                        memberRepository,
                        attachmentTagRepository,
                        newItemRepository
                )
        );

        if(req.getTag().size()>0) {
            uploadAttachments(changeRequest.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(changeRequest.getId());
    }
    public CrReadDto read(Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByChangeRequest(changeRequest),
                        routeProductRepository,
                        routeOrderingRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);

        if (routeDtoList.size() > 0) {
            RouteOrdering routeOrdering = routeOrderingRepository.findByChangeRequest(changeRequest).get(0);
            return CrReadDto.toDto(
                    changeRequest,
                    routeOrdering,
                    routeOrderingRepository,
                    routeProductRepository,
                    bomRepository,
                    preliminaryBomRepository,
                    attachmentTagRepository,
                    defaultImageAddress
            );
        }
        return CrReadDto.noRoutetoDto(
                changeRequest,
                attachmentTagRepository,
                defaultImageAddress,
                routeOrderingRepository
        );
    }




    private void uploadAttachments(
            List<CrAttachment> attachments,
            List<MultipartFile> filedAttachments
    ) {
        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? uniquename??? ??????????????? ?????? ??????????????? ?????????
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    private void deleteAttachments(List<CrAttachment> attachments) {
        for(CrAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }

    @Transactional
    public void delete(Long id) {

        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        changeRequestRepository.delete(changeRequest);
    }


    @Transactional
    public ProjectTempCreateUpdateResponse update(Long id, CrUpdateRequest req) {

        ChangeRequest cr=
                changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        if (!cr.isTempsave()) { //true??? ???????????? ??????, false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            throw new CrUpdateImpossibleException();
        }

        List<Long> oldTags = produceOldNewTagComment(cr, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(cr, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(cr, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(cr, req).getNewComment();
        List<CrAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(cr, req).getTargetAttachmentsForTagAndComment();


        ChangeRequest.FileUpdatedResult result = cr.update(
                req,
                newItemRepository,
                crReasonRepository,
                crImportanceRepository,
                crSourceRepository,
                memberRepository,
                attachmentTagRepository,

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
        return new ProjectTempCreateUpdateResponse(id);

    }


    @Transactional
    public DesignCreateUpdateResponse tempEnd(
            Long id, CrUpdateRequest req) {

        ChangeRequest cr=
                changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        List<Long> oldTags = produceOldNewTagComment(cr, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(cr, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(cr, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(cr, req).getNewComment();
        List<CrAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(cr, req).getTargetAttachmentsForTagAndComment();


        if (!cr.isTempsave() || cr.isReadonly()) {
            //tempsave??? false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            //readonly??? true?????? ?????? ????????????
            throw new CrUpdateImpossibleException();
        }

        Long crReasonId = req.getCrReasonId();
        if (crReasonId==null){
            throw new CrReasonNotFoundException();
        }
        if(crReasonId==1L&&!(req.getCrReason().isBlank())) {
            if ((!req.getCrReason().isBlank()) || !(req.getCrReason() == null)) {
                CrReason addCrReason = crReasonRepository.save(
                        new CrReason(
                                req.getCrReason()
                        )
                );
                crReasonId = addCrReason.getId();
            }
        }

        ChangeRequest.FileUpdatedResult result = cr.tempEnd(
                req,
                crReasonId,
                newItemRepository,
                crReasonRepository,
                crImportanceRepository,
                crSourceRepository,
                memberRepository,
                attachmentTagRepository,

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
        if(routeOrderingRepository.findByChangeRequest(cr).size()>0) {
            List<RouteOrdering> routeOrdering = routeOrderingRepository.findByChangeRequest(cr);
            routeId = routeOrderingRepository.findByChangeRequest(cr).get(routeOrdering.size() - 1).getId();

            RouteOrdering setRoute =
                    routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

            setRoute.setChangeRequest(cr);
        }
        saveTrueAttachment(cr);
        /////////////////////////////////////////////////////////////////////////

        return new DesignCreateUpdateResponse(id, routeId);

    }

    private void saveTrueAttachment(ChangeRequest target) {
        crAttachmentRepository.findByChangeRequest(target).
                forEach(
                        i->i.setSave(true)
                );

    }

    /**
     *  CR ??????
     */
    public List<ChangeRequest> crCandidates(){

        List<ChangeRequest> allCrList = changeRequestRepository.findAll();
        List<ChangeRequest> crCandidates = new ArrayList<>();

        for(ChangeRequest cr : allCrList){
            if(
                    routeOrderingRepository.findByChangeRequest(cr).size()>0
                            && (routeOrderingRepository.findByChangeRequest(cr).get(
                            routeOrderingRepository.findByChangeRequest(cr).size()-1
                    ).getLifecycleStatus().equals("COMPLETE") ||
                            (routeOrderingRepository.findByChangeRequest(cr).get(
                                    routeOrderingRepository.findByChangeRequest(cr).size()-1
                            ).getLifecycleStatus().equals("RELEASE")
                            )
                    )
                     //(1) ????????? ????????? complete, release


            ){
                if(cr.getDone()==null||!cr.getDone()){ //(2) ?????? cr ????????? ????????????){
                crCandidates.add(cr);
            }
            }
        }

        return crCandidates;

    }
    ///////////////////////////////////
    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<CrAttachment> targetAttachmentsForTagAndComment;
    }

    public OldNewTagCommentUpdatedResult produceOldNewTagComment(
            ChangeRequest entity, //update ????????? ?????????
            CrUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<CrAttachment> attachments
                = entity.getAttachments();

        List<CrAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (attachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<CrAttachment> oldAttachments = entity.getAttachments();
            for (CrAttachment attachment : oldAttachments) {

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
