package eci.server.ItemModule.dto.item;


import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.dto.manufacture.ManufactureSimpleDto;
import eci.server.ItemModule.dto.material.MaterialSimpleDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ItemDto {
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

    private List<MaterialSimpleDto> materialDto;
    private List<ManufactureSimpleDto> manufactureSimpleDtos;

    private char revision;

    public static ItemDto toDto(Item Item) {

        return new ItemDto(
                Item.getTempsave(),

                Item.getId(),
                Item.getName(),
                Item.getType(),
                Item.getItemNumber(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getWeight(),
                MemberDto.toDto(Item.getMember()),

                Item.getThumbnail().
                        stream().
                        map(i -> ImageDto.toDto(i)).collect(toList()),

                Item.getAttachments().
                        stream().
                        map(i -> AttachmentDto.toDto(i))
                        .collect(toList()),

                ColorDto.toDto(Item.getColor()),

                Item.getMaterials().
                        stream().
                        map(i -> MaterialSimpleDto.toDto(
                                i.getMaterial())
                        ).collect(toList()),

                Item.getManufactures().
                        stream().
                        map(i -> ManufactureSimpleDto.toDto(
                                i.getManufacture())
                        ).collect(toList()),

                (char) Item.getRevision()



        );
    }

}