//package eci.server.ProjectModule.dto.project;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import eci.server.ItemModule.dto.color.ColorDto;
//import eci.server.ItemModule.dto.item.AttachmentDto;
//import eci.server.ItemModule.dto.item.ImageDto;
//import eci.server.ItemModule.dto.item.ItemDto;
//import eci.server.ItemModule.dto.manufacture.ManufactureSimpleDto;
//import eci.server.ItemModule.dto.material.MaterialSimpleDto;
//import eci.server.ItemModule.dto.member.MemberDto;
//import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
//import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
//import eci.server.ItemModule.entity.item.ItemType;
//import eci.server.ItemModule.entity.newRoute.RoutePreset;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReadProjectDto {
//
//    private boolean tempsave;
//
//    private Long id;
//    private String name;
//    private String type;
//
//    private String itemNumber;
//
//    private String width;
//    private String height;
//    private String weight;
//    private MemberDto member;
//
//    private List<ImageDto> thumbnail;
//
//    private List<AttachmentDto> attachments;
//
//    private ColorDto color;
//    private List<MaterialSimpleDto> material;
//
//    private List<ManufactureSimpleDto> manufactures;
//    private List<String> partnumbers;
//
//
//    private List<RouteOrderingDto> routeDtoList;
//
//    private char revision;
//    private String workflowPhase;
//    private String lifecyclePhase;
//
//
//
//    public static eci.server.ItemModule.dto.item.ReadItemDto toDto(
//            ItemDto itemDto,
//            List<RouteOrderingDto> routeDtoList,
//            RouteOrderingDto newRouteDto,
//            RouteProductDto routeProductDto,
//            List<String> partNumbers,
//            List<AttachmentDto> attachmentDtoList
//
//    ) {
//
//
//        return new eci.server.ItemModule.dto.item.ReadItemDto(
//                itemDto.istempsave(),//true면 임시저장 상태, false면 찐 저장 상태
//
//                itemDto.getId(),
//                itemDto.getName(),
//                itemDto.getType(),
//
//                itemDto.getItemNumber(),
//
//                itemDto.getWidth(),
//                itemDto.getHeight(),
//                itemDto.getWeight(),
//                itemDto.getMember(),
//
//                itemDto.getThumbnail(),
//
//                attachmentDtoList,
//
//                itemDto.getColor(),
//
//                itemDto.getMaterialDto(),
//                itemDto.getManufactureSimpleDtos(),
//
//                partNumbers,
//
//                routeDtoList,
//
//                (char)(newRouteDto.getRevisedCnt().intValue()),
////                ((Character)newRouteDto.getRevisedCnt()),
//
//                routeProductDto.getType(),//이게 곧 work phase
//
//                newRouteDto.getLifecycleStatus()//develop이나 release 중 하나
//
//        );
//    }
//
//
//    public static eci.server.ItemModule.dto.item.ReadItemDto noRoutetoDto(
//            ItemDto itemDto,
//            List<RouteOrderingDto> routeDtoList,
//            List<String> partnumbers,
//            List<AttachmentDto> attachmentDtoList,
//            RoutePreset routePreset
//
//    ) {
//
//        List<String> typeList = new ArrayList<>();
//        Integer routeType =  ItemType.valueOf(itemDto.getType()).label();
//        List routeProduct = List.of((routePreset.itemRouteName[routeType]));
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//        }
//
//        return new eci.server.ItemModule.dto.item.ReadItemDto(
//                itemDto.istempsave(),//true면 임시저장 상태, false면 찐 저장 상태
//
//                itemDto.getId(),
//                itemDto.getName(),
//                itemDto.getType(),
//
//                itemDto.getItemNumber(),
//
//                itemDto.getWidth(),
//                itemDto.getHeight(),
//                itemDto.getWeight(),
//                itemDto.getMember(),
//                itemDto.getThumbnail(),
//
//                attachmentDtoList,
//
//                itemDto.getColor(),
//
//                itemDto.getMaterialDto(),
//                itemDto.getManufactureSimpleDtos(),
//
//                partnumbers,
//
//                routeDtoList,
//
//                '0',
//                typeList.toString(),
//                "LIFESTATUS_NONE"
//
//        );
//    }
//
//}
