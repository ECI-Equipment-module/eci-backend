package eci.server.dto.item;

import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.entity.item.ItemType;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.UUID;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {

    private ItemType itemType;

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

    @Null
    private Long memberId;

    private List<MultipartFile> images = new ArrayList<>();

    public static Item toEntity(ItemCreateRequest req, MemberRepository memberRepository) {
        return new Item(
                req.name,
                req.type,
                ItemType.valueOf(req.type).label()+req.itemNumber,
                req.width,
                req.height,
                req.weight,

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                req.images.stream().map(
                        i -> new Image(
                                i.getOriginalFilename())
                        ).collect(
                                toList()
                                )
        );
    }
}