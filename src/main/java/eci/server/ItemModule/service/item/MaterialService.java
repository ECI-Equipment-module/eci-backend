package eci.server.ItemModule.service.item;

import eci.server.ItemModule.dto.material.MaterialListDto;
import eci.server.ItemModule.dto.material.MaterialReadCondition;
import eci.server.ItemModule.repository.material.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialListDto readAll(MaterialReadCondition cond) {
        return MaterialListDto.toDto(
                materialRepository.findAllByCondition(cond)
        );
    }

}
