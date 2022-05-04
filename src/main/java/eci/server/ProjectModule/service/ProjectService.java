package eci.server.ProjectModule.service;

import eci.server.ItemModule.dto.item.ReadItemDto;
import eci.server.ItemModule.dto.member.MemberListDto;
import eci.server.ItemModule.dto.member.MemberReadCondition;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;

import eci.server.ProjectModule.dto.*;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.exception.ProjectUpdateImpossibleException;

import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.repository.projectAttachmentRepository.ProjectAttachmentRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectLevelRepository projectLevelRepository;
    private final ProduceOrganizationRepository produceOrganizationRepository;
    private final ClientOrganizationRepository clientOrganizationRepository;
    private final ProjectAttachmentRepository projectAttachmentRepositoryl;
    private final FileService fileService;
    private final LocalFileService localFileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;

    @Transactional
    public ProjectTempCreateUpdateResponse tempCreate(ProjectTemporaryCreateRequest req) {

        Project project = projectRepository.save(
                ProjectTemporaryCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository,
                        projectTypeRepository,
                        projectLevelRepository,
                        produceOrganizationRepository,
                        clientOrganizationRepository
                )
        );
        uploadAttachments(project.getProjectAttachments(), req.getAttachments());

        return new ProjectTempCreateUpdateResponse(project.getId());
    }


    @Transactional
    public ProjectCreateUpdateResponse create(ProjectCreateRequest req) {
        req.getAttachments().get(0).getContentType();

        Project project = projectRepository.save(
                ProjectCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository,
                        projectTypeRepository,
                        projectLevelRepository,
                        produceOrganizationRepository,
                        clientOrganizationRepository
                )
        );

        uploadAttachments(project.getProjectAttachments(), req.getAttachments());

        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByItem(project.getItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByItem(project.getItem()).get(routeOrdering.size()-1).getId();

        return new ProjectCreateUpdateResponse(project.getId(), routeId);
    }



    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 파일이 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    private void deleteAttachments(List<ProjectAttachment> attachments) {
//        attachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
        attachments.
                stream().
                forEach(
                        i -> i.setDeleted(true)
                );
    }

    @Transactional
    public ProjectTempCreateUpdateResponse update(Long id, ProjectUpdateRequest req) {

        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);


        if (project.getTempsave()==false){

            //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능

            throw new ProjectUpdateImpossibleException();
        }

        Project.FileUpdatedResult result = project.update(
                req,
                itemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository
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


    // read one project
    public ProjectDto read(Long id){
        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        return ProjectDto.toDto(
                targetProject,
                routeOrderingRepository,
                routeProductRepository
                );
    }

    public ProjectListDto readAll(ProjectReadCondition cond) {
        return ProjectListDto.toDto(
                projectRepository.findAllByCondition(cond)
        );
    }
     //delete one project

    @Transactional
    public void delete(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        deleteProjectAttachments(project.getProjectAttachments());
        projectRepository.delete(project);
    }

    private void deleteProjectAttachments(List<ProjectAttachment> projectAttachments) {
        projectAttachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

}
