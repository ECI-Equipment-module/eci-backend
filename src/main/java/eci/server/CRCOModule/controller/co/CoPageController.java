package eci.server.CRCOModule.controller.co;

import eci.server.CRCOModule.dto.cr.CrPagingDto;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
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

        Page<ChangeOrder> crList = new PageImpl<>(crs);

        /**
         * 수정 필요
         */
        return crList.map(
                cr -> CrPagingDto.toCoDto(
                        cr.getCoNewItems().get(0).getNewItem(), routeOrderingRepository, cr, defaultImageAddress)
        );

    }

}