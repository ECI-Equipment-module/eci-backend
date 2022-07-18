package eci.server.ReleaseModule.controller;

import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.ReleaseModule.dto.ReleaseDto;
import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.repository.ReleaseRepository;
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
public class ReleasePageController {

    @Autowired
    private final ReleaseRepository releaseRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final AttachmentTagRepository attachmentTagRepositor;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    /**
     * 임시저장 = false 인 것만 부름 (라우트가 항상 존재)
     * @param pageRequest
     * @return
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping("/release/page")
    public Page<ReleaseDto> readCrPages(@PageableDefault(size = 5)
                                        @SortDefault.SortDefaults({
                                                @SortDefault(
                                                        sort = "createdAt",
                                                        direction = Sort.Direction.DESC)
                                        })
                                                Pageable pageRequest) {

        Page<Releasing> ListBefore =
                releaseRepository.findAll(pageRequest);

        List<Releasing> crs =
                ListBefore.stream().filter(
                        i -> (!i.getTempsave())
                ).collect(Collectors.toList());

        List<ReleaseDto> ReleaseDtoList = ReleaseDto.toDtoList(
                crs,
                routeOrderingRepository,
                routeProductRepository,
                attachmentTagRepositor,
                defaultImageAddress

        );

        Page<ReleaseDto> releaseList = new PageImpl<>(ReleaseDtoList);

        /**
         * 수정 필요
         */
        return releaseList;

    }

}

