package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
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

    private List<MultipartFile> images = new ArrayList<>();

    private List<MultipartFile> thumbnail = new ArrayList<>();

    public ItemCreateRequest(String name, String type,String width, String height, String weight, Long memberId, Boolean inProgress, List<MultipartFile> thumbnail) {

        this.name = name;
        this.type = type;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.memberId = memberId;
        this.inProgress = inProgress;
        this.thumbnail = thumbnail;
    }

    public static Item toEntity(ItemCreateRequest req, MemberRepository memberRepository) {

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
                req.thumbnail.stream().map(
                        i -> new Image(
                                i.getOriginalFilename())
                        ).collect(
                                toList()
                                )
        );
    }
}