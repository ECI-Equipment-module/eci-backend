package eci.server.CRCOModule.controller.co;

import eci.server.CRCOModule.dto.co.CoSearchDto;
import eci.server.CRCOModule.dto.cr.CrPagingDto;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.CRCOModule.repository.co.CoNewItemRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ReleaseModule.dto.CoListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor


public class CoPageController{

    @Autowired
    ChangeOrderRepository changeOrderRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final CoNewItemRepository coNewItemRepository;
    @Value("${default.image.address}")
    private String defaultImageAddress;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/co/page")
    public Page<CrPagingDto> readCrPages(@PageableDefault(size=5)
                                         @SortDefault.SortDefaults({
                                                 @SortDefault(
                                                         sort = "createdAt",
                                                         direction = Sort.Direction.DESC)
                                         })
                                                 Pageable pageRequest) {

        Page<ChangeOrder> ListBefore =
                changeOrderRepository.findAll(pageRequest);

        List<ChangeOrder> crs =
                ListBefore.stream().filter(
                        i-> (!i.getTempsave())
                ).collect(Collectors.toList());

        List<CrPagingDto> crPagingDtos = CrPagingDto.toCoPageDto(
                crs, routeOrderingRepository, defaultImageAddress
        ,coNewItemRepository);

        Page<CrPagingDto> crList = new PageImpl<>(crPagingDtos);



        /**
         * 수정 필요
         */
        return crList;

    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/releaseCoId")
    public CoListDto readCoFromRelease(@PageableDefault(size=5)
                                         @SortDefault.SortDefaults({
                                                 @SortDefault(
                                                         sort = "createdAt",
                                                         direction = Sort.Direction.DESC)
                                         })
                                                 Pageable pageRequest) {

        Page<ChangeOrder> ListBefore =
                changeOrderRepository.findAll(pageRequest);

        List<ChangeOrder> cos =
                ListBefore.stream().filter(
                        i-> (!i.getTempsave())
                ).collect(Collectors.toList());

        List<CoSearchDto> coPagingDtos =
                cos.stream().map(
                        CoSearchDto::toDto
                ).collect(Collectors.toList());

        Page<CoSearchDto> crList = new PageImpl<>(coPagingDtos);

        /**
         * 수정 필요
         */
        return CoListDto.toDto(crList);

    }

}