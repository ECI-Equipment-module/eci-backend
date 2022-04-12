package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.exception.item.ManufactureNotFoundException;
import eci.server.ItemModule.exception.item.MaterialNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {
    private final Logger logger = LoggerFactory.getLogger(ItemCreateRequest.class);

    private ItemType itemType;

    @NotBlank(message = "아이템 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "아이템 타입을 입력해주세요.")
    private String type;

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Integer itemNumber;

    @NotNull(message = "너비를 입력해주세요.")
    private String width;

    @NotNull(message = "높이를 입력해주세요.")
    private String height;

    @NotNull(message = "무게를 입력해주세요.")
    private String weight;

    // hidden = true
    @Null
    private Long memberId;

    private Boolean inProgress;

    private List<MultipartFile> thumbnail = new ArrayList<>();

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();

    @NotNull(message = "색깔을 입력해주세요.")
    private Long colorId;

    @NotNull(message = "재료를 입력해주세요.")
    private List<Long> materials = new ArrayList<>();

    private List<Long> manufactures = new ArrayList<>();

    private List<String> partnumbers = new ArrayList<>();


    public static Item toEntity(
            ItemCreateRequest req,
            MemberRepository memberRepository,
            ColorRepository colorRepository,
            MaterialRepository materialRepository,
            ManufactureRepository manufactureRepository) {

        return new Item(
                req.name,
                req.type,
                ItemType.valueOf(req.type).label()*1000000+(int)(Math.random()*1000),
                req.width,
                req.height,
                req.weight,

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                req.inProgress,

                colorRepository.findById(
                        req.getColorId()
                ).orElseThrow(ColorNotFoundException::new),

                req.thumbnail.stream().map(
                        i -> new Image(
                                i.getOriginalFilename()
                        )
                ).collect(
                        toList()
                ),

                req.attachments.stream().map(
                        i -> new Attachment(
                                i.getOriginalFilename(),
                                req.getTag().get(req.attachments.indexOf(i)),
                                req.getAttachmentComment().get(req.attachments.indexOf(i))
                        )
                ).collect(
                        toList()
                ),

                req.materials.stream().map(
                        i ->
                                materialRepository.
                                        findById(i).orElseThrow(MaterialNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.manufactures.stream().map(
                        i ->
                                manufactureRepository.
                                        findById(i).orElseThrow(ManufactureNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.partnumbers
        );
    }
}