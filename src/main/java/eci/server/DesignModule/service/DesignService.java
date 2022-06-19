package eci.server.DesignModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
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
import eci.server.ProjectModule.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final DesignRepository designRepository;

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
        if (!(req.getTag().size() == 0)) {
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
        if (!(req.getTag().size() == 0)) {
            uploadAttachments(design.getDesignAttachments(), req.getAttachments());
        }
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItem(design.getNewItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByNewItem(design.getNewItem()).get(routeOrdering.size() - 1).getId();

        //06-17 등록되면 , routeOrdering 에 design 으로 얘를 등록 시켜주기//////
        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setDesign(design);
        saveTrueAttachment(design);
        /////////////////////////////////////////////////////////////////

        return new DesignCreateUpdateResponse(design.getId(), routeId);
    }

    private void uploadAttachments(List<DesignAttachment> attachments, List<MultipartFile> filedAttachments) {
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
            //tempsave가 false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가,
            // 임시저장 일때만 가능
            //readonly가 true라면 수정 불가상태
            throw new DesignUpdateImpossibleException();
        }

        if (!design.getTempsave()) {
            //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
            throw new DesignUpdateImpossibleException();
        }

        Design.FileUpdatedResult result = design.update(
                req,
                itemRepository,
                memberRepository
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
            //tempsave가 false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가,
            // 임시저장 일때만 가능
            //readonly가 true라면 수정 불가상태
            throw new DesignUpdateImpossibleException();
        }

        Design.FileUpdatedResult result = design.tempEnd(
                req,
                itemRepository,
                memberRepository
        );


        uploadAttachments(
                result.getDesignUpdatedResult().getAddedAttachments(),
                result.getDesignUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getDesignUpdatedResult().getDeletedAttachments()
        );

        //06-17 아래 라우트에 디자인 등록 로직 추가

        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItem(design.getNewItem());
        //디자인에 딸린 라우트////////////////////////////
        Long routeId = routeOrderingRepository.findByNewItem(design.getNewItem()).get(routeOrdering.size() - 1).getId();


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
        return DesignDto.toDto(
                targetDesign,
                routeOrderingRepository,
                routeProductRepository,
                bomRepository,
                preliminaryBomRepository,
                attachmentTagRepository
        );
    }

    //로젝트 리스트에서 찾아노는 경우
    public Page<DesignSimpleDto> readPageAll
    (
            Pageable pageRequest,
            ProjectMemberRequest req
    ){
        Page<Design> designListBefore = designRepository.findAll(pageRequest);//에러

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

                        //tag가 개발
                        design.getDesignAttachments().stream().filter(
                                        a -> a.getTag().equals("DEVELOP")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        DesignAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),

                        //tag가 디자인
                        design.getDesignAttachments().stream().filter(
                                        a -> a.getTag().equals("DESIGN")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        DesignAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),


                        design.getCreatedAt(),

                        //project.getLifecycle(), //프로젝트의 라이프사이클

                        routeOrderingRepository.findByNewItem(design.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(design.getNewItem()).size()-1
                        ).getLifecycleStatus(),
                        //아이템의 라이프 사이클

                        req.getMemberId().equals(design.getMember().getId())
                        //현재 로그인 된 플젝 작성멤버랑 같으면 readonly==true

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




}
