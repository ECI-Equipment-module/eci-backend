package eci.server.ProjectModule.service;

import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;
import eci.server.ProjectModule.dto.*;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
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

    @Transactional
    public ProjectCreateUpdateResponse tempCreate(ProjectTemporaryCreateRequest req) {

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

        return new ProjectCreateUpdateResponse(project.getId());
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

        return new ProjectCreateUpdateResponse(project.getId());
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


    // read one project

    // delete one project
}
