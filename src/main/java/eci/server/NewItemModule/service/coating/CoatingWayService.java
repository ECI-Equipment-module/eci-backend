package eci.server.NewItemModule.service.coating;

import eci.server.NewItemModule.dto.coatingWay.CoatingWayReadCondition;
import eci.server.NewItemModule.dto.coatingcommon.CoatingListDto;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoatingWayService {

    private final CoatingWayRepository coatingWayRepository;

    public CoatingListDto readAll(CoatingWayReadCondition cond) {
        return CoatingListDto.toDto(
                coatingWayRepository.findAllByCondition(cond)
        );
    }

}
