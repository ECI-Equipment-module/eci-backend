package eci.server.CRCOModule.controller.cr;

import eci.server.CRCOModule.dto.cr.CrPagingDto;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
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


public class CrPageController {

    @Autowired
    ChangeRequestRepository changeRequestRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    @Value("${default.image.address}")
    private String defaultImageAddress;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/cr/page")
    public Page<CrPagingDto> readCrPages(@PageableDefault(size=5)
                                                        @SortDefault.SortDefaults({
                                                                @SortDefault(
                                                                        sort = "createdAt",
                                                                        direction = Sort.Direction.DESC)
                                                        })
                                                                Pageable pageRequest) {

        Page<ChangeRequest> ListBefore = changeRequestRepository.findAll(pageRequest);

        List<ChangeRequest> crs =
                ListBefore.stream().filter(
                        i-> (!i.isTempsave())
                ).collect(Collectors.toList());

        Page<ChangeRequest> crList = new PageImpl<>(crs);

        return crList.map(
                cr -> CrPagingDto.toDto(
                        cr.getNewItem(), routeOrderingRepository, cr, defaultImageAddress)
        );

    }

}
