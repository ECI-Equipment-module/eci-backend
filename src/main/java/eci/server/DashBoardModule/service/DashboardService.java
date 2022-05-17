package eci.server.DashBoardModule.service;

import eci.server.DashBoardModule.dto.ToDoDoubleList;
import eci.server.DashBoardModule.dto.ToDoSingle;
import eci.server.DashBoardModule.dto.myProject.TotalProject;
import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.AttachmentRepository;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.config.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final DesignRepository designRepository;

    private final ProjectRepository projectRepository;

    private final AuthHelper authHelper;

    public TotalProject readProjectTotal(){
        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        List<Project> myProjectList = projectRepository.findByMember(member1);

        int totalNumber;
        int working = 0 ;
        int complete = 0 ;
        int release = 0 ;
        int pending = 0 ;
        int drop = 0 ;


        for(Project project : myProjectList){
            if(!project.getTempsave()){
                if(
                        routeOrderingRepository.findByItem(project.getItem()).get(
                                routeOrderingRepository.findByItem(project.getItem()).size()-1
                ).getLifecycleStatus().equals("WORKING")
                ){
                    working+=1;
                }
                else if (
                        routeOrderingRepository.findByItem(project.getItem()).get(
                                routeOrderingRepository.findByItem(project.getItem()).size()-1
                        ).getLifecycleStatus().equals("COMPLETE")
                ){
                    complete+=1;
                }
                else if (
                        routeOrderingRepository.findByItem(project.getItem()).get(
                                routeOrderingRepository.findByItem(project.getItem()).size()-1
                        ).getLifecycleStatus().equals("RELEASE")
                ){
                    release+=1;
                }
                else if (
                        routeOrderingRepository.findByItem(project.getItem()).get(
                                routeOrderingRepository.findByItem(project.getItem()).size()-1
                        ).getLifecycleStatus().equals("PENDING")
                ){
                    pending+=1;
                }
                else if (
                        routeOrderingRepository.findByItem(project.getItem()).get(
                                routeOrderingRepository.findByItem(project.getItem()).size()-1
                        ).getLifecycleStatus().equals("DROP")
                ){
                    drop+=1;
                }
        }

            }

        totalNumber = working + complete + release + pending + drop;


        return new TotalProject(
                totalNumber,
                (double)working/totalNumber,
                (double)complete/totalNumber,
                (double)release/totalNumber,
                (double)pending/totalNumber,
                (double)drop/totalNumber
        );

    }



    //project to-do api

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
            if (project.getTempsave()) {
                tempSavedProjectList.add(project);
                //임시저장 진행 중인 것
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
                    if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                            routeProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {
                        myRouteProductList.add(routeProduct);
                        break;
                    }

                }
            }

            //3) 프로젝트 링크 안된 애만 담기
            HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

            for (RouteProduct routeProduct : myRouteProductList){
                if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){

                    Item targetItem = routeProduct.getRouteOrdering().getItem();

                    unlinkedItemTodoResponses.add(
                            new TodoResponse(
                                    targetItem.getId(),
                                    targetItem.getName(),
                                    targetItem.getType(),
                                    targetItem.getItemNumber().toString()
                            )
                    );
                }
            }

        List<TodoResponse> NEW_PROJECT = new ArrayList<>(unlinkedItemTodoResponses);

        ToDoSingle tempSave= new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle newProject= new ToDoSingle("New Project", NEW_PROJECT);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newProject);

        return new ToDoDoubleList(toDoDoubleList);

    }

    //project to-do api

    public ToDoDoubleList readDesignTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save 용 ) 내가 작성자인 모든 프로젝트들 데려오기
        List<Design> myDesignList = designRepository.findByMember(member1);
        Iterator<Design> myDesignListItr = myDesignList.iterator();

        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<Design> tempSavedDesignList = new ArrayList<>();

        for (Design design : myDesignList) {
            if (design.getTempsave()) {
                tempSavedDesignList.add(design);
                //임시저장 진행 중인 것
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

        if (tempSavedDesignList.size() > 0) {
            for (Design d : tempSavedDesignList) {
                TodoResponse
                        projectTodoResponse =
                        new TodoResponse(
                                d.getId(),
                                d.getItem().getName(),
                                d.getItem().getType(),
                                d.getItem().getItemNumber().toString()
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

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 기구Design생성[설계자] 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        routeProduct.getRoute_name().equals("기구Design생성[설계자]")) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 디자인 링크 안된 아이템만 담기
        HashSet<TodoResponse> unlinkedItemTodoResponses = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList){
            if(designRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){

                Item targetItem = routeProduct.getRouteOrdering().getItem();

                unlinkedItemTodoResponses.add(
                        new TodoResponse(
                                targetItem.getId(),
                                targetItem.getName(),
                                targetItem.getType(),
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

        for (RouteProduct routeProduct : myRouteProductList){ //myRoute-> 내꺼 + 현재
            if(routeProduct.isRejected() && routeProduct.getRoute_name().equals("기구Design생성[설계자]")){

                Design targetDesign = routeProduct.getDesign();

                rejectedDesignTodoResponses.add(
                        new TodoResponse(
                                targetDesign.getId(),
                                targetDesign.getItem().getName(),
                                targetDesign.getItem().getType(),
                                targetDesign.getItem().getItemNumber().toString()
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
        HashSet<TodoResponse> needReviewDesignTodoResponses = new HashSet<>();
/////이거 맞는지 모르겠다 검토 필요
        for (RouteProduct routeProduct : myRouteProductList){ //myRoute-> 내꺼 + 현재
            if(routeProduct.getRoute_name().equals("기구Design Review[설계팀장]")){

                Design targetDesign = routeProduct.getDesign();

                needReviewDesignTodoResponses.add(
                        new TodoResponse(
                                targetDesign.getId(),
                                targetDesign.getItem().getName(),
                                targetDesign.getItem().getType(),
                                targetDesign.getItem().getItemNumber().toString()
                        )
                );
            }
        }

        List<TodoResponse> NEED_REVIEW = new ArrayList<>(needReviewDesignTodoResponses);

        ToDoSingle tempSave= new ToDoSingle("Save as Draft", TEMP_SAVE);
        ToDoSingle newDesign= new ToDoSingle("New Design", NEW_DESIGN);
        ToDoSingle rejectedDesign= new ToDoSingle("Rejected Design", REJECTED);
        ToDoSingle needRevise= new ToDoSingle("Need Revise", REVISE);
        ToDoSingle needReview= new ToDoSingle("Waiting Review", NEED_REVIEW);

        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newDesign);
        toDoDoubleList.add(rejectedDesign);
        toDoDoubleList.add(needRevise);
        toDoDoubleList.add(needReview);

        return new ToDoDoubleList(toDoDoubleList);

    }


}