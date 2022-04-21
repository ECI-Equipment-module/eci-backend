package eci.server.ItemModule.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.dto.manufacture.ManufactureSimpleDto;
import eci.server.ItemModule.dto.material.MaterialSimpleDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.NewRouteDto;
import eci.server.ItemModule.dto.newRoute.RouteProductDto;
import eci.server.ItemModule.dto.route.RouteDto;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadItemDto {

    private boolean tempsave;

    private Long id;
    private String name;
    private String type;

    private Integer itemNumber;

    private String width;
    private String height;
    private String weight;
    private MemberDto member;

    private List<ImageDto> thumbnail;

    private List<AttachmentDto> attachments;

    private ColorDto color;
    private List<MaterialSimpleDto> material;

    private List<ManufactureSimpleDto> manufactures;
    private List<String> partnumbers;

    private List<NewRouteDto> routeDtoList;

    private char revision;
    private String workflowPhase;
    private String lifecyclePhase;



    public static ReadItemDto toDto(
            ItemDto itemDto,
            List<NewRouteDto> routeDtoList,
            NewRouteDto newRouteDto,
            RouteProductDto routeProductDto,
            List<String> partNumbers,
            List<AttachmentDto> attachmentDtoList

    ) {


        return new ReadItemDto(
                itemDto.isTempsave(),//true면 임시저장 상태, false면 찐 저장 상태

                itemDto.getId(),
                itemDto.getName(),
                itemDto.getType(),

                itemDto.getItemNumber(),

                itemDto.getWidth(),
                itemDto.getHeight(),
                itemDto.getWeight(),
                itemDto.getMember(),

                itemDto.getThumbnail(),

                attachmentDtoList,

                itemDto.getColor(),

                itemDto.getMaterialDto(),
                itemDto.getManufactureSimpleDtos(),

                partNumbers,

                routeDtoList,

                (char)(newRouteDto.getRevisedCnt().intValue()),
//                ((Character)newRouteDto.getRevisedCnt()),

                routeProductDto.getType(),//이게 곧 work phase

                newRouteDto.getLifecycleStatus()//develop이나 release 중 하나

        );
    }


    public static ReadItemDto noRoutetoDto(
            ItemDto itemDto,
            List<NewRouteDto> routeDtoList,
            List<String> partnumbers,
            List<AttachmentDto> attachmentDtoList

    ) {

        return new ReadItemDto(
                itemDto.isTempsave(),//true면 임시저장 상태, false면 찐 저장 상태

                itemDto.getId(),
                itemDto.getName(),
                itemDto.getType(),

                itemDto.getItemNumber(),

                itemDto.getWidth(),
                itemDto.getHeight(),
                itemDto.getWeight(),
                itemDto.getMember(),
                itemDto.getThumbnail(),

                attachmentDtoList,

                itemDto.getColor(),

                itemDto.getMaterialDto(),
                itemDto.getManufactureSimpleDtos(),

                partnumbers,

                routeDtoList,

                '0',
                "WORKFLOW_NONE",
                "LIFESTATUS_NONE"

        );
    }

}
