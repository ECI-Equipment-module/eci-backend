package eci.server.NewItemModule.dto.newItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.classification.ClassificationDto;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class NewItemPagingDto {

    private Long id;
    private List<NewItemImageDto> thumbnail;
    private String itemNumber;
    private String name;
    private ItemTypesDto type;
    private ClassificationDto classification;
    private boolean sharing;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;


    public static NewItemPagingDto toDto(
            NewItem newItem) {

        return new NewItemPagingDto(

                newItem.getId(),

                newItem.getThumbnail().
                        stream().
                        map(NewItemImageDto::toDto).collect(toList()),

                newItem.getItemNumber(),

                newItem.getName(),

                ItemTypesDto.toDto(
                        newItem.getItemTypes()
                ),

                ClassificationDto.toDto(newItem.getClassification())
                ,

                newItem.isSharing(),

                newItem.getCreatedAt()
        );

    }
}