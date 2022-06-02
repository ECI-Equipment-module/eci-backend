package eci.server.ItemModule.repository.manufacture;

//import eci.server.ItemModule.dto.manufacture.MakerReadCondition;
import eci.server.NewItemModule.dto.maker.MakerReadCondition;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomMakerRepository {
    //Page<MakerSimpleDto> findAllByCondition(MakerReadCondition cond);

    Page<MakerSimpleDto> findAllByCondition(eci.server.ItemModule.dto.manufacture.MakerReadCondition cond);
    //Page<MakerSimpleDto> findAllByCondition(eci.server.ItemModule.dto.manufacture.MakerReadCondition cond);
}
