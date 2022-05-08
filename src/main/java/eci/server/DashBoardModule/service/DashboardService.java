package eci.server.DashBoardModule.service;

import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponseList;
import eci.server.DashBoardModule.dto.projectTodo.ProjectTodoResponse;
import eci.server.DashBoardModule.dto.projectTodo.ProjectTodoResponseList;
import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
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
import java.util.List;

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
    public ProjectTodoResponseList readProjectTodo() {

        List<ProjectTodoResponse> TEMP_SAVE = new ArrayList<>();

        List<ProjectTodoResponse> NEW_PROJECT1 = new ArrayList<>();
        List<ProjectTodoResponse> NEW_PROJECT2 = new ArrayList<>();

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
                ProjectTodoResponse
                        projectTodoResponse =
                        new ProjectTodoResponse(
                                p.getId(),
                                p.getName(),
                                p.getProjectType().getName(),
                                p.getProjectNumber()
                        );
                TEMP_SAVE.add(projectTodoResponse);
            }
        }

        // NEW PROJECT
        //후보 1) 만들었으나, 아직 route 에 승인해달라고 보내지 않음
        // 프로젝트의 아이템의 라우트 오더링의 present 의 present 가 아직도 1 (링크단계 ) 이라면

        List<Project> newProjectList1 = new ArrayList<>();

        //이때 my project 에서는 찐 저장된 애들만 남아있다.
        for (Project project : myProjectList) {

            if (
                //프로젝트 링크를 가지는 아이템 속성을 가지는 경우에만 검사 수행
                    project.getItem().getType().equals("TYPE4")
                            &&
                            //현재 PRESENT (현재 진행 중인) 가 링크인 단계면 (TYPE4의 링크 단계는 1) -
                            // 아직 링크 진행 중이란 말이니깐 승인이 안됐다는 것것
                            routeOrderingRepository.findByItem(project.getItem()).get(
                                    routeOrderingRepository.findByItem(project.getItem()).size() - 1
                            ).getPresent() == 1
            ) {
                newProjectList1.add(project);
            }

            if (newProjectList1.size() > 0) {
                for (Project p : newProjectList1) {
                    ProjectTodoResponse
                            projectTodoResponse =
                            new ProjectTodoResponse(
                                    p.getId(),
                                    p.getName(),
                                    p.getProjectType().getName(),
                                    p.getProjectNumber()
                            );
                    NEW_PROJECT1.add(projectTodoResponse);
                }
            }

            //후보 2) 프로젝트 승인 여부 관계 없이 프로젝트의 아이템의 라우트 오더링이 아직 진행 중
            // (아이템의 라우트오더링의 life cycle 을 확인하면 됨)
            List<Project> newProjectList2 = new ArrayList<>();

            //이때 my project 에서는 찐 저장된 애들만 남아있다.
            for (Project project2 : myProjectList) {

                if (
                    //프로젝트 링크를 가지는 아이템 속성을 가지는 경우에만 검사 수행
                        routeOrderingRepository.findByItem(project2.getItem()).get(
                                routeOrderingRepository.findByItem(project2.getItem()).size() - 1
                        ).getLifecycleStatus().equals("WORKING")

                ) {
                    newProjectList2.add(project2);
                }

                if (newProjectList2.size() > 0) {
                    for (Project p : newProjectList2) {
                        ProjectTodoResponse
                                projectTodoResponse =
                                new ProjectTodoResponse(
                                        p.getId(),
                                        p.getName(),
                                        p.getProjectType().toString(),
                                        p.getProjectNumber()
                                );
                        NEW_PROJECT2.add(projectTodoResponse);
                    }
                }
            }
        }
        return new ProjectTodoResponseList(TEMP_SAVE, NEW_PROJECT1, NEW_PROJECT2);
    }


}