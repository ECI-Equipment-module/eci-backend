package eci.server.BomModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final BomGuard bomGuard;


}
