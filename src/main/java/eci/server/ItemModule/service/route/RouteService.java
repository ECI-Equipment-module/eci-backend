package eci.server.ItemModule.service.route;


import eci.server.ItemModule.dto.item.ImageDto;
import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.route.*;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository RouteRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository ItemRepository;

    public RouteDto read(Long id) {
        return RouteDto.toDto(
                RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new)
        );
    }

    public List<RouteDto> readAll(RouteReadCondition cond) {
        return RouteDto.toDtoList(
                RouteRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(cond.getItemId())
        );
    }

    @Transactional
    public void create(RouteCreateRequest req) {
        RouteRepository.save(RouteCreateRequest.toEntity(
                req,
                memberRepository,
                ItemRepository,
                RouteRepository)
        );
    }

    @Transactional
    public void delete(Long id) {
        Object RouteNotFoundException;
        Route Route = RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        Route.findDeletableRoute().ifPresentOrElse(RouteRepository::delete, Route::delete);
    }

    @Transactional
    public RouteUpdateResponse update(Long id, RouteUpdateRequest req) {
        RouteRepository.save(RouteUpdateRequest.toEntity(
                req,
                memberRepository,
                ItemRepository,
                RouteRepository)
        );
        return new RouteUpdateResponse(id);
    }

}