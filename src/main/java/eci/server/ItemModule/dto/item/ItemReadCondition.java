package eci.server.ItemModule.dto.item;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemReadCondition {

    /**
     * 페이지 번호는 0 이상
     */
    @NotNull(message = "페이지 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요. (0 이상)")
    private Integer page;

    /**
     * 페이지 크기는 1 이상
     */
    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
    private Integer size;

    private List<Long> memberId = new ArrayList<>();

    //검색 조건 - 우선은 멤버로만 검색할 수 있도록 지정
}