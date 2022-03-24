package eci.server.dto.item;

import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "0원 이상을 입력해주세요")
    private Long price;

    @Null
    private Long memberId;

    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요.")
    private Long categoryId;

    private List<MultipartFile> images = new ArrayList<>();

    public static Item toEntity(ItemCreateRequest req, MemberRepository memberRepository) {
        return new Item(
                req.title,
                req.content,
                req.price,
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                req.images.stream().map(i -> new Image(i.getOriginalFilename())).collect(toList())
        );
    }
}