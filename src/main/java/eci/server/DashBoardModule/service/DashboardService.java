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
import eci.server.CRCOModule.service.co.CoService;
import eci.server.DashBoardModule.dto.ToDoDoubleList;
import eci.server.DashBoardModule.dto.ToDoSingle;
import eci.server.DashBoardModule.dto.myProject.TotalProject;
import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.repository.DocumentRepository;
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
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.repository.ReleaseRepository;
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
    private final ReleaseRepository releasingRepository;
    private final NewItemService newItemService;
    private final CoService changeOrderService;
    private final DocumentRepository documentRepository;

    public TotalProject readProjectTotal() {
        //0) ?????? ????????? ??? ??????
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

        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save ??? ) ?????? ???????????? ?????? ??????????????? ????????????
        List<Project> myProjectList = projectRepository.findByMember(member1);
        Iterator<Project> myProjectListItr = myProjectList.iterator();

        //1-2 temp-save ??? true ??? ?????? ?????? ?????????
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
                    //????????? ?????? ??????
                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx){
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) { //06-18 ???????????? ???????????? ???????????????, ????????? ???????????? ??????????????? ?????? ??????
                            //06-04 : ???????????? ?????? ?????? ????????? ???????????? ??????????????? ?????????
                            tempSavedProjectList.add(project);
                            //???????????? ?????? ?????? ???
                        }
                    }
                }

                else {
                    tempSavedProjectList.add(project);
                }


            }
        }
// temp save ????????? ???????????? ?????? ?????? ?????? ?????????
//        //?????? ?????? waiting approve ??? ???????????? ???????????? ???????????? ???????????? ??????.
//        for (Project project : myProjectList) {
//            if (!project.getTempsave()
//                // ???????????? ?????? ?????? ?????? ?????? ?????? approve ?????? ?????? ??????
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

        //new project -> ????????? ??????

        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????????????? Item(??????) Link(?????????) ??? ???
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
                    //routeProduct.getRoute_name().equals("??????????????? Item(??????) Link(?????????)")
                ) {

                    System.out.println("project create route product id ::::" + routeProduct.getId());
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) ???????????? ?????? ?????? ?????? ??????
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) {
            NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();
            if (routeProduct.getRouteOrdering().getProject()==null) {



                //0712
                // 0712 - revise target id ??? null ????????? ??? ??? ?????????
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
                            System.out.println("??? ??????????????? ?????? ?????? ????????????????????????" + proj.getId());
                            //proj.setTempsave(true);proj.setReadonly(true); //0713 TODO : ??????????????? ???
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

                else{ // ?????? ???????????? revise ??? ?????? ?????? new item ?????? , targetItem ??? ?????? revise _ progress ???????????????
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

        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save ??? ) ?????? ???????????? ?????? ????????? ????????????
        List<Design> myDesignList = designRepository.findByMember(member1);
        Iterator<Design> myDesignListItr = myDesignList.iterator();

        //1-2 temp-save ??? true ??? ?????? ?????? ?????????
        List<Design> tempSavedDesignList = new ArrayList<>();

        for (Design design : myDesignList) {
            if (design.getTempsave()
                    &&
                    Objects.equals(design.getId(), designRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(
                            designRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size() - 1
                    ).getId())){
                //05-30 - ??? ????????? ?????? ????????? ??????! (?????? ????????? ?????? ?????? ?????? ??????)


                if(routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size() - 1);
                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) { //06-18 ???????????? ???????????? ???????????????, ????????? ???????????? ??????????????? ?????? ??????
                            //06-04 : ???????????? ?????? ?????? ????????? ???????????? ??????????????? ?????????
                            tempSavedDesignList.add(design);
                            //???????????? ?????? ?????? ???
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

        //new project -> ????????? ??????
        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ?????? Design ??????[?????????] ??? ???
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("??????Design??????[?????????]")
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

        //3) ????????? ?????? ?????? ???????????? ??????
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) { //design create ????????? ?????? ??????

            if (routeProduct.getRouteOrdering().getDesign()==null) {

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

                // 0712 - revise target id ??? null ????????? ??? ??? ?????????
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

                else{ // ?????? ???????????? revise ??? ?????? ?????? new item ?????? , targetItem ??? ?????? revise _ progress ???????????????
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

        //3) REJECT - ????????? ??????????????? ????????? ????????????,

        // ????????? ????????????,
        // ????????????????????? ????????? ?????????,
        // PRE - REJECTED=TRUE ??? ???
        HashSet<TodoResponse> rejectedDesignTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) { //myRoute-> ?????? + ??????
            //06-01 ??????
            if (routeProduct.isPreRejected() &&
                    //routeProduct.getRoute_name().equals("??????Design??????[?????????]")
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
        //?????? ????????? ??????????????? ???  ????????? ??????Design Review[????????????] ???,
        // ?????????????????????-????????? ?????? ??????

        List<RouteProduct> myDesignReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("??????Design Review")
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
/////?????? ????????? ???????????? ?????? ??????
        for (RouteProduct routeProduct : myDesignReviewRouteProductList) { //myRoute-> ?????? + ??????

            //?????? ??????????????? ?????? ?????? ???????????????????????? ???????????? ?????????, ??? ???????????? ??????????????????.

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


        //4) REVISE - TODO : REVISE ???????????? ??????
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

    //06-04 ????????? ????????????
    public ToDoDoubleList readItemTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AccessExpiredException::new
        );

        //1-1 temp save ??? ) ?????? ???????????? ?????? ????????? ????????????
        List<NewItem> NewItemList = newItemRepository.findByMember(member1);
        //1-2 temp-save ??? true ??? ?????? ?????? ?????????
        List<NewItem> tempSavedNewItemList = new ArrayList<>();



        for (NewItem newItem : NewItemList) {

            if (newItem.isTempsave() && !newItem.isRevise_progress() &&!newItem.isRevise_progress()){
                //07-10 REVISE ??? ?????? ?????? ?????? ??????????????? ????????????
                // (REVISE ?????? ?????? REVISE ??? ?????????)

                if(routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size() - 1);
                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        //06-18 ???????????? ???????????? ???????????????, ????????? ???????????? ??????????????? ?????? ??????
                        //06-04 : ???????????? ?????? ?????? ????????? ???????????? ??????????????? ?????????
                        tempSavedNewItemList.add(newItem);
                        //???????????? ?????? ?????? ???
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
                                d.getItemTypes()==null?" ":d.getItemTypes().getItemType().toString(),
                                d.getItemNumber(),
                                -1L
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????Design??????[?????????] ??? ???
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("??????Design??????[?????????]")
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

        //2) REJECT - ????????? ??????????????? ????????? ????????????,,
        // ????????????????????? ????????? ?????????,
        // preREJECTED=TRUE ??? ???
        List<TodoResponse> rejectedNewItemTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> ?????? + ?????? ?????? ???
            //06-01 ??????
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
        // ???????????? ??? revise_progress = true ??? ?????? ??????!
        // CO ??? ?????? ????????? ????????? ??? COMPLETE ????????? && ???????????? ?????? ??? member1
        List<TodoResponse> reviseNewItemTodoResponses = new ArrayList<>();

        //3-1 revise co ?????? ) ?????? ???????????? ?????? CO ????????????
        List<ChangeOrder> changeOrders = changeOrderRepository.findByMember(member1);

        if(changeOrders.size()>0) {

            // 3-2 : ?????? ????????? CO ????????? ????????? ????????????
            List<RouteOrdering> routeOrdering = new ArrayList<>();

            for (ChangeOrder changeOrder : changeOrders) {
                List<RouteOrdering> orderings = routeOrderingRepository.findByChangeOrderOrderByIdAsc(changeOrder);
                routeOrdering.addAll(orderings);
            }

            //3-3 : & ??? ???????????? co ??????????????? ??? ???????????? ????????????

            Set<NewItem> reviseCheckItems = new HashSet<>();
            for (RouteOrdering ro : routeOrdering){

                reviseCheckItems.addAll(
                        ro.getChangeOrder().getCoNewItems().stream().map(
                                CoNewItem::getNewItem
                        ).collect(Collectors.toList())
                );

            }
            // 3-4 : revise_progress ??? true ????????? revisedCnt??? 0????????? routeOrdering??? ????????? ????????? ???????????????
            for(NewItem reviseTarget : reviseCheckItems){

                NewItem reviseNewItem = newItemRepository.findByReviseTargetId(reviseTarget.getId());

                if(reviseTarget.isRevise_progress() && reviseNewItem==null){
                    reviseNewItemTodoResponses.add(
                            new TodoResponse(
                                    reviseTarget.getId(),
                                    reviseTarget.getName(),
                                    reviseTarget.getItemTypes().getItemType().toString(),
                                    reviseTarget.getItemNumber(),
                                    1L
                            )
                    );
                }
            }

            // ??? routeOrdering?????? change_order??? conewitem??? newitem
            // ???????????? ???????????? ?????? ??? revise_progress=false??? ??? ??????

        }

        List<TodoResponse> REVISE = new ArrayList<>(reviseNewItemTodoResponses);

        //4) REVIEW
        //?????? ????????? ??????????????? ???  ????????? Item Request Review(????????????) ???,
        // ?????????????????????-????????? ?????? ??????

        List<RouteProduct> myNewItemReviewRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        (
                                routeProduct.getType().getModule().equals("ITEM")
                                        &&
                                        routeProduct.getType().getName().equals("REVIEW")
                        )
                    //routeProduct.getRoute_name().equals("Item Request Review(????????????)")

                ) {
                    myNewItemReviewRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        HashSet<TodoResponse> needReviewNewItemTodoResponses = new HashSet<>();
        for (RouteProduct routeProduct : myNewItemReviewRouteProductList) {
            //myRoute-> ?????? + ??????

            //?????? ??????????????? ?????? ?????? ???????????????????????? request??????
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
        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );
        //new bom -> ????????? ??????
        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2-1 ) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ?????? BOM ??????[?????????] ??? ???
        List<RouteProduct> myRouteBomCreateProductList = new ArrayList<>();

        // 2-2 ) // & ????????? ?????? bom review ??? ???
        List<RouteProduct> myRouteBomReviewProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {

                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    // ?????? ????????? ??????????????? ???????????? ??????

                    if (
                        // ????????? bom create ??????
                            routeProduct.getType().getModule().equals("BOM")
                                    &&
                                    routeProduct.getType().getName().equals("CREATE")
                    ) {
                        myRouteBomCreateProductList.add(routeProduct);
                        //break;

                    } else if (
                        // ????????? bom review ??????
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

        //3) new bom : ??? ?????? ?????? ???????????? ??????
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteBomCreateProductList) {//?????? ??? ?????? ?????? ?????????
            if ( //0621 dev Bom ??? edit = false ??????

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

        // 4) ??????????????? ???

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //1-1 temp save ??? ) ?????? ???????????? ?????? ????????? ????????????
        List<Bom> myBomList = bomRepository.findByMember(member1);

        List<DevelopmentBom> developmentBoms = new ArrayList<>();

        for( Bom bom : myBomList){
            developmentBoms.add(developmentBomRepository.findByBom(bom));
        }
        //1-2 temp-save ??? true ??? ?????? ?????? ?????????
        List<DevelopmentBom> tempSavedDesignList = new ArrayList<>();

        for (DevelopmentBom bom : developmentBoms) {
            if (bom.getTempsave() && bom.getEdited()){ //06-28 edited ??? true ?????? ??????????????? true ??? ?????? ??????????????? ??????

                if(routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(bom.getBom().getNewItem()).size() - 1);

                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) {
                            //06-18 ???????????? ???????????? ???????????????, ????????? ???????????? ??????????????? ?????? ??????
                            //06-04 : ???????????? ?????? ?????? ????????? ???????????? ??????????????? ?????????
                            tempSavedDesignList.add(bom);
                            //???????????? ?????? ?????? ???
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

        // 5) ????????? ??????
        //  ????????? ??????????????? ????????? ????????????,

        // ??? CREATE ??????,
        // ????????????????????? ????????? ?????????,
        // PRE - REJECTED=TRUE ??? ???
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

        // 6 ) BOM REVIEW ??? ??????, ?????? ?????? ?????????, ????????? ????????? ??? - ??????


        HashSet<TodoResponse> needReviewBomTodoResponses = new HashSet<>();

        for(RouteProduct routeProduct : myRouteBomReviewProductList){

            //?????? ??????????????? ?????? ?????? ???????????????????????? ?????? ?????????, ??? ?????? ??????????????????.
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

        //7) REVISE - TODO : REVISE ???????????? ??????
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

        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AccessExpiredException::new
        );
        // 1. CR ????????????
        //1-1 cr temp save ??? ) ?????? ???????????? ?????? CR ????????????
        List<TodoResponse> CR_TEMP_SAVE = new ArrayList<>();

        List<ChangeRequest> myChangeRequests = changeRequestRepository.findByMember(member1);

        //1-2 temp-save ??? true ?????? temp-save ??? pre rejected  ?????? ?????? ?????????
        List<ChangeRequest> tempSavedCrList = new ArrayList<>();

        for (ChangeRequest cr : myChangeRequests) {

            if (cr.isTempsave()){
                // (1) ?????? ??? ??????????????? ?????? ?????? CR REQUEST
                if(routeOrderingRepository.findByChangeRequest(cr).size()>0){

                    // cr??? ????????? ????????? ??? ?????? ??????
                    RouteOrdering ordering = routeOrderingRepository.findByChangeRequest(cr).get(
                            routeOrderingRepository.findByChangeRequest(cr).size() - 1);

                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        tempSavedCrList.add(cr);

                    }
                }
                // (2) ????????? ?????? ????????? ???????????? CR
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

        // 2. CO ????????????
        //1-1 cr temp save ??? ) ?????? ???????????? ?????? CR ????????????
        List<TodoResponse> CO_TEMP_SAVE = new ArrayList<>();

        List<ChangeOrder> myChangeOrders = changeOrderRepository.findByMember(member1);

        //1-2 temp-save ??? true ?????? temp-save ??? pre rejected  ?????? ?????? ?????????
        List<ChangeOrder> tempSavedCoList = new ArrayList<>();

        for (ChangeOrder co : myChangeOrders) {

            if (co.getTempsave()){
                // (1) ?????? ??? ??????????????? ?????? ?????? Co REQUEST
                if(routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size()>0){

                    // cr??? ????????? ????????? ??? ?????? ??????
                    RouteOrdering ordering = routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).get(
                            routeOrderingRepository.findByChangeOrderOrderByIdAsc(co).size() - 1);

                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected() ) {

                        tempSavedCoList.add(co);

                    }
                }
                // (2) ????????? ?????? ????????? ???????????? Co
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


        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

//        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? CO REVIEW ??? ???
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

        //3) CR_REJECT - ????????? ??????????????? ????????? ????????????,,
        // ????????????????????? ????????? ?????????,
        // preREJECTED=TRUE ??? ???
        List<TodoResponse> rejectedCRTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> ?????? + ?????? ?????? ???
            //06-01 ??????
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


        //4) C0_REJECTED- ????????? ??????????????? ????????? ????????????,,
        // ????????????????????? ????????? ?????????,
        // preREJECTED=TRUE ??? ???
        List<TodoResponse> rejectedCOTodoResponses = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) { //myRoute-> ?????? + ?????? ?????? ???
            //06-01 ??????
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
        //?????? ????????? ??????????????? ???  ????????? Item Request Review(????????????) ???,
        // ?????????????????????-????????? ?????? ??????

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


    /**
     * RELEASE TODO
     *
     * @return
     */
    public ToDoDoubleList readRELEASETodo() {
        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );
        //new bom -> ????????? ??????
        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2-1 ) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ?????? BOM ??????[?????????] ??? ???
        List<RouteProduct> myRouteReleaseCreateProductList = new ArrayList<>();

        // 2-2 ) // & ????????? ?????? bom review ??? ???
        List<RouteProduct> myRouteReleaseReviewProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {

                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    // ?????? ????????? ??????????????? ???????????? ??????

                    if (
                        // ????????? bom create ??????
                            routeProduct.getType().getModule().equals("RELEASE")
                                    &&
                                    routeProduct.getType().getName().equals("CREATE")
                    ) {
                        myRouteReleaseCreateProductList.add(routeProduct);
                        //break;

                    } else if (
                        // ????????? bom review ??????
                            routeProduct.getType().getModule().equals("RELEASE")
                                    &&
                                    routeProduct.getType().getName().equals("REVIEW")
                    ) {
                        myRouteReleaseReviewProductList.add(routeProduct);
                        //break;
                    }

                }
            }
        }

        //1 ::: TEMP SAVE RELEASE: RELEASE ??? TEMP SAVE = TRUE ;

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //1-1 temp save ??? ) ?????? ???????????? ?????? ????????? ????????????
        List<Releasing> myReleaseList = releasingRepository.findByMember(member1);

        //1-2 temp-save ??? true ??? ?????? ?????? ?????????

        Set<Releasing> tempSavedReleaseList = new HashSet();
        for (Releasing releasing : myReleaseList) {

            if (releasing.getTempsave()){
                if(routeOrderingRepository.findByReleaseOrderByIdAsc(releasing).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByReleaseOrderByIdAsc(releasing)
                            .get(
                                    routeOrderingRepository.findByReleaseOrderByIdAsc(releasing).size() - 1
                            );

                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) {
                            tempSavedReleaseList.add(releasing);
                        }
                    }
                }

                else {
                    tempSavedReleaseList.add(releasing);
                }
            }
        }

        if (tempSavedReleaseList.size() > 0) {
            for (Releasing r: tempSavedReleaseList) {
                TodoResponse
                        releaseTodoResponse =
                        new TodoResponse(
                                r.getId(),
                                r.getReleaseTitle(),
                                r.getReleaseType().getName(),
                                r.getReleaseNumber(),
                                -1L
                        );
                TEMP_SAVE.add(releaseTodoResponse);
            }
        }

        // 2 ::: ????????? RELEASE ???
        //  ????????? ??????????????? ????????? ????????????,
        // RELEASE CREATE ??????
        // ????????????????????? ????????? ?????????,
        // PRE - REJECTED=TRUE ??? ???
        HashSet<TodoResponse> rejectedReleaseTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteReleaseCreateProductList) {
            if (
                    routeProduct.isPreRejected()
            ) {

                Releasing targetReleasing = routeProduct.getRouteOrdering().getRelease();

                if(targetReleasing!=null) {
                    rejectedReleaseTodoResponses.add(
                            new TodoResponse(
                                    targetReleasing.getId(),
                                    targetReleasing.getReleaseTitle(),
                                    targetReleasing.getReleaseType().getName(),
                                    targetReleasing.getReleaseNumber(),
                                    -1L
                            )
                    );
                }
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedReleaseTodoResponses);

        //3 :: RELEASE REVIEW ??? ??????, ?????? ?????? ?????????, ????????? ????????? RELEASE REVIEW
        HashSet<TodoResponse> needReviewRELEASETodoResponses = new HashSet<>();

        for(RouteProduct routeProduct : myRouteReleaseReviewProductList){

            //?????? ??????????????? ?????? ?????? ???????????????????????? ?????? ?????????, ??? ?????? ??????????????????.
            Releasing releasing = routeProduct.getRouteOrdering().getRelease();

            needReviewRELEASETodoResponses.add(
                    new TodoResponse(
                            releasing.getId(),
                            releasing.getReleaseTitle(),
                            releasing.getReleaseType().getName(),
                            releasing.getReleaseNumber(),
                            -1L
                    )
            );
        }

        List<TodoResponse> REVIEW = new ArrayList<>(needReviewRELEASETodoResponses);


        // 4 ::: NEW ITEM - ITEM ??? ??? ????????? complete & released ??? 0 ??? ???
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        Set<NewItem> newItemForRelease =
                newItemService.readAffectedItems().stream().filter(
                        newItem -> (newItem.getReleased()!=null&&newItem.getReleased()==0)
                ).collect(Collectors.toSet());

        Set<NewItem> finalNewItemForRelease = new HashSet<>();

        for (NewItem chkItem : newItemForRelease) {
            // ??? ???????????? release ??? ???????????? ????????????  (releasing of new item ??? ????????? 0)
            List<Releasing> releasingOfNewItem = releasingRepository.findByNewItemOrderByIdAsc(
                    chkItem
            );

//            // ??? ???????????? ?????? CO ??? RELEASE
//            List<ChangeOrder> changeOrders
//
            if(releasingOfNewItem.size()==0) {
                finalNewItemForRelease.add(chkItem);
            }

        }

        for (NewItem newItem : finalNewItemForRelease) {
            unlinkedItemTodoResponses.add(
                    new TodoResponse(
                            newItem.getId(),
                            newItem.getName(),
                            newItem.getItemTypes().getItemType().toString(),
                            newItem.getItemNumber(),
                            -1L
                    )
            );

        }
        List<TodoResponse> NEW_ITEM = new ArrayList<>(unlinkedItemTodoResponses);

        // 5 ::: NEW CO
        HashSet<TodoResponse> unlinkedCoTodoResponses = new HashSet<>();

        Set<ChangeOrder> changeOrders =
                new HashSet<>(changeOrderService.readCoAvailableInRelease());

        for (ChangeOrder changeOrder : changeOrders) {
            unlinkedCoTodoResponses.add(
                    new TodoResponse(
                            changeOrder.getId(),
                            changeOrder.getName(),
                            "CO",
                            changeOrder.getCoNumber(),
                            -1L
                    )
            );

        }
        List<TodoResponse> NEW_CO = new ArrayList<>(unlinkedCoTodoResponses);

        // TOTAL

        ToDoSingle newItem = new ToDoSingle("New First Release", NEW_ITEM);
        ToDoSingle newCo = new ToDoSingle("New CO Release", NEW_CO);
        ToDoSingle tempRelease = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle rejectedRelease = new ToDoSingle("Rejected Release", REJECTED);
        ToDoSingle reviewRelease = new ToDoSingle("Waiting Review", REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();

        toDoDoubleList.add(newItem);
        toDoDoubleList.add(newCo);
        toDoDoubleList.add(tempRelease);
        toDoDoubleList.add(rejectedRelease);
        toDoDoubleList.add(reviewRelease);

        return new ToDoDoubleList(toDoDoubleList);

    }



    /**
     * DOCUMENT TODO
     *
     * @return
     */
    public ToDoDoubleList readDOCUMENTTodo() {
        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );
        //new bom -> ????????? ??????
        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2-1 ) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ?????? Doc ??????[?????????] ??? ???
        List<RouteProduct> myRouteDocCreateProductList = new ArrayList<>();

        // 2-2 ) // & ????????? ?????? Doc review ??? ???
        List<RouteProduct> myRouteDocReviewProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {

                if (routeProductMember.getMember().getId().equals(member1.getId())) {
                    // ?????? ????????? ??????????????? ???????????? ??????

                    if (
                        // ????????? bom create ??????
                            routeProduct.getType().getModule().equals("DOC")
                                    &&
                                    routeProduct.getType().getName().equals("CREATE")
                    ) {
                        myRouteDocCreateProductList.add(routeProduct);
                        //break;

                    } else if (
                        // ????????? bom review ??????
                            routeProduct.getType().getModule().equals("DOC")
                                    &&
                                    routeProduct.getType().getName().equals("REVIEW")
                    ) {
                        myRouteDocReviewProductList.add(routeProduct);
                        //break;
                    }

                }
            }
        }

        //1 ::: TEMP SAVE RELEASE: Doc ??? TEMP SAVE = TRUE ;

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //1-1 temp save ??? ) ?????? ???????????? ?????? ????????? ????????????
        List<Document> myDocList = documentRepository.findByMember(member1);

        //1-2 temp-save ??? true ??? ?????? ?????? ?????????

        Set<Document> tempSavedDocumentList = new HashSet();
        for (Document doc : myDocList) {

            if (doc.getTempsave()){
                if(routeOrderingRepository.findByDocumentOrderByIdAsc(doc).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByDocumentOrderByIdAsc(doc)
                            .get(
                                    routeOrderingRepository.findByDocumentOrderByIdAsc(doc).size() - 1
                            );

                    int presentIdx = ordering.getPresent();

                    if(routeProductRepository.findAllByRouteOrdering(ordering).size()>
                            presentIdx) {
                        RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                        if (!routeProduct.isPreRejected()) {
                            tempSavedDocumentList.add(doc);
                        }
                    }
                }

                else {
                    tempSavedDocumentList.add(doc);
                }
            }
        }

        if (tempSavedDocumentList.size() > 0) {
            for (Document targetDocument: tempSavedDocumentList) {
                TodoResponse
                        releaseTodoResponse =
                        new TodoResponse(
                                targetDocument.getId(),
                                targetDocument.getDocumentTitle(),
                                targetDocument.getClassification().getDocClassification1().getName()
                                        +"/"+
                                        targetDocument.getClassification().getDocClassification2().getName(),
                                targetDocument.getDocumentNumber(),
                                -1L
                        );
                TEMP_SAVE.add(releaseTodoResponse);
            }
        }

        // 2 ::: ????????? RELEASE ???
        //  ????????? ??????????????? ????????? ????????????,
        // RELEASE CREATE ??????
        // ????????????????????? ????????? ?????????,
        // PRE - REJECTED=TRUE ??? ???
        HashSet<TodoResponse> rejectedDocumentTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteDocCreateProductList) {
            if (
                    routeProduct.isPreRejected()
            ) {

                Document targetDocument = routeProduct.getRouteOrdering().getDocument();

                if(targetDocument!=null) {
                    rejectedDocumentTodoResponses.add(
                            new TodoResponse(
                                    targetDocument.getId(),
                                    targetDocument.getDocumentTitle(),
                                    targetDocument.getClassification().getDocClassification1().getName()
                                    +"/"+
                                            targetDocument.getClassification().getDocClassification2().getName(),
                                    targetDocument.getDocumentNumber(),
                                    -1L
                            )
                    );
                }
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedDocumentTodoResponses);

        //3 :: RELEASE REVIEW ??? ??????, ?????? ?????? ?????????, ????????? ????????? RELEASE REVIEW
        HashSet<TodoResponse> needReviewRELEASETodoResponses = new HashSet<>();

        for(RouteProduct routeProduct : myRouteDocReviewProductList){

            //?????? ??????????????? ?????? ?????? ???????????????????????? ?????? ?????????, ??? ?????? ??????????????????.
            Document targetDocument = routeProduct.getRouteOrdering().getDocument();

            needReviewRELEASETodoResponses.add(
                    new TodoResponse(
                            targetDocument.getId(),
                            targetDocument.getDocumentTitle(),
                            targetDocument.getClassification().getDocClassification1().getName()
                                    +"/"+
                                    targetDocument.getClassification().getDocClassification2().getName(),
                            targetDocument.getDocumentNumber(),
                            -1L
                    )
            );
        }

        List<TodoResponse> REVIEW = new ArrayList<>(needReviewRELEASETodoResponses);

        // TOTAL

        ToDoSingle tempDocument = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle rejectedDocument = new ToDoSingle("Rejected Document", REJECTED);
        ToDoSingle reviewDocument = new ToDoSingle("Waiting Review", REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();


        toDoDoubleList.add(tempDocument);
        toDoDoubleList.add(rejectedDocument);
        toDoDoubleList.add(reviewDocument);

        return new ToDoDoubleList(toDoDoubleList);

    }



}