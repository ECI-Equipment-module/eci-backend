package eci.server.ItemModule.service.item;

import eci.server.ItemModule.dto.manufacture.ManufactureListDto;
import eci.server.ItemModule.dto.manufacture.ManufactureReadCondition;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManufactureService {

    private final ManufactureRepository manufactureRepository;

    public ManufactureListDto readAll(ManufactureReadCondition cond) {
        return ManufactureListDto.toDto(
                manufactureRepository.findAllByCondition(cond)
        );
    }

}
