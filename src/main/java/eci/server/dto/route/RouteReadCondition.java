package eci.server.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteReadCondition {
    @NotNull(message = "아이템 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 아이템 번호를 입력해주세요. (0 이상)")
    private Long itemId;
}