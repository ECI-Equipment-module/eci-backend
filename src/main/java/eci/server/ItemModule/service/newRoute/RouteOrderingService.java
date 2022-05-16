package eci.server.ItemModule.service.newRoute;

import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotLinkedException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.newRoute.*;
import eci.server.ItemModule.dto.route.*;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RejectImpossibleException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;

import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotLinkedException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteOrderingService {

    private final ProjectRepository projectRepository;
    private final RouteProductRepository routeProductRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final DesignRepository designRepository;

    private final RouteOrderingRepository routeOrderingRepository;
    private final RoutePreset routePreset;

    private final RouteTypeRepository routeTypeRepository;

    public RouteOrderingDto read(Long id) {
        return RouteOrderingDto.toDto(
                routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new),
                routeProductRepository,
                routeOrderingRepository
        );
    }

    public List<RouteOrderingDto> readAll(RouteOrderingReadCondition cond) {

        List<RouteOrdering> newRoutes = routeOrderingRepository.findByItem(
                itemRepository.findById(cond.getItemId())
                        .orElseThrow(RouteNotFoundException::new)
        );

        return RouteOrderingDto.toDtoList(
                newRoutes,
                routeProductRepository,
                routeOrderingRepository
        );
    }

    @Transactional
    public RouteOrderingCreateResponse createItemRoute(RouteOrderingCreateRequest req) {
        RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest.toEntity(
                        req,
                        itemRepository,
                        routePreset,
                        routeTypeRepository
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toEntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository

                );

        for(RouteProduct routeProduct : routeProductList ){

            RouteProduct routeProduct1 =
                    routeProductRepository.save(routeProduct);
            System.out.println(routeProduct1.getRoute_name());
                    System.out.println(routeProduct1.getMembers().get(0).getMember());
                    System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
        }

        return new RouteOrderingCreateResponse(newRoute.getId());
    }

    @Transactional
    public List<RouteProductDto> rejectUpdate(
            Long id,
            String rejectComment,
            Integer rejectedSequence
    ) {

        RouteOrdering routeOrdering = routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        //추가적으로 만들어진 애들 반환
        List<RouteProduct> rejectUpdatedRouteProductList = routeOrdering.rejectUpdate(
                id,
                rejectComment,
                rejectedSequence,
                routeOrderingRepository,
                routeProductRepository

        );

        List<RouteProduct> addedProducts = new ArrayList<>();

        // 새로 만들어진 애들 저장
        for(RouteProduct routeProduct:rejectUpdatedRouteProductList){
            addedProducts.add(routeProductRepository.save(routeProduct));
        }

        // 처음으로 복제된 애는 거부대상 아이의 복제품 => 얘의 set reject=true로 변경
        addedProducts.get(0).setRejected(true);
        Integer range = addedProducts.get(0).getSequence()+1;
        //show=false인 애들 삭제할건데 검사범위는 거부대상아이 전의

//        // present는 현재 만들어진 애로 설정
//        newRoute.setPresent(addedProducts.get(0).getSequence());

        List<RouteProduct> deletedList =
        //isShow 가 false 인 것은 삭제 처리
                routeProductRepository.findAllByRouteOrdering(routeOrdering)
                        .subList(routeOrdering.getPresent()-1, range)
                        .stream().filter(
                                d -> !d.isRoute_show()
                        )
                        .collect(
                                Collectors.toList()
                        );

        //본격 삭제 진행
        for(RouteProduct routeProduct : deletedList){
            routeProductRepository.delete(routeProduct);
        }

        // 기존 + 새 라우트프로덕트까지 해서 돌려주기
        return RouteProductDto.toProductDtoList(
                routeProductRepository.findAllByRouteOrdering(routeOrdering)
        );
    }

    @Transactional
    public RouteUpdateResponse approveUpdate(Long id, RouteOrderingUpdateRequest req) {

        RouteOrdering routeOrdering = routeOrderingRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);


        List<RouteProduct> presentRouteProductCandidate = routeProductRepository
                .findAllByRouteOrdering(routeOrdering);

        //현재 진행중인 라우트프로덕트
        if(routeOrdering.getLifecycleStatus().equals("COMPLETE")){
            throw new UpdateImpossibleException();
        }

        if(presentRouteProductCandidate.size()==routeOrdering.getPresent()){
            //만약 present 가 끝까지 닿았으면 현재 complete 된 상황!
            routeOrdering.updateToComplete();
            //throw new UpdateImpossibleException();

        }
        else {
            RouteProduct targetRoutProduct = presentRouteProductCandidate.get(routeOrdering.getPresent());

            // TODO : 함수로 따로 빼기 availableAccept("route_name)
            //route_name에 따른 조건을 각각 설정해서 해당 조건 부합할 때만 accept 가능하게,
            // 아니면 exception 날리게 설정
            if (targetRoutProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {
                //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것

                //아이템에 링크된 맨 마지막 (최신) 프로젝트 데려오기
                if (projectRepository.findByItem(routeOrdering.getItem()).size() == 0) {
                    throw new ProjectNotLinkedException();
                } else {
                    Project linkedProject =
                            projectRepository.findByItem(routeOrdering.getItem())
                                    .get(
                                            projectRepository.findByItem(routeOrdering.getItem()).size() - 1
                                    );
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setProject(linkedProject);

                    linkedProject.finalSaveProject();
                }
            }

            /////////////////////////////////////////////////////////////////////////////////

            else if (targetRoutProduct.getRoute_name().equals("기구Design생성[설계자]")) {
                //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것

                //아이템에 링크된 맨 마지막 (최신) 디자인 데려오기
                if (designRepository.findByItem(routeOrdering.getItem()).size() == 0) {
                    throw new DesignNotLinkedException();
                } else {
                    Design linkedDesign =
                            designRepository.findByItem(routeOrdering.getItem())
                                    .get(
                                            designRepository.findByItem(routeOrdering.getItem()).size() - 1
                                    );
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setDesign(linkedDesign);
                    // 해당 design 의 임시저장을 false
                    linkedDesign.finalSaveDesign();
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////////////////////////////////
            RouteOrderingUpdateRequest newRouteUpdateRequest =
                    routeOrdering
                            .update(
                                    req,
                                    routeProductRepository
                            );
        }
        return new RouteUpdateResponse(id);
    }

}