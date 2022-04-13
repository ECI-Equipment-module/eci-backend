package eci.server.ItemModule.repository.color;

import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.color.ColorReadResonse;
import org.springframework.data.domain.Page;

public interface CustomColorRepository {
    Page<ColorReadResonse> findAllByCondition(ColorReadCondition cond);
}
