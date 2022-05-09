package eci.server.DashBoardModule.service;

import eci.server.DashBoardModule.dto.ToDoDoubleList;
import eci.server.DashBoardModule.dto.ToDoSingle;
import eci.server.DashBoardModule.dto.projectTodo.ProjectTodoResponseList;
import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ColorRepository colorRepository;
    private final MaterialRepository materialRepository;
    private final ManufactureRepository manufactureRepository;
    private final AttachmentRepository attachmentRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;

    private final ProjectRepository projectRepository;

    private final ReadPartNumberService readPartNumber;

    private final FileService fileService;
    private final LocalFileService localFileService;

    private final AuthHelper authHelper;

    private final RoutePreset routePreset;

    //project to-do api
    public ToDoDoubleList readProjectTodo() {

        List<TodoResponse> TEMP_SAVE = new ArrayList<>();

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1-1 temp save 용 ) 내가 작성자인 모든 프로젝트들 데려오기
        List<Project> myProjectList = projectRepository.findByMember(member1);

        //1-2 temp-save 가 true 인 것만 담는 리스트
        List<Project> tempSavedProjectList = new ArrayList<>();

        for (Project project : myProjectList) {
            if (project.getTempsave()) {
                tempSavedProjectList.add(project);
                myProjectList.remove(tempSavedProjectList);
                //임시저장 진행 중인 것
            }
        }

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

//        ToDoDoubleList toDoDoubleList = new ToDoDoubleList();
        List<ToDoSingle> toDoDoubleList = new ArrayList<ToDoSingle>();
        toDoDoubleList.add(tempSave);
        toDoDoubleList.add(newProject);

        return new ToDoDoubleList(toDoDoubleList);

    }


}