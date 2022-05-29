package eci.server.NewItemModule.service.coating;

import eci.server.ItemModule.dto.color.ColorListDto;
import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.NewItemModule.dto.coatingType.CoatingTypeReadCondition;
import eci.server.NewItemModule.dto.coatingcommon.CoatingListDto;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoatingTypeService {

    private final CoatingTypeRepository coatingTypeRepository;

    public CoatingListDto readAll(CoatingTypeReadCondition cond) {
        return CoatingListDto.toDto(
                coatingTypeRepository.findAllByCondition(cond)
        );
    }

}
