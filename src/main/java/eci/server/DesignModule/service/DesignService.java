package eci.server.DesignModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.service.co.CoService;
import eci.server.DesignModule.dto.*;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.exception.DesignUpdateImpossibleException;
import eci.server.DesignModule.repository.DesignAttachmentRepository;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.*;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DesignService {

    private final MemberRepository memberRepository;
    private final NewItemRepository itemRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final ProjectRepository projectRepository;
    private final NewItemRepository newItemRepository;
    private final DesignAttachmentRepository designAttachmentRepository;
    private final AttachmentTagRepository attachmentTagRepository;

    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final DesignRepository designRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

//    public ProjectListDto readDashboardAll(ProjectReadCondition cond) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(cond)
//        );
//    }

//    public DesignListDto readDashboardAll(ProjectReadCondition cond) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(cond)
//        );
//    }


    @Transactional
    public DesignTempCreateUpdateResponse tempCreate(DesignTempCreateRequest req) {

        Design design = designRepository.save(
                DesignTempCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository,
                        attachmentTagRepository
                )
        );
        if (!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(design.getDesignAttachments(), req.getAttachments());
        }

        return new DesignTempCreateUpdateResponse(design.getId());
    }


    @Transactional
    public DesignCreateUpdateResponse create(DesignCreateRequest req) {

        Design design = designRepository.save(
                DesignCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository,
                        attachmentTagRepository
                )
        );
        if (!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(design.getDesignAttachments(), req.getAttachments());
        }
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem());
        //??????????????? ?????? ?????????
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(routeOrdering.size() - 1).getId();

        //06-17 ???????????? , routeOrdering ??? design ?????? ?????? ?????? ????????????//////
        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setDesign(design);
        saveTrueAttachment(design);
        /////////////////////////////////////////////////////////////////

        return new DesignCreateUpdateResponse(design.getId(), routeId);
    }

    private void uploadAttachments(List<DesignAttachment> attachments, List<MultipartFile> filedAttachments) {
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

    private void deleteAttachments(List<DesignAttachment> attachments) {
        for(DesignAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }



    @Transactional
    public DesignTempCreateUpdateResponse update(Long id, DesignUpdateRequest req) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        if (!design.getTempsave() ||design.getReadonly() ) {
            //tempsave??? false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????,
            // ???????????? ????????? ??????
            //readonly??? true?????? ?????? ????????????
            throw new DesignUpdateImpossibleException();
        }

        if (!design.getTempsave()) {
            //true??? ???????????? ??????, false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            throw new DesignUpdateImpossibleException();
        }

        List<Long> oldTags = produceOldNewTagComment(design, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(design, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(design, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(design, req).getNewComment();
        List<DesignAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(design, req).getTargetAttachmentsForTagAndComment();


        Design.FileUpdatedResult result = design.update(
                req,
                itemRepository,
                memberRepository,
                attachmentTagRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );


        uploadAttachments(
                result.getDesignUpdatedResult().getAddedAttachments(),
                result.getDesignUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getDesignUpdatedResult().getDeletedAttachments()
        );
        return new DesignTempCreateUpdateResponse(id);

    }

    @Transactional
    public DesignCreateUpdateResponse tempEnd(Long id, DesignUpdateRequest req) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);

        if (!design.getTempsave() || design.getReadonly()) {
            //tempsave??? false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????,
            // ???????????? ????????? ??????
            //readonly??? true?????? ?????? ????????????
            throw new DesignUpdateImpossibleException();
        }
        List<Long> oldTags = produceOldNewTagComment(design, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(design, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(design, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(design, req).getNewComment();
        List<DesignAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(design, req).getTargetAttachmentsForTagAndComment();

        Design.FileUpdatedResult result = design.tempEnd(
                req,
                itemRepository,
                memberRepository,
                attachmentTagRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );


        uploadAttachments(
                result.getDesignUpdatedResult().getAddedAttachments(),
                result.getDesignUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getDesignUpdatedResult().getDeletedAttachments()
        );

        //06-17 ?????? ???????????? ????????? ?????? ?????? ??????

        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem());
        //???????????? ?????? ?????????////////////////////////////
        Long routeId =
                routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(routeOrdering.size() - 1).getId();

        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setDesign(design);
        saveTrueAttachment(design);
        ////////////////////////////////////////////////
        return new DesignCreateUpdateResponse(id, routeId);

    }


    // read one project
    public DesignDto read(Long id) {
        Design targetDesign = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(targetDesign.getNewItem())
                .get(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(targetDesign.getNewItem()).size()-1
                );
        return DesignDto.toDto(
                routeOrdering,
                targetDesign,
                routeOrderingRepository,
                routeProductRepository,
                bomRepository,
                preliminaryBomRepository,
                attachmentTagRepository,
                defaultImageAddress
        );
    }

    //????????? ??????????????? ???????????? ??????
    public Page<DesignSimpleDto> readPageAll
    (
            Pageable pageRequest,
            ProjectMemberRequest req
    ){
        Page<Design> designListBefore = designRepository.findAll(pageRequest);//??????

        List<Design> designListList =
                designListBefore.stream().filter(
                        i->i.getTempsave().equals(false)
                ).collect(Collectors.toList());

        Page<Design> designList = new PageImpl<>(designListList);

        Page<DesignSimpleDto> pagingList = designList.map(
                design -> new DesignSimpleDto(

                        design.getId(),

                        design.getNewItem().getName(),
                        design.getNewItem().getItemNumber(),

                        design.getTempsave(),

                        //tag??? ??????
                        design.getDesignAttachments().stream().filter(
                                        a -> a.getTag().equals("DEVELOP")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        DesignAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),

                        //tag??? ?????????
                        design.getDesignAttachments().stream().filter(
                                        a -> a.getTag().equals("DESIGN")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        DesignAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),


                        design.getCreatedAt(),

                        //project.getLifecycle(), //??????????????? ??????????????????

                        routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size()-1
                        ).getLifecycleStatus(),
                        //???????????? ????????? ?????????

                        req.getMemberId().equals(design.getMember().getId())
                        //?????? ????????? ??? ?????? ??????????????? ????????? readonly==true

                )
        );
        return pagingList;
    }
    //delete one project

    @Transactional
    public void delete(Long id) {
        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        deleteDesignAttachments(design.getDesignAttachments());
        designRepository.delete(design);
    }

    private void deleteDesignAttachments(List<DesignAttachment> designAttachments) {
        designAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void saveTrueAttachment(Design target) {
        designAttachmentRepository.findByDesign(target).
                forEach(
                        i->i.setSave(true)
                );

    }



    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<DesignAttachment> targetAttachmentsForTagAndComment;
    }

    public OldNewTagCommentUpdatedResult produceOldNewTagComment(
            Design entity, //update ????????? ?????????
            DesignUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<DesignAttachment> attachments
                = entity.getDesignAttachments();

        List<DesignAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (attachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<DesignAttachment> oldAttachments =
                    entity.getDesignAttachments();
            for (DesignAttachment attachment : oldAttachments) {

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
