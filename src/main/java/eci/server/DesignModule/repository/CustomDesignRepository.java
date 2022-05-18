package eci.server.DesignModule.repository;

import eci.server.DesignModule.dto.DesignReadCondition;
import eci.server.DesignModule.dto.DesignReadDto;
import org.springframework.data.domain.Page;

public interface CustomDesignRepository {
    Page<DesignReadDto> findAllByCondition(DesignReadCondition cond);
}
