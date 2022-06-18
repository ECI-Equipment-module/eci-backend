package eci.server.DashBoardModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.DashBoardModule.dto.ToDoDoubleList;
import eci.server.DashBoardModule.dto.ToDoSingle;
import eci.server.DashBoardModule.dto.myProject.TotalProject;
import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.repository.DesignRepository;
//import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.member.auth.AccessExpiredException;
import eci.server.ItemModule.exception.route.RouteProductNotFoundException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.repository.project.ProjectRepository;
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
                        routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("WORKING")
                ) {
                    working += 1;
                } else if (
                        routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("COMPLETE")
                ) {
                    complete += 1;
                } else if (
                        routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("RELEASE")
                ) {
                    release += 1;
                } else if (
                        routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1
                        ).getLifecycleStatus().equals("PENDING")
                ) {
                    pending += 1;
                } else if (
                        routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                                routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1
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
                    Objects.equals(project.getId(), projectRepository.findByNewItem(project.getNewItem()).get(
                            projectRepository.findByNewItem(project.getNewItem()).size() - 1
                    ).getId())
            ) {



                if(routeOrderingRepository.findByNewItem(project.getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItem(project.getNewItem()).get(
                            routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1);
                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected()) { //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                        //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                        tempSavedProjectList.add(project);
                        //임시저장 진행 중인 것
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
                                p.getProjectNumber()
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


                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 프로젝트 링크 안된 애만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) {
            if (projectRepository.findByNewItem(routeProduct.getRouteOrdering().getNewItem()).size() == 0) {

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                targetItem.getId(),
                                targetItem.getName(),
                                targetItem.getItemTypes().getItemType().toString(),
                                targetItem.getItemNumber().toString()
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
     *
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
                    Objects.equals(design.getId(), designRepository.findByNewItem(design.getNewItem()).get(
                            designRepository.findByNewItem(design.getNewItem()).size() - 1
                    ).getId())){
                //05-30 - 이 아이가 최신 아이일 때만! (최신 아니고 옛날 거면 필요 없음)


                if(routeOrderingRepository.findByNewItem(design.getNewItem()).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItem(design.getNewItem()).get(
                            routeOrderingRepository.findByNewItem(design.getNewItem()).size() - 1);
                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected()) { //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
                        //06-04 : 임시저장 이고 읽기 전용이 아니라면 임시저장에 뜨도록
                        tempSavedDesignList.add(design);
                        //임시저장 진행 중인 것
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
                                d.getNewItem().getItemNumber()
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

        for (RouteProduct routeProduct : myRouteProductList) {
            if (designRepository.findByNewItem(routeProduct.getRouteOrdering().getNewItem()).size() == 0) {

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                targetItem.getId(),
                                targetItem.getName(),
                                targetItem.getItemTypes().getItemType().toString(),
                                targetItem.getItemNumber().toString()
                        )
                );
            }
        }

        List<TodoResponse> NEW_DESIGN = new ArrayList<>(unlinkedItemTodoResponses);

        //3) REJECT - 라우트 프로덕트들 중에서 현재이고,

        // 디자인 설계이고,
        // 라우트프로덕트 멤버가 나이고,
        // REJECTED=TRUE 인 것
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
                                targetDesign.getNewItem().getItemNumber().toString()
                        )
                );
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedDesignTodoResponses);

        //4) REVISE - TODO : REVISE 추가되고 작업
        List<TodoResponse> REVISE = new ArrayList<>();

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
            RouteProduct targetRouteProduct = routeProductRepository.findById(routeProduct.getId() - 1)
                    .orElseThrow(RouteProductNotFoundException::new);

            Design targetDesign = targetRouteProduct.getDesign();

            needReviewDesignTodoResponses.add(
                    new TodoResponse(
                            targetDesign.getId(),
                            targetDesign.getNewItem().getName(),
                            targetDesign.getNewItem().getItemTypes().getItemType().toString(),
                            targetDesign.getNewItem().getItemNumber()
                    )
            );
        }


        List<TodoResponse> NEED_REVIEW = new ArrayList<>(needReviewDesignTodoResponses);

        ToDoSingle tempSave = new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle newDesign = new ToDoSingle("New Design", NEW_DESIGN);
        ToDoSingle rejectedDesign = new ToDoSingle("Rejected Design", REJECTED);
        ToDoSingle needRevise = new ToDoSingle("Need Revise", REVISE);
        ToDoSingle needReview = new ToDoSingle("Waiting Review", NEED_REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newDesign);
        toDoDoubleList.add(rejectedDesign);
        toDoDoubleList.add(needRevise);
        toDoDoubleList.add(needReview);

        return new ToDoDoubleList(toDoDoubleList);

    }

    /**
     * DESIGN TODO
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

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 개발BOM생성[설계자] 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        //routeProduct.getRoute_name().equals("개발BOM생성[설계자]")
                        (
                                routeProduct.getType().getModule().equals("BOM")
                                        &&
                                        routeProduct.getType().getName().equals("CREATE")
                        )
                ) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) new bom : 봄 링크 안된 아이템만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList) {
            if (bomRepository.findByNewItem(routeProduct.getRouteOrdering().getNewItem()).size() == 0) {

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                targetItem.getId(),
                                targetItem.getName(),
                                targetItem.getItemTypes().getItemType().toString(),
                                targetItem.getItemNumber().toString()
                        )
                );
            }
        }

        List<TodoResponse> NEW_BOM = new ArrayList<>(unlinkedItemTodoResponses);


        ToDoSingle newDesign = new ToDoSingle("New Bom", NEW_BOM);
        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(newDesign);


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

            if (newItem.isTempsave()){

                if(routeOrderingRepository.findByNewItem(newItem).size()>0){
                    RouteOrdering ordering = routeOrderingRepository.findByNewItem(newItem).get(
                            routeOrderingRepository.findByNewItem(newItem).size() - 1);
                    int presentIdx = ordering.getPresent();
                    RouteProduct routeProduct = routeProductRepository.findAllByRouteOrdering(ordering).get(presentIdx);
                    if (!routeProduct.isPreRejected()) { //06-18 거부된게 아닐때만 임시저장에, 거부된 것이라면 임시저장에 뜨면 안됨
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
                                d.getItemNumber()
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
                                    //TODO 이렇게 문자열로 구분하는게 아닌, ROUTE TYPE으로
//                            routeProduct.getRoute_name().equals("Item(사내가공품/외주구매품-시방)등록 Request(설계자)")
//                            || routeProduct.getRoute_name().equals("Item Request")
//                            || routeProduct.getRoute_name().equals("Item(외주구매품 단순)신청 Request(설계자)")
//                            || routeProduct.getRoute_name().equals("Item(제품)등록 Request(설계자)")
//                            || routeProduct.getRoute_name().equals("Item(원재료) Request(설계자)")

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
                                        target.getItemNumber().toString()
                                )
                        );
                    }
                }
            }
        }
        List<TodoResponse> REJECTED = new ArrayList<>(rejectedNewItemTodoResponses);

        //3) REVISE - TODO : REVISE 추가되고 작업
        List<TodoResponse> REVISE = new ArrayList<>();

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
                            newItem.getItemNumber()
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
}
