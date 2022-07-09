package eci.server.CRCOModule.service.cr;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.CrCreateRequest;
import eci.server.CRCOModule.dto.CrReadDto;
import eci.server.CRCOModule.dto.CrTempCreateRequest;
import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.exception.CrNotFoundException;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;

import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.ProjectDto;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrService {

    private final FileService fileService;
    private final CrReasonRepository crReasonRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final CrSourceRepository crSourceRepository;
    private final CrImportanceRepository crImportanceRepository;
    private final MemberRepository memberRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final NewItemRepository newItemRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @Transactional
    public NewItemCreateResponse tempCreate(CrTempCreateRequest req) {

        Long crReasonId = req.getCrReasonId();

        ChangeRequest changeRequest = changeRequestRepository.save(

                CrTempCreateRequest.toEntity(
                        req,
                        crReasonId,
                        crReasonRepository,
                        crSourceRepository,
                        crImportanceRepository,
                        memberRepository,
                        attachmentTagRepository,
                        newItemRepository
                )
        );

        if(req.getTag().size()>0) {
            uploadAttachments(changeRequest.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(changeRequest.getId());
    }

    @Transactional
    public NewItemCreateResponse create(CrCreateRequest req) {

        Long crReasonId = req.getCrReasonId();

        if(crReasonId==1L) {
            if ((!req.getCrReason().isBlank()) || !(req.getCrReason() == null)) {
                CrReason addCrReason = crReasonRepository.save(
                        new CrReason(
                                req.getCrReason()
                        )
                );
                crReasonId = addCrReason.getId();
            }
        }

        ChangeRequest changeRequest = changeRequestRepository.save(

                CrCreateRequest.toEntity(
                        req,
                        crReasonId,
                        crReasonRepository,
                        crSourceRepository,
                        crImportanceRepository,
                        memberRepository,
                        attachmentTagRepository,
                        newItemRepository
                )
        );

        if(req.getTag().size()>0) {
            uploadAttachments(changeRequest.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(changeRequest.getId());
    }

    public CrReadDto read(Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByChangeRequest(changeRequest),
                        routeProductRepository,
                        routeOrderingRepository,
                        bomRepository,
                        preliminaryBomRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);

        if (routeDtoList.size() > 0) {
            RouteOrdering routeOrdering = routeOrderingRepository.findByChangeRequest(changeRequest).get(0);
            return CrReadDto.toDto(
                    changeRequest,
                    routeOrdering,
                    routeOrderingRepository,
                    routeProductRepository,
                    bomRepository,
                    preliminaryBomRepository,
                    attachmentTagRepository,
                    defaultImageAddress
            );
        }
        return CrReadDto.noRoutetoDto(
                changeRequest,
                attachmentTagRepository,
                defaultImageAddress,
                routeOrderingRepository
        );
    }



    private void uploadAttachments(
            List<CrAttachment> attachments,
            List<MultipartFile> filedAttachments
    ) {
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

    private void deleteAttachments(List<CrAttachment> attachments) {
        for(CrAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }

    @Transactional
    public void delete(Long id) {

        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElseThrow(CrNotFoundException::new);

        changeRequestRepository.delete(changeRequest);
    }

}
