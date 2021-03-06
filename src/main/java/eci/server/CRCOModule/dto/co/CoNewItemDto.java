package eci.server.CRCOModule.dto.co;

import eci.server.CRCOModule.dto.cr.CrReadDto;
import eci.server.CRCOModule.dto.featuresdtos.ChangedFeatureDto;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.ItemClassificationDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.Comparator;
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

    private String crNumber;//CrReadDto crNumber;


    public static CoNewItemDto toDto(
            List<ChangeRequest> CrinCo,
            CoNewItem coItem,
            RouteOrderingRepository routeOrderingRepository) {

        return new CoNewItemDto(
                coItem.getNewItem().getItemNumber(),
                coItem.getNewItem().getName(),

                coItem.getNewItem().getId(),

                (char) (coItem.getNewItem().getRevision()),

                new ItemClassificationDto(coItem.getNewItem().getClassification().getClassification1().getName() + "/"
                        + coItem.getNewItem().getClassification().getClassification2().getName() + "/"
                        + coItem.getNewItem().getClassification().getClassification3().getName()),

                routeOrderingRepository.findByNewItemOrderByIdAsc(coItem.getNewItem()).get(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(coItem.getNewItem()).size() - 1
                ).getLifecycleStatus(),

                coItem.getChangedContent(),

                coItem.getChangedFeature()==null?
                        ChangedFeatureDto.toDto():
                        ChangedFeatureDto.toDto(coItem.getChangedFeature()),

                crNumberCheckAndMake(CrinCo, coItem.getNewItem())

        );
    }


    public static List<CoNewItemDto> toDtoList(
            List<ChangeRequest> CrinCo,
            List<CoNewItem> coItems,
            RouteOrderingRepository routeOrderingRepository) {

        List<CoNewItemDto> coNewItemDtoList =
                coItems.stream().map(
                        coItem ->
                                new CoNewItemDto(
                                        coItem.getNewItem().getItemNumber(),
                                        coItem.getNewItem().getName(),

                                        coItem.getNewItem().getId(),

                                        (char) (coItem.getNewItem().getRevision()),

                                        new ItemClassificationDto(coItem.getNewItem().getClassification().getClassification1().getName() + "/"
                                                + coItem.getNewItem().getClassification().getClassification2().getName() + "/"
                                                + coItem.getNewItem().getClassification().getClassification3().getName()),

                                        routeOrderingRepository.findByNewItemOrderByIdAsc(coItem.getNewItem()).get(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(coItem.getNewItem()).size() - 1
                                        ).getLifecycleStatus(),

                                        coItem.getChangedContent(),

                                        coItem.getChangedFeature()==null?
                                                ChangedFeatureDto.toDto():
                                                ChangedFeatureDto.toDto(coItem.getChangedFeature()),

                                        crNumberCheckAndMake(CrinCo, coItem.getNewItem())

                                )
                ).collect(Collectors.toList());

        return coNewItemDtoList;
    }

    /**
     * ??? newItem ??? cr ??? ?????? ????????? ?????? ?????? && ????????? coNumber return
     *
     * @param crInCo
     * @param newItem
     * @return coNumber
     */
    private static String crNumberCheckAndMake(List<ChangeRequest> crInCo, NewItem newItem) {

        for (ChangeRequest cr : crInCo) {
            if (cr.getNewItem() == newItem) {
                return cr.getCrNumber();
            }
        }
        return " "; // CR ??? ????????? CR NUMBER ????????? ???
    }


    static Comparator<ChangeRequest> crComparator = new Comparator<ChangeRequest>() {
        @Override
        public int compare(ChangeRequest o1, ChangeRequest o2) {
            return (int) (o2.getId() - o1.getId());
        }

    };
}