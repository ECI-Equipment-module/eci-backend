package eci.server.NewItemModule.dto.newItem;

import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.NewItemModule.dto.attachment.NewItemAttachmentDto;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class NewItemDetailDto {
    private boolean tempsave;

    private Long id;
    private String name;
    private String type;
    private String itemNumber;
    private String width;
    private String height;
    private String weight;
    private MemberDto member;
    private List<NewItemImageDto> thumbnail;
    private List<NewItemAttachmentDto> attachments;
    private ColorDto color;

    private List<MakerSimpleDto> makerSimpleDtos;

    private char revision;

    public static NewItemDetailDto toDto(NewItem Item) {

        return new NewItemDetailDto(
                Item.getTempsave(),

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getWeight(),
                MemberDto.toDto(Item.getMember()),

                Item.getThumbnail().
                        stream().
                        map(i -> NewItemImageDto.toDto(i)).collect(toList()),

                Item.getAttachments().
                        stream().
                        map(i -> NewItemAttachmentDto.toDto(i))
                        .collect(toList()),

                ColorDto.toDto(Item.getColor()),


                Item.getMakers().
                        stream().
                        map(i -> MakerSimpleDto.toDto(
                                i.getMaker())
                        ).collect(toList()),

                (char) Item.getRevision()



        );
    }

}