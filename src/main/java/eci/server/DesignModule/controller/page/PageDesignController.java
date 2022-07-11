package eci.server.DesignModule.controller.page;

import eci.server.DesignModule.dto.DesignSimpleDto;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.DesignModule.service.DesignService;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ProjectModule.dto.project.ProjectMemberRequest;
import eci.server.ProjectModule.dto.project.ProjectSimpleDto;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequiredArgsConstructor
public class PageDesignController {
    private final DesignService designService;
    /**
     * 프로젝트 모듈에서의 프로젝트 리스트 (내가 만든 프로젝트들 maybe..?)
     */
    @Autowired
    DesignRepository designRepository;
    RouteOrderingRepository routeOrderingRepository;
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/design/page")
    @AssignMemberId
    public Page<DesignSimpleDto> pagingDesign(@PageableDefault(size=5)
                                                @SortDefault.SortDefaults({
                                                        @SortDefault(
                                                                sort = "createdAt",
                                                                direction = Sort.Direction.DESC)
                                                })
                                                        Pageable pageRequest,
                                               ProjectMemberRequest req) {



        Page<DesignSimpleDto> designSimpleDtos = designService.readPageAll(pageRequest, req);


        return designSimpleDtos;
    }


}

