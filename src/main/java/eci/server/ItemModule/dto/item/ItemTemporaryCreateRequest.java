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
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTemporaryCreateRequest {
    private final Logger logger = LoggerFactory.getLogger(ItemCreateRequest.class);

    private ItemType itemType;

    private String name;

    private String type;

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Integer itemNumber;

    private String width;

    private String height;

    private String weight;

    // hidden = true
    @Null
    private Long memberId;

    private Boolean inProgress;

    private List<MultipartFile> thumbnail = new ArrayList<>();

    private List<MultipartFile> attachments = new ArrayList<>();

    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();

    private Long colorId;

    private List<Long> materials = new ArrayList<>();

    private List<Long> manufactures = new ArrayList<>();

    private List<String> partnumbers = new ArrayList<>();


    public static Item toEntity(
            ItemTemporaryCreateRequest req,
            MemberRepository memberRepository,
            ColorRepository colorRepository,
            MaterialRepository materialRepository,
            ManufactureRepository manufactureRepository) {


        return new Item(
                req.name.isBlank() ? "이름을 입력해주세요" : req.name,
                req.type.isBlank() ? String.valueOf(ItemType.NONE) : req.type,
                ItemType.valueOf(req.type.isBlank()? "NONE":req.type)
                        .label()*1000000+(int)(Math.random()*1000),
                req.width.isBlank() ? "너비를 입력해주세요" : req.width,
                req.height.isBlank() ? "높이를 입력해주세요" : req.height,
                req.weight.isBlank() ? "무게를 입력해주세요" : req.weight,

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true, //inProgress = true
                false, // 임시


                colorRepository.findById(
                        req.getColorId()==null?99999L:req.getColorId()
                ).orElseThrow(ColorNotFoundException::new),

                req.thumbnail==null?
                        new ArrayList<>():
                                req.thumbnail.stream().map(
                        i -> new Image(
                                i.getOriginalFilename()
                        )
                ).collect(
                        toList()
                ),

                req.attachments==null?
                        new ArrayList<>()
                        :
                        req.attachments.stream().map(
                        i -> new Attachment(
                                i.getOriginalFilename(),
                                req.getTag().get(req.attachments.indexOf(i)),
                                req.getAttachmentComment().get(req.attachments.indexOf(i))
                        )
                ).collect(
                        toList()
                ),

                req.materials==null?
                        new ArrayList<>()
                        :req.materials.stream().map(
                        i ->
                                materialRepository.
                                        findById(i).orElseThrow(MaterialNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.manufactures==null?
                        new ArrayList<>()
                        :
                req.manufactures.stream().map(
                        i ->
                                manufactureRepository.
                                        findById(i).orElseThrow(ManufactureNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.partnumbers.size()==0?new ArrayList<>():req.partnumbers
        );
    }
}