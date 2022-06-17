package eci.server.NewItemModule.service.supplier;


import eci.server.ItemModule.dto.manufacture.MakerListDto;

import eci.server.NewItemModule.dto.maker.MakerReadCondition;
import eci.server.NewItemModule.repository.maker.MakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MakerService {

    private final MakerRepository makerRepository;

    public MakerListDto readAll(MakerReadCondition cond) {
        return MakerListDto.toDto(
                makerRepository.findAllByCondition(cond)
        );
    }

}

