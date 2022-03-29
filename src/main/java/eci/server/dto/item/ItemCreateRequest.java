package eci.server.dto.item;

import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.entity.item.ItemType;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.repository.member.MemberRepository;
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
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {
    private final Logger logger = LoggerFactory.getLogger(ItemCreateRequest.class);

    @NotBlank(message = "아이템 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "아이템 타입을 입력해주세요.")
    private String type;

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Integer itemNumber;

    @NotNull(message = "너비를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private Long width;

    @NotNull(message = "높이를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private Long height;

    @NotNull(message = "무게를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private Long weight;

    // hidden = true
    @Null
    private Long memberId;

    private List<MultipartFile> thumbnail = new ArrayList<>();



    public ItemCreateRequest(String name, String type,Long width, Long height, Long weight, Long memberId, List<MultipartFile> thumbnail) {
        this.name = name;
        this.type = type;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.memberId = memberId;
        this.thumbnail = thumbnail;
    }

    public static Item toEntity(ItemCreateRequest req, MemberRepository memberRepository) {

//        System.out.println(ItemType.valueOf(req.type).label());
//        System.out.println(req.itemNumber);

        return new Item(
                req.name,
                req.type,
                ItemType.valueOf(req.type).label()+(int)(Math.random()*1000),
                req.width,
                req.height,
                req.weight,

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                req.thumbnail.stream().map(
                        i -> new Image(
                                i.getOriginalFilename())
                        ).collect(
                                toList()
                                )
        );
    }
}