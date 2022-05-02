package eci.server.ItemModule.dto.newRoute.projectRoute;

import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRouteOrderingCreateRequest {

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long projectId;

    @Null // 첫번째 멤버 아이디는 무조건 itemId 작성자랑 동일 인물
    private Long memberId;

    private ArrayList<ArrayList<Long>> memberIds;

    private String requestComment;

    public static RouteOrdering toEntity(
            ProjectRouteOrderingCreateRequest req,
            ProjectRepository projectRepository,
            RoutePreset routePreset,
            RouteTypeRepository routeTypeRepository
    ){
        Project targetProject = projectRepository.findById(req.projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<String> typeList = new ArrayList<>();

        //아이템 타입에따라서 라우트 타입이 선택된다.
        // TODO 라벨 아니고 ITEM.ROUTE_TYPE으로 선택해준다
        Integer routeType =
                ItemType.valueOf(targetProject.getItem().getType()).label();

        List routeProduct = List.of((routePreset.projectRouteName[routeType]));

        System.out.println(routeProduct.size());
        for(Object type : routeProduct){
            typeList.add(type.toString());
            System.out.println(type);
        }

        return new RouteOrdering(
                typeList.toString(),
                projectRepository.findById(req.projectId)
                        .orElseThrow(ProjectNotFoundException::new)
        );
    }

}


