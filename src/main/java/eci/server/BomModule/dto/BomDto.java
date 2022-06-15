package eci.server.BomModule.dto;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.repository.CompareBomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DesignModule.dto.itemdesign.BomDesignItemDto;
import eci.server.ItemModule.dto.item.ItemDesignDto;
import eci.server.NewItemModule.dto.responsibility.DesignResponsibleDto;
import eci.server.NewItemModule.dto.responsibility.ResponsibleDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.config.guard.BomGuard;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class BomDto {
    ItemDesignDto item;
    List<DesignResponsibleDto> list;


    public static BomDto toDto(
            NewItem newItem,
            Bom bom,
            BomGuard bomGuard,
            PreliminaryBomRepository preliminaryBomRepository,
            DevelopmentBomRepository developmentBomRepository,
            CompareBomRepository compareBomRepository
    ) {

        BomDesignItemDto bomDesignItemDto = BomDesignItemDto.toBomDto(
                newItem,
                bom,
                bomGuard,
                preliminaryBomRepository,
                developmentBomRepository,
                compareBomRepository
        );

        List<DesignResponsibleDto> responsibleDtoList = bomDesignItemDto.getResponsibleList();

        return new BomDto(
                ItemDesignDto.toDto(newItem),
                responsibleDtoList
        );
    }

}
