package eci.server.ItemModule.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.route.RouteDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadItemDto {

    private Long id;
    private String name;
    private String type;
    private String width;
    private String height;
    private String weight;
    private MemberDto member;
    private List<ImageDto> thumbnail;

    private List<RouteDto> routeDtoList;

    private char revision;
    private String workflowPhase;
    private String lifecyclePhase;

    public static ReadItemDto toDto(ItemDto itemDto, List<RouteDto> routeDtoList, RouteDto routeDto) {

        return new ReadItemDto(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getType(),
                itemDto.getWidth(),
                itemDto.getHeight(),
                itemDto.getWeight(),
                itemDto.getMember(),
                itemDto.getThumbnail(),

                routeDtoList,

                ((char)routeDto.getRevisedCnt()),
                routeDto.getWorkflowPhase(),
                routeDto.getLifecycleStatus()

        );
    }

}
