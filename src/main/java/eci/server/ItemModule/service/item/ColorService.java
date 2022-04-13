package eci.server.ItemModule.service.item;

import eci.server.ItemModule.dto.color.ColorCreateRequest;
import eci.server.ItemModule.dto.color.ColorListDto;
import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.repository.color.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorListDto readAll(ColorReadCondition cond) {
            return ColorListDto.toDto(
                colorRepository.findAllByCondition(cond)
        );
    }

}
