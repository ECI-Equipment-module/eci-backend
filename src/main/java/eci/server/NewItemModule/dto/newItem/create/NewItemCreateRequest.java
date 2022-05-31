package eci.server.NewItemModule.dto.newItem.create;

import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.exception.item.*;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.manufacture.MakerRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemCreateRequest {

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification1;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification2;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification3;

    @NotNull(message = "아이템 타입을 입력해주세요.")
    private String type;
    //ItemTypes 으로 변환하기

    @Null
    private Integer itemNumber;

    @Null
    private String name;

    private boolean share;

    private String carType;

    // 맞춤 속성 ) boolean 빼고 다 만듦
    // integrate; curve;  forming;

    private String forming; //ChoiceField 5,6

    private String curve; //ChoiceField 3(평면),4 (곡면)

    private String integrate;

    private String width;

    private String height;

    private String thickness;

    private String weight;

    private String importance;

    private Long colorId;

    private Integer loadQuantity;

    private Long coatingWayId;

    private Long coatingTypeId;

    private Integer modulus;

    private String screw;

    private String cuttingType;

    private String lcd;

    private Integer displaySize;

    private Integer screwHeight;

    private Long clientOrganizationId;

    private Long supplierOrganizationId;

    private Long makersId;

    private List<MultipartFile> thumbnail = new ArrayList<>();

    private List<MultipartFile> attachments = new ArrayList<>();

    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();

    private List<Long> maker = new ArrayList<>();

    private List<String> partnumbers = new ArrayList<>();

    @Null
    private Long memberId;

    public static NewItem toEntity(
            NewItemCreateRequest req,
            MemberRepository memberRepository,
            ColorRepository colorRepository,
            MakerRepository makerRepository) {

        if (req.getTag().size() == 0) {
            return new NewItem(
                    req.name,
                    req.type.isBlank() ? "NONE" : req.type,
                    ItemType.valueOf(
                                    req.type.isBlank() ? "NONE" : req.type)
                            .label() * 1000000 + (int) (Math.random() * 1000),
                    req.width,
                    req.height,
                    req.weight,

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    false, //임시저장 끝
                    false, //생성 시에는 개정되는 것이 false

                    colorRepository.findById(
                            req.getColorId()
                    ).orElseThrow(ColorNotFoundException::new),

                    req.thumbnail.stream().map(
                            i -> new NewItemImage(
                                    i.getOriginalFilename()
                            )
                    ).collect(
                            toList()
                    ),

                    req.makers.stream().map(
                            i ->
                                    makerRepository.
                                            findById(i).orElseThrow(ManufactureNotFoundException::new)
                    ).collect(
                            toList()
                    ),

                    req.partnumbers
            );
        }

        return new NewItem(
                req.name,
                req.type.isBlank() ? "NONE" : req.type,
                ItemType.valueOf(
                                req.type.isBlank() ? "NONE" : req.type)
                        .label() * 1000000 + (int) (Math.random() * 1000),
                req.width,
                req.height,
                req.weight,

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                false, //임시저장 끝
                false, //생성 시에는 개정되는 것이 false

                colorRepository.findById(
                        req.getColorId()
                ).orElseThrow(ColorNotFoundException::new),

                req.thumbnail.stream().map(
                        i -> new NewItemImage(
                                i.getOriginalFilename()
                        )
                ).collect(
                        toList()
                ),
                req.attachments.stream().map(
                        i -> new NewItemAttachment(
                                i.getOriginalFilename(),
                                req.getTag().get(req.attachments.indexOf(i)),
                                req.getAttachmentComment().get(req.attachments.indexOf(i))
                        )
                ).collect(
                        toList()
                ),

                req.maker.stream().map(
                        i ->
                                makerRepository.
                                        findById(i).orElseThrow(ManufactureNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.partnumbers
        );

    }

}