package eci.server.ItemModule.service.item;

import eci.server.ItemModule.dto.manufacture.MakerListDto;
import eci.server.ItemModule.dto.manufacture.MakerReadCondition;
import eci.server.ItemModule.repository.manufacture.MakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MakerService {

    private final MakerRepository manufactureRepository;

    public MakerListDto readAll(MakerReadCondition cond) {
        return MakerListDto.toDto(
                manufactureRepository.findAllByCondition(cond)
        );
    }

}
