package eci.server.service.route;


import eci.server.dto.route.RouteCreateRequest;
import eci.server.dto.route.RouteDto;
import eci.server.dto.route.RouteReadCondition;
import eci.server.entity.route.Route;
import eci.server.exception.route.RouteNotFoundException;
import eci.server.repository.item.ItemRepository;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.route.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository RouteRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository ItemRepository;

    public List<RouteDto> readAll(RouteReadCondition cond) { // 1
        return RouteDto.toDtoList(
                RouteRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(cond.getItemId())
        );
    }

    @Transactional
    public void create(RouteCreateRequest req) { // 2
        RouteRepository.save(RouteCreateRequest.toEntity(
                req,
                memberRepository,
                ItemRepository,
                RouteRepository)
        );
    }

    @Transactional
    public void delete(Long id) { // 3
        Object RouteNotFoundException;
        Route Route = RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        Route.findDeletableRoute().ifPresentOrElse(RouteRepository::delete, Route::delete);
    }
}