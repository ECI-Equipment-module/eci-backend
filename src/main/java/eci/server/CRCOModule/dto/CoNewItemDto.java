package eci.server.CRCOModule.dto;

import eci.server.CRCOModule.dto.featuresdtos.ChangedFeatureDto;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.ItemClassificationDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoNewItemDto {

    private String itemNumber;
    private String itemName;

    private Long itemId;

    private char itemRevision;
    //private String itemFamily; //classification

    private ItemClassificationDto itemClassification;
    private String itemStatus;

    private String changedContent;

    private ChangedFeatureDto changedFeature;


    public static CoNewItemDto toDto(
            CoNewItem coItem,
            RouteOrderingRepository routeOrderingRepository) {

        return new CoNewItemDto(
                coItem.getNewItem().getItemNumber(),
                coItem.getNewItem().getName(),

                coItem.getNewItem().getId(),

                (char)(coItem.getNewItem().getRevision()),

                new ItemClassificationDto(coItem.getNewItem().getClassification().getClassification1().getName()+"/"
                        +coItem.getNewItem().getClassification().getClassification2().getName()+"/"
                        +coItem.getNewItem().getClassification().getClassification3().getName()),

                routeOrderingRepository.findByNewItem(coItem.getNewItem()).get(
                        routeOrderingRepository.findByNewItem(coItem.getNewItem()).size()-1
                ).getLifecycleStatus(),

                coItem.getChangedContent(),

                ChangedFeatureDto.toDto(coItem.getChangedFeature())

        );
    }


    public static List<CoNewItemDto> toDtoList(
            List<CoNewItem> coItems,
            RouteOrderingRepository routeOrderingRepository) {

        List<CoNewItemDto> coNewItemDtoList=
        coItems.stream().map(
                coItem ->
        new CoNewItemDto(
                coItem.getNewItem().getItemNumber(),
                coItem.getNewItem().getName(),

                coItem.getNewItem().getId(),

                (char)(coItem.getNewItem().getRevision()),

                new ItemClassificationDto(coItem.getNewItem().getClassification().getClassification1().getName()+"/"
                        +coItem.getNewItem().getClassification().getClassification2().getName()+"/"
                        +coItem.getNewItem().getClassification().getClassification3().getName()),

                routeOrderingRepository.findByNewItem(coItem.getNewItem()).get(
                        routeOrderingRepository.findByNewItem(coItem.getNewItem()).size()-1
                ).getLifecycleStatus(),

                coItem.getChangedContent(),

                ChangedFeatureDto.toDto(coItem.getChangedFeature())

        )
        ).collect(Collectors.toList());

        return coNewItemDtoList;
    }

}