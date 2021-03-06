package eci.server.CRCOModule.service.co;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.co.CoCreateRequest;
import eci.server.CRCOModule.dto.co.CoReadDto;
import eci.server.CRCOModule.dto.co.CoTempCreateRequest;
import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.dto.cr.CrUpdateRequest;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.exception.CoNotFoundException;
import eci.server.CRCOModule.exception.CoUpdateImpossibleException;
import eci.server.CRCOModule.exception.CrNotFoundException;
import eci.server.CRCOModule.exception.CrUpdateImpossibleException;
import eci.server.CRCOModule.repository.co.CoAttachmentRepository;
import eci.server.CRCOModule.repository.co.CoCoEffectRepository;
import eci.server.CRCOModule.repository.cr.CrAttachmentRepository;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.CRCOModule.repository.cofeature.ChangedFeatureRepository;
import eci.server.CRCOModule.repository.cofeature.CoEffectRepository;
import eci.server.CRCOModule.repository.cofeature.CoStageRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.CRCOModule.service.cr.CrService;
import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.ProjectTempCreateUpdateResponse;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
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
public class CoService{


    private final FileService fileService;
    private final CrReasonRepository crReasonRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final CrImportanceRepository crImportanceRepository;
    private final MemberRepository memberRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final NewItemRepository newItemRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final ChangeOrderRepository changeOrderRepository;
    private final ClientOrganizationRepository clientOrganizationRepository;
    private final CarTypeRepository carTypeRepository;
    private final CoEffectRepository coEffectRepository;
    private final CoStageRepository coStageRepository;
    private final ChangedFeatureRepository changedFeatureRepository;
    private final CoAttachmentRepository coAttachmentRepository;
    private final CrReasonRepository coReasonRepository;
    private final CoCoEffectRepository coCoEffectRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @Transactional
    public NewItemCreateResponse tempCreate(CoTempCreateRequest req) {

        Long crReasonId = req.getCoReasonId();


        ChangeOrder changeOrder = changeOrderRepository.save(

                CoTempCreateRequest.toEntity(
                        req,
                        crReasonId,

                        clientOrganizationRepository,
                        carTypeRepository,
                        crReasonRepository,
                        coEffectRepository,
                        coStageRepository,
                        memberRepository,
                        crImportanceRepository,
                        attachmentTagRepository,
                        newItemRepository,
                        changeRequestRepository,
                        changedFeatureRepository
                )
        );

        if(req.getAttachments()!=null && req.getAttachments().size()>0){
            uploadAttachments(changeOrder.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(changeOrder.getId());
    }

        @Transactional
        public NewItemCreateResponse create(CoCreateRequest req) {

            Long coReasonId = req.getCoReasonId();

            if(coReasonId==1L) {
                if ((!req.getCoReason().isBlank()) || !(req.getCoReason() == null)) {
                    CrReason addCrReason = crReasonRepository.save(
                            new CrReason(
                                    req.getCoReason()
                            )
                    );
                    coReasonId = addCrReason.getId();
                }
            }

            ChangeOrder changeOrder = changeOrderRepository.save(

                    CoCreateRequest.toEntity(
                            req,
                            coReasonId,

                            clientOrganizationRepository,
                            carTypeRepository,

                            crReasonRepository,
                            coEffectRepository,
                            coStageRepository,
                            memberRepository,
                            crImportanceRepository,
                            attachmentTagRepository,

                            newItemRepository,
                            changeRequestRepository,
                            changedFeatureRepository

                    )
            );

            if(req.getAttachments()!=null && req.getAttachments().size()>0) {
                uploadAttachments(changeOrder.getAttachments(), req.getAttachments());
            }

            return new NewItemCreateResponse(changeOrder.getId());
        }


    private void uploadAttachments(
            List<CoAttachment> attachments,
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

    private void deleteAttachments(List<CoAttachment> attachments) {
        for(CoAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }



    @Transactional
    public void delete(Long id) {

        ChangeOrder changeOrder = changeOrderRepository
                .findById(id).orElseThrow(CoNotFoundException::new);

        changeOrderRepository.delete(changeOrder);
    }


    @Transactional
    public ProjectTempCreateUpdateResponse update(Long id, CoUpdateRequest req) {


        Long coReasonId = req.getCoReasonId();
        if(coReasonId==null){
            coReasonId=1L;
        }

        ChangeOrder co=
                changeOrderRepository.findById(id).orElseThrow(CoNotFoundException::new);

        if (!co.getTempsave()) { //true??? ???????????? ??????, false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            throw new CrUpdateImpossibleException();
        }
        List<Long> oldTags = produceOldNewTagComment(co, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(co, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(co, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(co, req).getNewComment();
        List<CoAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(co, req).getTargetAttachmentsForTagAndComment();


        ChangeOrder.FileUpdatedResult result = co.update(
                req,
                coReasonId,

                clientOrganizationRepository,
                carTypeRepository,
                coReasonRepository,
                coEffectRepository,
                coStageRepository,
                memberRepository,
                crImportanceRepository,
                attachmentTagRepository,
                newItemRepository,
                changeRequestRepository,
                changedFeatureRepository,

                coCoEffectRepository,

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
            Long id, CoUpdateRequest req) {


        Long coReasonId = req.getCoReasonId();

        if(coReasonId==1L) {
            if ((!req.getCoReason().isBlank()) || !(req.getCoReason() == null)) {
                CrReason addCrReason = crReasonRepository.save(
                        new CrReason(
                                req.getCoReason()
                        )
                );
                coReasonId = addCrReason.getId();
            }
        }

        ChangeOrder co=
                changeOrderRepository.findById(id).orElseThrow(CrNotFoundException::new);

        if (!co.getTempsave() || co.getReadonly()) {
            //tempsave??? false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            //readonly??? true?????? ?????? ????????????
            throw new CoUpdateImpossibleException();
        }
        List<Long> oldTags = produceOldNewTagComment(co, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(co, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(co, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(co, req).getNewComment();
        List<CoAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(co, req).getTargetAttachmentsForTagAndComment();

        ChangeOrder.FileUpdatedResult result = co.tempEnd(
                req,
                coReasonId,

                clientOrganizationRepository,
                carTypeRepository,
                coReasonRepository,
                coEffectRepository,
                coStageRepository,
                memberRepository,
                crImportanceRepository,
                attachmentTagRepository,

                newItemRepository,
                changeRequestRepository,
                changedFeatureRepository,

                coCoEffectRepository,

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
        if(routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()>0) {
            List<RouteOrdering> routeOrdering = routeOrderingRepository.findByChangeOrderOrderByIdAsc(co);
            routeId = routeOrderingRepository.findByChangeOrderOrderByIdAsc(co)
                    .get(routeOrdering.size() - 1).getId();

            RouteOrdering setRoute =
                    routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

            setRoute.setChangeOrder(co);
        }
        saveTrueAttachment(co);
        /////////////////////////////////////////////////////////////////////////

        return new DesignCreateUpdateResponse(id, routeId);

    }

    private void saveTrueAttachment(ChangeOrder target) {
        coAttachmentRepository.findByChangeOrderOrderByIdAsc(target).
                forEach(
                        i->i.setSave(true)
                );

    }


    public CoReadDto read(Long id){
        ChangeOrder co = changeOrderRepository.findById(id).orElseThrow(CrNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByChangeOrderOrderByIdAsc(co),
                        routeProductRepository,
                        routeOrderingRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);

        if (routeDtoList.size() > 0) {
            RouteOrdering routeOrdering =
                    routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).get(
                            routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()-1
                    );
            return CoReadDto.toDto(
                    co,
                    routeOrdering,
                    routeOrderingRepository,
                    routeProductRepository,
                    bomRepository,
                    preliminaryBomRepository,
                    attachmentTagRepository,
                    defaultImageAddress
            );
        }
        return CoReadDto.noRouteDto(
                co,
                //routeOr,
                routeOrderingRepository,
                routeProductRepository,
                attachmentTagRepository,
                defaultImageAddress
        );
    }

    /**
     * CO ??? ????????? COMPLETE ??? ???
     * @return
     */
    public List<ChangeOrder> readCoAvailableInRelease() {

        List<ChangeOrder> changeOrders = changeOrderRepository.findAllByOrderByIdAsc();

        //1-2) ????????? release ??? complete??? ?????? ?????? ????????? ?????? ??????
        List<ChangeOrder> finalChangeOrders = new ArrayList<>();

        for(ChangeOrder  co : changeOrders){
            if(
                    routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()>0
                            && (routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).get(
                            routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()-1
                    ).getLifecycleStatus().equals("COMPLETE")
//                            ||
//                            (routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).get(
//                                    routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()-1
//                            ).getLifecycleStatus().equals("RELEASE")
//                            )
                    )
            ){
                finalChangeOrders.add(co);
            }
        }
            return finalChangeOrders;
    }

    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<CoAttachment> targetAttachmentsForTagAndComment;
    }

    public OldNewTagCommentUpdatedResult produceOldNewTagComment(
            ChangeOrder entity, //update ????????? ?????????
            CoUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<CoAttachment> attachments
                = entity.getAttachments();

        List<CoAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (attachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<CoAttachment> oldAttachments = entity.getAttachments();
            for (CoAttachment attachment : oldAttachments) {

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
