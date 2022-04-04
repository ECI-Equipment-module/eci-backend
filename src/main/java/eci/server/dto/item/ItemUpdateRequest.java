package eci.server.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    @NotBlank(message = "아이템 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "아이템 타입을 입력해주세요.")
    private String type;

    @NotNull(message = "너비를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private String width;

    @NotNull(message = "높이를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private String height;

    @NotNull(message = "무게를 입력해주세요.")
    @PositiveOrZero(message = "0 이상을 입력해주세요")
    private String weight;

    /**
     * 추가된 이미지를 첨부
     */
    private List<MultipartFile> addedImages = new ArrayList<>();


    /**
     * 제거된 이미지 아이디 입력
     */
    private List<Long> deletedImages = new ArrayList<>();
}

