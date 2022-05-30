//package eci.server.NewItemModule.dto.item;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import eci.server.ItemModule.dto.color.ColorDto;
//import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
//import eci.server.ItemModule.dto.material.MaterialSimpleDto;
//import eci.server.ItemModule.dto.member.MemberDto;
//import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
//import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
//import eci.server.ItemModule.entity.item.Item;
//import eci.server.ItemModule.entity.item.ItemType;
//import eci.server.ItemModule.entity.newRoute.RoutePreset;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReadItemDto {
//
//    private boolean tempsave;
//
//    private Long id;
//    private String name;
//    private String type;
//
//    private Integer itemNumber;
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
//    private List<MakerSimpleDto> manufactures;
//    private List<String> partnumbers;
//
//
//    //private List<RouteOrderingDto> routeDtoList;
//
//    private char revision;
//    private String workflowPhase;
//    private String lifecyclePhase;
//
//    private Long routeId;
//
//    //05-22 추가
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
//    private LocalDateTime createdAt;
//    private MemberDto creator;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
//    private LocalDateTime modifiedAt;
//    private MemberDto modifier;
//
//
//
//
//    public static ReadItemDto toDto(
//            Item item,
//            ItemDto itemDto,
//            RouteOrderingDto routeOrderingDto,
//            RouteProductDto routeProductDto,
//            List<String> partNumbers,
//            List<AttachmentDto> attachmentDtoList
//
//    ) {
//
//
//        return new ReadItemDto(
//                item.getTempsave(),//true면 임시저장 상태, false면 찐 저장 상태
//
//                item.getId(),
//                item.getName(),
//                item.getType(),
//
//                item.getItemNumber(),
//
//                item.getWidth(),
//                item.getHeight(),
//                item.getWeight(),
//                itemDto.getMember(),
//
//                itemDto.getThumbnail(),
//
//                attachmentDtoList,
//
//                itemDto.getColor(),
//
//                itemDto.getMaterialDto(),
//                itemDto.getMakerSimpleDtos(),
//
//                partNumbers,
//
//                //routeDtoList,
//
//                itemDto.getRevision(),
////                ((Character)newRouteDto.getRevisedCnt()),
//
//                routeProductDto.getType(),//이게 곧 work phase
//
//                routeOrderingDto.getLifecycleStatus(),//develop이나 release 중 하나
//
//                routeOrderingDto.getId(),
//
//                item.getCreatedAt(),
//                MemberDto.toDto(item.getMember()),
//
//                item.getModifier()==null?null:item.getModifiedAt(),
//                item.getModifier()==null?null:MemberDto.toDto(item.getModifier())
//
//
//        );
//    }
//
//
//    public static ReadItemDto noRoutetoDto(
//            Item item,
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
//        return new ReadItemDto(
//                itemDto.isTempsave(),//true면 임시저장 상태, false면 찐 저장 상태
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
//                itemDto.getMakerSimpleDtos(),
//
//                partnumbers,
//
//                //routeDtoList,
//
//                itemDto.getRevision(),
//                typeList.toString(),
//                "LIFECYCLE_NONE",
//                0L,
//
//                item.getCreatedAt(),
//                MemberDto.toDto(item.getMember()),
//
//                item.getModifier()==null?null:item.getModifiedAt(),
//                item.getModifier()==null?null:MemberDto.toDto(item.getModifier())
//
//
//        );
//    }
//
//}
