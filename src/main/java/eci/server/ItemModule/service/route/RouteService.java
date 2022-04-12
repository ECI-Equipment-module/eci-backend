package eci.server.ItemModule.service.route;


<<<<<<< HEAD
import eci.server.ItemModule.dto.item.ImageDto;
import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.route.*;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
=======
import eci.server.ItemModule.dto.route.RouteCreateRequest;
import eci.server.ItemModule.dto.route.RouteDto;
import eci.server.ItemModule.dto.route.RouteReadCondition;
import eci.server.ItemModule.entity.route.Route;
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

=======
import java.util.List;

>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository RouteRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository ItemRepository;

<<<<<<< HEAD
    public RouteDto read(Long id) {
        return RouteDto.toDto(
                RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new)
        );
    }

    public List<RouteDto> readAll(RouteReadCondition cond) {
=======
    public List<RouteDto> readAll(RouteReadCondition cond) { // 1
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
        return RouteDto.toDtoList(
                RouteRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(cond.getItemId())
        );
    }

    @Transactional
<<<<<<< HEAD
    public void create(RouteCreateRequest req) {
=======
    public void create(RouteCreateRequest req) { // 2
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
        RouteRepository.save(RouteCreateRequest.toEntity(
                req,
                memberRepository,
                ItemRepository,
                RouteRepository)
        );
    }

    @Transactional
<<<<<<< HEAD
    public void delete(Long id) {
=======
    public void delete(Long id) { // 3
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
        Object RouteNotFoundException;
        Route Route = RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        Route.findDeletableRoute().ifPresentOrElse(RouteRepository::delete, Route::delete);
    }
<<<<<<< HEAD

    @Transactional
    public RouteUpdateResponse update(Long id, RouteUpdateRequest req) {
        Route route = RouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        RouteUpdateRequest updateResponse = route.update(
                req,
                memberRepository,
                ItemRepository,
                RouteRepository
        );

        return new RouteUpdateResponse(id);
    }

=======
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
}