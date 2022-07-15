package eci.server.DashBoardModule.service;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.DashBoardModule.dto.ToDoDoubleList;
import eci.server.DashBoardModule.dto.ToDoSingle;
import eci.server.DashBoardModule.dto.myProject.TotalProject;
import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.auth.AccessExpiredException;
import eci.server.ItemModule.exception.route.RouteProductNotFoundException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.config.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final DesignRepository designRepository;
    private final BomRepository bomRepository;
    private final ProjectRepository projectRepository;
    private final NewItemRepository newItemRepository;
    private final AuthHelper authHelper;
    private final DevelopmentBomRepository developmentBomRepository;
    private final ChangeOrderRepository changeOrderRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final ProjectService projectService;

    public TotalProject readProjectTotal() {
        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        List<Project> myProjectList = projectRepository.findByMember(member1);

        int totalNumber;
        int working = 0;
        int complete = 0;
        int release = 0;
        int pending = 0;
        int drop = 0;


        for (Project project : myProjectList) {
            if (!project.getTempsave()) {
                if (
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("WORKING")
                ) {
                    working += 1;
                } else if (
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("COMPLETE")
                ) {
                    complete += 1;
                } else if (
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("RELEASE")
                ) {
                    release += 1;
                } else if (
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("PENDING")
                ) {
                    pending += 1;
                } else if (
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("DROP")
                ) {
                    drop += 1;
                }
            }

        }

        totalNumber = working + complete + release + pending + drop;


        return new TotalProject(
                totalNumber,
                (double) working / totalNumber,
                (double) complete / totalNumber,
                (double) release / totalNumber,
                (double) pending / totalNumber,
                (double) drop / totalNumber
        );

    }


    /**
     * PROJECT TODO
     *
     * @return
     */

    public ToDoDoubleList readProjectTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save 용 ) 내가 작성자인 모든 프로젝트들 데려오기
        List<Project> myProjectList = projectRepository.findByMember(member1);
        Iterator<Project> myProjectListItr = myProjectList.iterator();

        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<Project> tempSavedProjectList = new ArrayList<>();

        for (Project project : myProjectList) {
            if (project.getTempsave()
                    &&
                    Objects.equals(project.getId(), projectRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                            projectRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1
                    ).getId())
            ) {

                if(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size()>0
                ){

                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size() - 1);
                    int presentIdx = ordering.getPresent();
                    //아래가 에러 발생
                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx){
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) { //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                            //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                            tempSavedProjectList.add(project);
                            //임시저장 진행 중인 것
                        }
                    }
                }

                else {
                    tempSavedProjectList.add(project);
                }


            }
        }
// temp save 로직을 변경해서 아래 코드 필요 없어짐
//        //만약 이게 waiting approve 로 빠진다면 담아주는 리스트만 변경하면 된다.
//        for (Project project : myProjectList) {
//            if (!project.getTempsave()
//                // 임시저장 되지 않은 애들 중에 아직 approve 받지 않은 것들
//               ) {
//                if(!(routeProductRepository.findAllByProject(project).size()==0)){
//                    if(!(routeProductRepository.findAllByProject(project).get(
//                            routeProductRepository.findAllByProject(project).size()-1
//                    ).isPassed())){
//                        tempSavedProjectList.add(project);
//                    }
//                }
//            }
//        }

        if (tempSavedProjectList.size() > 0) {
            for (Project p : tempSavedProjectList) {
                TodoResponse
                        projectTodoResponse =
                        new TodoResponse(
                                p.getId(),
                                p.getName(),
                                p.getProjectType().getName(),
                                p.getProjectNumber(),
                                -1L
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        //new project -> 아이템 목록

        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 프로젝트와 Item(제품) Link(설계자) 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId())
                        &&
                        (
                                routeProduct.getType().getModule().equals("PROJECT")
                                        &&
                                        routeProduct.getType().getName().equals("CREATE")
                        )
                    //routeProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")
                ) {

                    System.out.println("project create route product id ::::" + routeProduct.getId());
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 프로젝트 링크 안된 애만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) {
            NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();
            if (routeProduct.getRouteOrdering().getProject()==null) {



                //0712
                // 0712 - revise target id 가 null 이라면 걍 새 아이템
                if(
                        targetItem.getReviseTargetId()!=null
                        &&
                        newItemRepository.
                                findById(targetItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new)
                                .isRevise_progress()
                ){

                    NewItem targetNewItem = newItemRepository.
                            findById(targetItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new);

                    Long reviseId=-1L;
                    System.out.println("old  "+ targetItem.getId());

                    if(projectRepository.findByNewItemOrderByIdAsc(targetNewItem).size()>0) {
                        System.out.println("have an old projects ");

                        Project oldProject = projectRepository.findByNewItemOrderByIdAsc(targetNewItem).get
                                (projectRepository.findByNewItemOrderByIdAsc(targetNewItem).size()-1);

                        reviseId = oldProject.getId();

                        if(reviseId!=-1L){

                            Project proj = projectRepository.findById(reviseId).orElseThrow(ProjectNotFoundException::new);
                            System.out.println("이 프로젝트가 임저 돼야 함ㅁㅁㅁㅁㅁㅁㅁ" + proj.getId());
                            //proj.setTempsave(true);proj.setReadonly(true); //0713 TODO : 반영안되는 중
                            projectService.projectUpdateToReadonlyFalseTempsaveTrue(proj);
                        }


                    }

                    unlinkedItemTodoResponses.add(
                            new TodoResponse(
                                    targetItem.getId(),
                                    targetItem.getName(),
                                    targetItem.getItemTypes().getItemType().toString(),
                                    targetItem.getItemNumber(),
                                    reviseId
                            )
                    );
                }

                else{ // 해당 아이템이 revise 로 인해 생긴 new item 이며 , targetItem 이 아직 revise _ progress 진행이라면
                    unlinkedItemTodoResponses.add(
                            new TodoResponse(
                                    targetItem.getId(),
                                    targetItem.getName(),
                                    targetItem.getItemTypes().getItemType().toString(),
                                    targetItem.getItemNumber(),
                                    -1L
                            )
                    );
                }
                //0712

            }
            else{
                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                targetItem.getId(),
                                targetItem.getName(),
                                targetItem.getItemTypes().getItemType().toString(),
                                targetItem.getItemNumber(),
                                -1L
                        )
                );
            }
        }

        List<TodoResponse> NEW_PROJECT = new ArrayList<>(unlinkedItemTodoResponses);

        ToDoSingle tempSave = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle newProject = new ToDoSingle("New Project", NEW_PROJECT);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newProject);

        return new ToDoDoubleList(toDoDoubleList);

    }

    /**
     * DESIGN TODO
     *fi
     * @return
     */
    public ToDoDoubleList readDesignTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save 용 ) 내가 작성자인 모든 디자인 데려오기
        List<Design> myDesignList = designRepository.findByMember(member1);
        Iterator<Design> myDesignListItr = myDesignList.iterator();

        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<Design> tempSavedDesignList = new ArrayList<>();

        for (Design design : myDesignList) {
            if (design.getTempsave()
                    &&
                    Objects.equals(design.getId(), designRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(
                            designRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size() - 1
                    ).getId())){
                //05-30 - 이 아이가 최신 아이일 때만! (최신 아니고 옛날 거면 필요 없음)


                if(routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size() - 1);
                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) { //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                            //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                            tempSavedDesignList.add(design);
                            //임시저장 진행 중인 것
                        }
                    }
                }

                else {
                    tempSavedDesignList.add(design);
                }
            }
        }


        if (tempSavedDesignList.size() > 0) {
            for (Design d : tempSavedDesignList) {
                TodoResponse
                        projectTodoResponse =
                        new TodoResponse(
                                d.getId(),
                                d.getNewItem().getName(),
                                d.getNewItem().getItemTypes().getItemType().toString(),
                                d.getNewItem().getItemNumber(),
                                -1L
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        //new project -> 아이템 목록
        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 기구 Design 생성[설계자] 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("기구Design생성[설계자]")
                        (
                                routeProduct.getType().getModule().equals("DESIGN")
                                        &&
                                        routeProduct.getType().getName().equals("CREATE")
                        )
                ) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 디자인 링크 안된 아이템만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) { //design create 단계에 처한 애들

            if (routeProduct.getRouteOrdering().getDesign()==null) {

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

                // 0712 - revise target id 가 null 이라면 걍 새 아이템
                if(targetItem.getReviseTargetId()!=null
                        &&
                        newItemRepository.
                                findById(targetItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new)
                                .isRevise_progress()
                ){

                    NewItem targetNewItem = newItemRepository.
                            findById(targetItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new);

                    Long reviseId=-1L;

                    if(designRepository.findByNewItemOrderByIdAsc(targetNewItem).size()>0) {
                        Design oldDesign = designRepository.findByNewItemOrderByIdAsc(targetNewItem).get
                                (designRepository.findByNewItemOrderByIdAsc(targetNewItem).size()-1);

                        reviseId = oldDesign.getId();
                    }

                    unlinkedItemTodoResponses.add(
                            new TodoResponse(
                                    targetItem.getId(),
                                    targetItem.getName(),
                                    targetItem.getItemTypes().getItemType().toString(),
                                    targetItem.getItemNumber(),
                                    reviseId
                            )
                    );
                }

                else{ // 해당 아이템이 revise 로 인해 생긴 new item 이며 , targetItem 이 아직 revise _ progress 진행이라면
                    unlinkedItemTodoResponses.add(
                            new TodoResponse(
                                    targetItem.getId(),
                                    targetItem.getName(),
                                    targetItem.getItemTypes().getItemType().toString(),
                                    targetItem.getItemNumber(),
                                    -1L
                            )
                    );
                }

            }
        }

        List<TodoResponse> NEW_DESIGN = new ArrayList<>(unlinkedItemTodoResponses);

        //3) REJECT - 라우트 프로덕트들 중에서 현재이고,

        // 디자인 설계이고,
        // 라우트프로덕트 멤버가 나이고,
        // PRE - REJECTED=TRUE 인 것
        HashSet<TodoResponse> rejectedDesignTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) { //myRoute-> 내꺼 + 현재
            //06-01 수정
            if (routeProduct.isPreRejected() &&
                    //routeProduct.getRoute_name().equals("기구Design생성[설계자]")
                    (
                            routeProduct.getType().getModule().equals("DESIGN")
                                    &&
                                    routeProduct.getType().getName().equals("CREATE")
                    )
            ) {

                Design targetDesign = routeProduct.getDesign();

                rejectedDesignTodoResponses.add(
                        new TodoResponse(
                                targetDesign.getId(),
                                targetDesign.getNewItem().getName(),
                                targetDesign.getNewItem().getItemTypes().getItemType().toString(),
                                targetDesign.getNewItem().getItemNumber(),
                                -1L
                        )
                );
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedDesignTodoResponses);

        //5) REVIEW
        //현재 라우트 프로덕트들 중  이름이 기구Design Review[설계팀장] 고,
        // 라우트프로덕트-멤버가 나로 지정

        List<RouteProduct> myDesignReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("기구Design Review")
                        routeProduct.getType().getModule().equals("DESIGN")
                        &&
                        routeProduct.getType().getName().equals("REVIEW")
                ) {
                    myDesignReviewRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        HashSet<TodoResponse> needReviewDesignTodoResponses = new HashSet<>();
/////이거 맞는지 모르겠다 검토 필요
        for (RouteProduct routeProduct : myDesignReviewRouteProductList) { //myRoute-> 내꺼 + 현재

            //현재 라우트보다 하나 이전 라우트프로덕트의 디자인만 존재함, 그 디자인을 가져와야한다.

            System.out.println(routeProduct.getId() - 1);
            RouteProduct targetRouteProduct = routeProductRepository.findById(routeProduct.getId() - 1)
                    .orElseThrow(RouteProductNotFoundException::new);

            Design targetDesign = targetRouteProduct.getDesign();

            needReviewDesignTodoResponses.add(
                    new TodoResponse(
                            targetDesign.getId(),
                            targetDesign.getNewItem().getName(),
                            targetDesign.getNewItem().getItemTypes().getItemType().toString(),
                            targetDesign.getNewItem().getItemNumber(),
                            -1L
                    )
            );
        }


        //4) REVISE - TODO : REVISE 추가되고 작업
        List<TodoResponse> REVISE = new ArrayList<>();


        List<TodoResponse> NEED_REVIEW = new ArrayList<>(needReviewDesignTodoResponses);

        ToDoSingle tempSave = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle newDesign = new ToDoSingle("New Design", NEW_DESIGN);
        ToDoSingle rejectedDesign = new ToDoSingle("Rejected Design", REJECTED);
        //ToDoSingle needRevise = new ToDoSingle("Need Revise", REVISE);
        ToDoSingle needReview = new ToDoSingle("Waiting Review", NEED_REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newDesign);
        toDoDoubleList.add(rejectedDesign);
        //toDoDoubleList.add(needRevise);
        toDoDoubleList.add(needReview);

        return new ToDoDoubleList(toDoDoubleList);

    }

    //06-04 아이템 대쉬보드
    public ToDoDoubleList readItemTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AccessExpiredException::new
        );

        //1-1 temp save 용 ) 내가 작성자인 모든 아이템 데려오기
        List<NewItem> NewItemList = newItemRepository.findByMember(member1);
        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<NewItem> tempSavedNewItemList = new ArrayList<>();



        for (NewItem newItem : NewItemList) {

            if (newItem.isTempsave() && !newItem.isRevise_progress()){
                //07-10 REVISE 된 것이 아닐 때에 임시저장에 뜨게하기
                // (REVISE 되는 거는 REVISE 에 떠야함)

                if(routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size() - 1);
                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                        //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                        tempSavedNewItemList.add(newItem);
                        //임시저장 진행 중인 것
                    }
                }

                else {
                    tempSavedNewItemList.add(newItem);
                }
            }
        }

        if (tempSavedNewItemList.size() > 0) {
            for (NewItem d : tempSavedNewItemList) {
                TodoResponse
                        projectTodoResponse =
                        new TodoResponse(
                                d.getId(),
                                d.getName(),
                                d.getItemTypes().getItemType().toString(),
                                d.getItemNumber(),
                                -1L
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 기구Design생성[설계자] 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("기구Design생성[설계자]")
                        (
                                routeProduct.getType().getModule().equals("DESIGN")
                                        &&
                                        routeProduct.getType().getName().equals("CREATE")
                        )
                ) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //2) REJECT - 라우트 프로덕트들 중에서 현재이고,,
        // 라우트프로덕트 멤버가 나이고,
        // preREJECTED=TRUE 인 것
        List<TodoResponse> rejectedNewItemTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> 내꺼 + 현재 진행 중
            //06-01 수정
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    if (routeProduct.isPreRejected() &&

                            (
                                    routeProduct.getType().getModule().equals("ITEM")
                                            &&
                                            routeProduct.getType().getName().equals("REQUEST")

                            )

                    ) {

                        NewItem target = routeProduct.getRouteOrdering().getNewItem();

                        rejectedNewItemTodoResponses.add(
                                new TodoResponse(
                                        target.getId(),
                                        target.getName(),
                                        target.getItemTypes().getItemType().toString(),
                                        target.getItemNumber(),
                                        -1L
                                )
                        );
                    }
                }
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedNewItemTodoResponses);

        //3) REVISE
        // 아이템들 중 revise_progress = true 인 것들 모두!
        // CO 로 찾은 라우트 오더링 중 COMPLETE 안됐고 && 작성자가 나인 것 member1
        List<TodoResponse> reviseNewItemTodoResponses = new ArrayList<>();

        //3-1 revise co 찾기 ) 내가 작성자인 모든 CO 데려오기
        List<ChangeOrder> changeOrders = changeOrderRepository.findByMember(member1);

        if(changeOrders.size()>0) {

            // 3-2 : 내가 작성한 CO 라우트 오더링 찾아오기
            List<RouteOrdering> routeOrdering = new ArrayList<>();

            for (ChangeOrder changeOrder : changeOrders) {
                List<RouteOrdering> orderings = routeOrderingRepository.findByChangeOrderByIdAsc(changeOrder);
                routeOrdering.addAll(orderings);
            }

            //3-3 : & 그 오더링의 co 아이템들을 한 리스트에 모아두자

            Set<NewItem> reviseCheckItems = new HashSet<>();
            for (RouteOrdering ro : routeOrdering){

                reviseCheckItems.addAll(
                        ro.getChangeOrder().getCoNewItems().stream().map(
                                CoNewItem::getNewItem
                        ).collect(Collectors.toList())
                );

            }
            // 3-4 : revise_progress 가 true 이면서 revisedCnt가 0초과인 routeOrdering이 없다면 라우트 만들어야해
            for(NewItem reviseTarget : reviseCheckItems){

                NewItem reviseNewItem = newItemRepository.findByReviseTargetId(reviseTarget.getId());

                if(reviseTarget.isRevise_progress() && reviseNewItem==null){
                    reviseNewItemTodoResponses.add(
                            new TodoResponse(
                                    reviseTarget.getId(),
                                    reviseTarget.getName(),
                                    reviseTarget.getItemTypes().getItemType().toString(),
                                    reviseTarget.getItemNumber(),
                                    -1L
                            )
                    );
                }
            }

            // 이 routeOrdering들의 change_order의 conewitem의 newitem
            // 리스트를 가져와서 그들 중 revise_progress=false인 것 찾기

        }

        List<TodoResponse> REVISE = new ArrayList<>(reviseNewItemTodoResponses);

        //4) REVIEW
        //현재 라우트 프로덕트들 중  이름이 Item Request Review(설계팀장) 고,
        // 라우트프로덕트-멤버가 나로 지정

        List<RouteProduct> myNewItemReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        (
                                routeProduct.getType().getModule().equals("ITEM")
                                        &&
                                        routeProduct.getType().getName().equals("REVIEW")
                        )
                    //routeProduct.getRoute_name().equals("Item Request Review(설계팀장)")

                ) {
                    myNewItemReviewRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        HashSet<TodoResponse> needReviewNewItemTodoResponses = new HashSet<>();
        for (RouteProduct routeProduct : myNewItemReviewRouteProductList) {
            //myRoute-> 내꺼 + 현재

            //현재 라우트보다 하나 이전 라우트프로덕트의 request니깐
            RouteProduct targetRouteProduct = routeProductRepository.findById(routeProduct.getId() - 1)
                    .orElseThrow(RouteProductNotFoundException::new);

            NewItem newItem = targetRouteProduct.getRouteOrdering().getNewItem();

            needReviewNewItemTodoResponses.add(
                    new TodoResponse(
                            newItem.getId(),
                            newItem.getName(),
                            newItem.getItemTypes().getItemType().toString(),
                            newItem.getItemNumber(),
                            -1L
                    )
            );
        }
        List<TodoResponse> NEED_REVIEW = new ArrayList<>(needReviewNewItemTodoResponses);

        ToDoSingle tempSave = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle rejectedItem = new ToDoSingle("Rejected Item", REJECTED);
        ToDoSingle needRevise = new ToDoSingle("Need Revise", REVISE);
        ToDoSingle needReview = new ToDoSingle("Waiting Review", NEED_REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(rejectedItem);
        toDoDoubleList.add(needRevise);
        toDoDoubleList.add(needReview);

        return new ToDoDoubleList(toDoDoubleList);

    }


    /**
     * BOM TO-DO
     *
     * @return
     */
    public ToDoDoubleList readBomTodo() {
        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );
        //new bom -> 아이템 목록
        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2-1 ) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 개발 BOM 생성[설계자] 인 것
        List<RouteProduct> myRouteBomCreateProductList = new ArrayList<>();

        // 2-2 ) // & 단계가 개발 bom review 인 것
        List<RouteProduct> myRouteBomReviewProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {

                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    // 내가 라우트 프로덕트에 지정되어 있고

                    if (
                        // 단계가 bom create 라면
                            routeProduct.getType().getModule().equals("BOM")
                                    &&
                                    routeProduct.getType().getName().equals("CREATE")
                    ) {
                        myRouteBomCreateProductList.add(routeProduct);
                        //break;

                    } else if (
                        // 단계가 bom review 라면
                            routeProduct.getType().getModule().equals("BOM")
                                    &&
                                    routeProduct.getType().getName().equals("REVIEW")
                    ) {
                        myRouteBomReviewProductList.add(routeProduct);
                        //break;
                    }

                }
            }
        }

        //3) new bom : 봄 링크 안된 아이템만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteBomCreateProductList) {//현재 봄 생성 단계 중에서
            if ( //0621 dev Bom 의 edit = false 라면

                    !developmentBomRepository
                            .findByBom(
                                    bomRepository.findByNewItemOrderByIdAsc(routeProduct.getRouteOrdering().getNewItem()).get(
                                            bomRepository.findByNewItemOrderByIdAsc(routeProduct.getRouteOrdering().getNewItem()).size() - 1
                                    )
                            ).getEdited()) {

                Bom bom = bomRepository.findByNewItemOrderByIdAsc(routeProduct.getRouteOrdering().getNewItem()).get(
                        bomRepository.findByNewItemOrderByIdAsc(routeProduct.getRouteOrdering().getNewItem()).size() - 1
                );

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();
                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                bom.getId(),
                                targetItem.getName(),
                                targetItem.getItemTypes().getItemType().toString(),
                                targetItem.getItemNumber(),
                                -1L
                        )
                );

            }
        }

        List<TodoResponse> NEW_BOM = new ArrayList<>(unlinkedItemTodoResponses);

        // 4) 임시저장인 것

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //1-1 temp save 용 ) 내가 작성자인 모든 디자인 데려오기
        List<Bom> myBomList = bomRepository.findByMember(member1);

        List<DevelopmentBom> developmentBoms = new ArrayList<>();

        for( Bom bom : myBomList){
            developmentBoms.add(developmentBomRepository.findByBom(bom));
        }
        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<DevelopmentBom> tempSavedDesignList = new ArrayList<>();

        for (DevelopmentBom bom : developmentBoms) {
            if (bom.getTempsave() && bom.getEdited()){ //06-28 edited 가 true 이며 임시저장이 true 인 것만 찾아오도록 수정

                if(routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).size() - 1);

                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) {
                            //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                            //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                            tempSavedDesignList.add(bom);
                            //임시저장 진행 중인 것
                        }
                    }
                }

                else {
                    tempSavedDesignList.add(bom);
                }
            }
        }

        if (tempSavedDesignList.size() > 0) {
            for (DevelopmentBom d : tempSavedDesignList) {
                TodoResponse
                        projectTodoResponse =
                        new TodoResponse(
                                d.getBom().getId(),
                                d.getBom().getNewItem().getName(),
                                d.getBom().getNewItem().getItemTypes().getItemType().toString(),
                                d.getBom().getNewItem().getItemNumber(),
                                -1L
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        // 5) 거부된 애들
        //  라우트 프로덕트들 중에서 현재이고,

        // 봄 CREATE 이고,
        // 라우트프로덕트 멤버가 나이고,
        // PRE - REJECTED=TRUE 인 것
        HashSet<TodoResponse> rejectedBomTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteBomCreateProductList) {
            if (
                    routeProduct.isPreRejected()
            ) {

                Bom targetBom = routeProduct.getRouteOrdering().getBom();

                if(targetBom!=null) {
                    rejectedBomTodoResponses.add(
                            new TodoResponse(
                                    targetBom.getId(),
                                    targetBom.getNewItem().getName(),
                                    targetBom.getNewItem().getItemTypes().getItemType().toString(),
                                    targetBom.getNewItem().getItemNumber(),
                                    -1L
                            )
                    );
                }
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedBomTodoResponses);

        // 6 ) BOM REVIEW 인 단계, 현재 진행 중이고, 내꺼고 단계가 봄 - 리뷰


        HashSet<TodoResponse> needReviewBomTodoResponses = new HashSet<>();

        for(RouteProduct routeProduct : myRouteBomReviewProductList){

            //현재 라우트보다 하나 이전 라우트프로덕트의 봄만 존재함, 그 봄을 가져와야한다.
            RouteProduct targetRouteProduct = routeProductRepository.findById(routeProduct.getId() - 1)
                    .orElseThrow(RouteProductNotFoundException::new);

            Bom targetBom = targetRouteProduct.getBom();

            needReviewBomTodoResponses.add(
                    new TodoResponse(
                            targetBom.getId(),
                            targetBom.getNewItem().getName(),
                            targetBom.getNewItem().getItemTypes().getItemType().toString(),
                            targetBom.getNewItem().getItemNumber(),
                            -1L
                    )
            );
        }

        List<TodoResponse> REVIEW = new ArrayList<>(needReviewBomTodoResponses);

        //7) REVISE - TODO : REVISE 추가되고 작업
        List<TodoResponse> REVISE = new ArrayList<>();


        ToDoSingle newBom = new ToDoSingle("Add New Bom", NEW_BOM);
        ToDoSingle tempBom = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle rejectedBom = new ToDoSingle("Rejected Bom", REJECTED);
        //ToDoSingle needRevise = new ToDoSingle("Need Revise", REVISE);
        ToDoSingle reviewBom = new ToDoSingle("Waiting Review", REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();

        toDoDoubleList.add(tempBom);
        toDoDoubleList.add(newBom);
        toDoDoubleList.add(rejectedBom);
        //toDoDoubleList.add(needRevise);
        toDoDoubleList.add(reviewBom);

        return new ToDoDoubleList(toDoDoubleList);

    }

    //////////////revise

    //0710 CR CO
    public ToDoDoubleList readCrCoTodo() {

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AccessExpiredException::new
        );
        // 1. CR 임시저장
        //1-1 cr temp save 용 ) 내가 작성자인 모든 CR 데려오기
        List<TodoResponse> CR_TEMP_SAVE = new ArrayList<>();

        List<ChangeRequest> myChangeRequests = changeRequestRepository.findByMember(member1);

        //1-2 temp-save 가 true 이며 temp-save 가 pre rejected  것만 담는 리스트
        List<ChangeRequest> tempSavedCrList = new ArrayList<>();

        for (ChangeRequest cr : myChangeRequests) {

            if (cr.isTempsave()){
                // (1) 한번 찐 저장했지만 거절 당한 CR REQUEST
                if(routeOrderingRepository.findByChangeRequest(cr).size()>0){

                    // cr의 라우트 오더링 중 가장 최신
                    RouteOrdering ordering = routeOrderingRepository.findByChangeRequest(cr).get(
                            routeOrderingRepository.findByChangeRequest(cr).size() - 1);

                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        tempSavedCrList.add(cr);

                    }
                }
                // (2) 한번도 저장 안됐던 임시저장 CR
                else {
                    tempSavedCrList.add(cr);
                }
            }
        }

        if (tempSavedCrList.size() > 0) {
            for (ChangeRequest d: tempSavedCrList) {
//                String type=null;
//                if(d.getNewItem()!=null){
//                    type = d.getNewItem().getItemTypes().getItemType().toString();
//                }else{
//                    type = "None";
//                }
                TodoResponse
                        crTempSaveTodoResponse =
                        new TodoResponse(
                                d.getId(),
                                d.getName(),
                                "CR",
                                d.getCrNumber(),
                                -1L
                        );
                CR_TEMP_SAVE.add(crTempSaveTodoResponse);
            }
        }

        // 2. CO 임시저장
        //1-1 cr temp save 용 ) 내가 작성자인 모든 CR 데려오기
        List<TodoResponse> CO_TEMP_SAVE = new ArrayList<>();

        List<ChangeOrder> myChangeOrders = changeOrderRepository.findByMember(member1);

        //1-2 temp-save 가 true 이며 temp-save 가 pre rejected  것만 담는 리스트
        List<ChangeOrder> tempSavedCoList = new ArrayList<>();

        for (ChangeOrder co : myChangeOrders) {

            if (co.getTempsave()){
                // (1) 한번 찐 저장했지만 거절 당한 Co REQUEST
                if(routeOrderingRepository.findByChangeOrderByIdAsc(co).size()>0){

                    // cr의 라우트 오더링 중 가장 최신
                    RouteOrdering ordering = routeOrderingRepository.findByChangeOrderByIdAsc(co).get(
                            routeOrderingRepository.findByChangeOrderByIdAsc(co).size() - 1);

                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        tempSavedCoList.add(co);

                    }
                }
                // (2) 한번도 저장 안됐던 임시저장 Co
                else {
                    tempSavedCoList.add(co);
                }
            }
        }

        if (tempSavedCoList.size() > 0) {

            for (ChangeOrder d: tempSavedCoList) {
                String type=null;

                TodoResponse
                        coTempSaveTodoResponse =
                        new TodoResponse(
                                d.getId(),
                                d.getName(),
                                "CO",
                                d.getCoNumber(),
                                -1L
                        );
                CO_TEMP_SAVE.add(coTempSaveTodoResponse);
            }
        }


        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

//        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 CO REVIEW 인 것
//        List<RouteProduct> myRouteProductList = new ArrayList<>();
//
//        for (RouteProduct routeProduct : routeProductList) {
//            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
//                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
//
//                        (
//                                routeProduct.getType().getModule().equals("CO")
//                                        &&
//                                        routeProduct.getType().getName().equals("REVIEW")
//                        )
//                ) {
//                    myRouteProductList.add(routeProduct);
//                    break;
//                }
//
//            }
//        }

        //3) CR_REJECT - 라우트 프로덕트들 중에서 현재이고,,
        // 라우트프로덕트 멤버가 나이고,
        // preREJECTED=TRUE 인 것
        List<TodoResponse> rejectedCRTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> 내꺼 + 현재 진행 중
            //06-01 수정
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    if (routeProduct.isPreRejected() &&

                            (
                                    routeProduct.getType().getModule().equals("CR")
                                            &&
                                            routeProduct.getType().getName().equals("REQUEST")

                            )

                    ) {

                        ChangeRequest target = routeProduct.getRouteOrdering().getChangeRequest();

                        rejectedCRTodoResponses.add(
                                new TodoResponse(
                                        target.getId(),
                                        target.getName(),
                                        "CR",
                                        target.getCrNumber(),
                                        -1L
                                )
                        );
                    }
                }
            }
        }
        List<TodoResponse> CR_REJECTED = new ArrayList<>(rejectedCRTodoResponses);


        //4) C0_REJECTED- 라우트 프로덕트들 중에서 현재이고,,
        // 라우트프로덕트 멤버가 나이고,
        // preREJECTED=TRUE 인 것
        List<TodoResponse> rejectedCOTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> 내꺼 + 현재 진행 중
            //06-01 수정
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    if (routeProduct.isPreRejected() &&

                            (
                                    routeProduct.getType().getModule().equals("CO")
                                            &&
                                            routeProduct.getType().getName().equals("REQUEST")

                            )

                    ) {

                        ChangeOrder target = routeProduct.getRouteOrdering().getChangeOrder();

                        rejectedCOTodoResponses.add(
                                new TodoResponse(
                                        target.getId(),
                                        target.getName(),
                                        "CO",
                                        target.getCoNumber(),
                                        -1L
                                )
                        );
                    }
                }
            }
        }
        List<TodoResponse> CO_REJECTED = new ArrayList<>(rejectedCOTodoResponses);

        //5) CR_REVIEW
        //현재 라우트 프로덕트들 중  이름이 Item Request Review(설계팀장) 고,
        // 라우트프로덕트-멤버가 나로 지정

        List<ChangeRequest> myCRReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        ( //(1) cr review
                                (
                                        routeProduct.getType().getModule().equals("CR")
                                                &&
                                                routeProduct.getType().getName().equals("REVIEW")
                                )
                                        ||
                                        (
                                                routeProduct.getType().getModule().equals("CR")
                                                        &&
                                                        routeProduct.getType().getName().equals("APPROVE")
                                        )

                        )

                ) {
                    myCRReviewRouteProductList.add(routeProduct.getRouteOrdering().getChangeRequest());
                    break;
                }

            }
        }

        HashSet<TodoResponse> needReviewCRTodoResponses = new HashSet<>();
        for (ChangeRequest cr : myCRReviewRouteProductList) {

            needReviewCRTodoResponses.add(
                    new TodoResponse(
                            cr.getId(),
                            cr.getName(),
                            "CR",
                            cr.getCrNumber(),
                            -1L
                    )
            );
        }
        List<TodoResponse> CR_NEED_REVIEW = new ArrayList<>(needReviewCRTodoResponses);

        //6) CO-REVIEW : APPROVE

        List<ChangeOrder> myCOReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        (
                                (
                                        routeProduct.getType().getModule().equals("CO")
                                                &&
                                                routeProduct.getType().getName().equals("APPROVE")
                                )

                                        ||
                                        (
                                                routeProduct.getType().getModule().equals("CO")
                                                        &&
                                                        routeProduct.getType().getName().equals("CONFIRM")
                                        )
                        )

                ) {
                    myCOReviewRouteProductList.add(routeProduct.getRouteOrdering().getChangeOrder());
                    break;
                }

            }
        }

        HashSet<TodoResponse> needReviewCOTodoResponses = new HashSet<>();
        for (ChangeOrder co : myCOReviewRouteProductList) {

            needReviewCOTodoResponses.add(
                    new TodoResponse(
                            co.getId(),
                            co.getName(),
                            "CO",
                            co.getCoNumber(),
                            -1L
                    )
            );
        }
        List<TodoResponse> CO_NEED_REVIEW = new ArrayList<>(needReviewCOTodoResponses);



        ToDoSingle CRTempSave = new ToDoSingle("Save as Draft CR", CR_TEMP_SAVE);
        ToDoSingle COTempSave = new ToDoSingle("Save as Draft CO", CO_TEMP_SAVE);
        ToDoSingle CRNeedReview = new ToDoSingle("Waiting Review CR", CR_NEED_REVIEW);
        ToDoSingle CONeedReview = new ToDoSingle("Waiting Review CO", CO_NEED_REVIEW);
        ToDoSingle rejectedCR = new ToDoSingle("Rejected CR", CR_REJECTED);
        ToDoSingle rejectedCO = new ToDoSingle("Rejected CO", CO_REJECTED);


        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(CRTempSave);
        toDoDoubleList.add(COTempSave);
        toDoDoubleList.add(rejectedCR);
        toDoDoubleList.add(rejectedCO);
        toDoDoubleList.add(CRNeedReview);
        toDoDoubleList.add(CONeedReview);

        return new ToDoDoubleList(toDoDoubleList);

    }


}