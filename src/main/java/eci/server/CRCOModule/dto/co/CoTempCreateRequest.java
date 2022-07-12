package eci.server.CRCOModule.dto.co;

import com.sun.istack.Nullable;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.exception.CrEffectNotFoundException;
import eci.server.CRCOModule.exception.CrNotFoundException;
import eci.server.CRCOModule.exception.CrReasonNotFoundException;
import eci.server.CRCOModule.repository.cofeature.ChangedFeatureRepository;
import eci.server.CRCOModule.repository.cofeature.CoEffectRepository;
import eci.server.CRCOModule.repository.cofeature.CoStageRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.exception.CarTypeNotFoundException;
import eci.server.ProjectModule.exception.ClientOrganizationNotFoundException;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoTempCreateRequest {


    private Long clientOrganizationId;

    private String clientItemNumber;

    private String coNumber;

    private String coPublishPeriod;

    private String coReceivedPeriod;

    private Boolean difference;

    @Nullable
    private Long carTypeId;

    private Boolean costDifferent;

    private String costDifference;

    private Long coReasonId;
    private String coReason;

    private Long coStageId;

    private String applyPeriod;

    @Nullable
    private List<Long> coEffectId;

    private Long coImportanceId;

    private String name;

    private String content;

    @Null
    private Long memberId;

    //선택한 cr
    private List<Long> changeRequestIds = new ArrayList<>();
    //아래 세개가 co-
    private List<Long> changeFeaturesIds = new ArrayList<>();
    private List<String> changeContents = new ArrayList<>();
    private List<Long> newItemsIds = new ArrayList<>();

    /**
     * 추가된 파일을 첨부
     */
    private List<MultipartFile> attachments = new ArrayList<>();

    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    /**
     * 삭제될 파일 아이디 입력 - is deleted 만 true
     */
    private List<Long> deletedAttachments = new ArrayList<>();


    public static ChangeOrder toEntity(
            CoTempCreateRequest req,
            Long CoReasonId,

            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            CrReasonRepository crReasonRepository,
            CoEffectRepository coEffectRepository,
            CoStageRepository coStageRepository,
            MemberRepository memberRepository,
            CrImportanceRepository crImportanceRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemRepository newItemRepository, // coNewItem으로 바꿔줄거

            ChangeRequestRepository changeRequestRepository,

            ChangedFeatureRepository changedFeatureRepository

    ) {


        if (req.getTag().size() == 0) {

            return new ChangeOrder(
                    req.getClientOrganizationId() == null ? null :
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                                    .orElseThrow(ClientOrganizationNotFoundException::new),

                    req.getClientItemNumber()==null||req.getClientItemNumber().isBlank()?
                            " ":req.getClientItemNumber(),

                    "made when saved",
                    //String.valueOf(req.getCoReasonId() * 1000000 + (int) (Math.random() * 1000)),

                    req.coPublishPeriod.isEmpty() ? null : LocalDate.parse
                            (req.coPublishPeriod, DateTimeFormatter.ISO_DATE),

                    req.coReceivedPeriod.isEmpty() ? null : LocalDate.parse
                            (req.coReceivedPeriod, DateTimeFormatter.ISO_DATE),

                    req.getDifference(),

                    req.getCarTypeId() == null ?
                            null
                            :
                            carTypeRepository.findById(req.carTypeId)
                                    .orElseThrow(CarTypeNotFoundException::new),

                    req.getCostDifferent() != null && req.getCostDifferent(),

                    req.getCostDifference() == null || req.getCostDifference().isEmpty() ?
                            " " : req.getCostDifference(),

                    req.coReasonId == null ? null ://crReasonRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                            crReasonRepository.findById(CoReasonId).orElseThrow(CrReasonNotFoundException::new),

                    req.coStageId == null ? null ://coStageRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                            coStageRepository.findById(req.coStageId).orElseThrow(CrReasonNotFoundException::new),

                    req.applyPeriod.isEmpty() ? null : LocalDate.parse
                            (req.applyPeriod, DateTimeFormatter.ISO_DATE),

                    req.coEffectId == null || req.getCoEffectId().size()==0?
                            null
                            :
                            req.getCoEffectId().stream().map(
                                    i->coEffectRepository.findById(i).orElseThrow(CrEffectNotFoundException::new)
                            ).collect(toList()),

                    req.coImportanceId == null ? null ://crImportanceRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                            crImportanceRepository.findById(req.coImportanceId).orElseThrow(CrReasonNotFoundException::new),

                    req.getName() == null || req.getName().isEmpty() ?
                            " " : req.getName(),

                    req.getContent() == null || req.getContent().isEmpty() ?
                            " " : req.getContent(),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),
                    true, //tempsave
                    false, //readonly

                    req.getChangeRequestIds().stream().map(
                            i ->
                                    changeRequestRepository.findById(i).orElseThrow(CrNotFoundException::new)
                    ).collect(
                            toList()
                    ), //changeRequests,

                    ////////////////////////co-itme
                    req.getChangeFeaturesIds().stream().map(
                            i ->
                                    changedFeatureRepository.findById(i).orElseThrow(CrNotFoundException::new)
                    ).collect(
                            toList()
                    ), //changedFeatures

                    req.getChangeContents(), //changedContents

                    req.getNewItemsIds().stream().map(
                            i ->
                                    newItemRepository.findById(i).orElseThrow(CrNotFoundException::new)
                    ).collect(
                            toList()
                    )//newItems
                    ///////////////////////////////////////////co-tiem
            );

        }

        return new ChangeOrder(
                req.getClientOrganizationId() == null ? null :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())
                                .orElseThrow(ClientOrganizationNotFoundException::new),

                req.getClientItemNumber()==null||req.getClientItemNumber().isBlank()?
                        " ":req.getClientItemNumber(),

                "made when saved",
                //String.valueOf(req.getCoReasonId() * 1000000 + (int) (Math.random() * 1000)),

                req.coPublishPeriod.isEmpty() ? null : LocalDate.parse
                        (req.coPublishPeriod, DateTimeFormatter.ISO_DATE),

                req.coReceivedPeriod.isEmpty() ? null : LocalDate.parse
                        (req.coReceivedPeriod, DateTimeFormatter.ISO_DATE),

                req.getDifference(),

                req.getCarTypeId() == null ?
                        null ://carTypeRepository.findById(-1L).orElseThrow(CarTypeNotFoundException::new)
                        carTypeRepository.findById(req.carTypeId)
                                .orElseThrow(CarTypeNotFoundException::new),

                req.getCostDifferent() != null && req.getCostDifferent(),

                req.getCostDifference() == null || req.getCostDifference().isEmpty() ?
                        " " : req.getCostDifference(),

                req.coReasonId == null ? null ://crReasonRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                        crReasonRepository.findById(CoReasonId).orElseThrow(CrReasonNotFoundException::new),

                req.coStageId == null ? null ://coStageRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                        coStageRepository.findById(req.coStageId).orElseThrow(CrReasonNotFoundException::new),

                req.applyPeriod.isEmpty() ? null : LocalDate.parse
                        (req.applyPeriod, DateTimeFormatter.ISO_DATE),

                req.coEffectId == null || req.getCoEffectId().size()==0?
                        null
                        :
                        req.getCoEffectId().stream().map(
                                i->coEffectRepository.findById(i).orElseThrow(CrEffectNotFoundException::new)
                        ).collect(toList()),


                req.coImportanceId == null ? null ://crImportanceRepository.findById(-1L).orElseThrow(CrReasonNotFoundException::new):
                        crImportanceRepository.findById(req.coImportanceId).orElseThrow(CrReasonNotFoundException::new),

                req.getName() == null || req.getName().isEmpty() ?
                        " " : req.getName(),

                req.getContent() == null || req.getContent().isEmpty() ?
                        " " : req.getContent(),

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),
                true, //tempsave
                false, //readonly

                req.getChangeRequestIds().stream().map(
                        i ->
                                changeRequestRepository.findById(i).orElseThrow(CrNotFoundException::new)
                ).collect(
                        toList()
                ), //changeRequests,

                req.getChangeFeaturesIds().stream().map(
                        i ->
                                changedFeatureRepository.findById(i).orElseThrow(CrNotFoundException::new)
                ).collect(
                        toList()
                ), //changedFeatures

                req.getChangeContents(), //changedContents

                req.getNewItemsIds().stream().map(
                        i ->
                                newItemRepository.findById(i).orElseThrow(CrNotFoundException::new)
                ).collect(
                        toList()
                ), //newItems


                req.getAttachmentComment().size() > 0 ? (

                        // 06-18 ) 1) comment 가 있다면 순차대로 채워지게 하기
                        req.attachments.stream().map(
                                i -> new CoAttachment(
                                        i.getOriginalFilename(),
                                        attachmentTagRepository
                                                .findById(req.getTag().get(req.attachments.indexOf(i))).
                                                orElseThrow(AttachmentNotFoundException::new).getName(),

                                        req.getAttachmentComment().isEmpty() ?
                                                " " : req.getAttachmentComment().get(
                                                req.getAttachments().indexOf(i)
                                        )
                                        ,
                                        //찐 생성이므로 이때 추가되는 문서들 모두 save = true
                                        true //save 속성임
                                )
                        ).collect(
                                toList()
                        )
                ) :

                        // 06-18 ) 2) comment 가 없다면 빈칸으로 코멘트 채워지게 하기
                        req.attachments.stream().map(
                                i -> new CoAttachment(
                                        i.getOriginalFilename(),
                                        attachmentTagRepository
                                                .findById(req.getTag().get(req.attachments.indexOf(i))).
                                                orElseThrow(AttachmentNotFoundException::new).getName(),

                                        " "
                                        ,
                                        //찐 생성이므로 이때 추가되는 문서들 모두 save = true
                                        true //save 속성임
                                )

                        ).collect(

                                toList()

                        )
        );
    }


}
