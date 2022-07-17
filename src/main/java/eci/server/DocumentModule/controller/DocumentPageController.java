package eci.server.DocumentModule.controller;

import eci.server.DocumentModule.dto.DocumentReadDto;
import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.repository.DocumentRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ReleaseModule.dto.ReleaseDto;
import eci.server.ReleaseModule.entity.Releasing;
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
public class DocumentPageController {
    @Autowired
    private final DocumentRepository documentRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/doc/page")
    public Page<DocumentReadDto> readDocPages(@PageableDefault(size = 5)
                                        @SortDefault.SortDefaults({
                                                @SortDefault(
                                                        sort = "createdAt",
                                                        direction = Sort.Direction.DESC)
                                        })
                                                Pageable pageRequest) {

        Page<Document> documents =
                documentRepository.findAll(pageRequest);

        List<Document> docs =
                documents.stream().filter(
                        i -> (!i.getTempsave())
                ).collect(Collectors.toList());

        List<DocumentReadDto> documentReadDtoList = DocumentReadDto.toDtoList(
                docs,
                routeOrderingRepository,
                routeProductRepository,
                defaultImageAddress

        );

        Page<DocumentReadDto> docList = new PageImpl<>(documentReadDtoList);

        /**
         * 수정 필요
         */
        return  docList;

    }
}
