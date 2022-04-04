package eci.server.dto.item;


import eci.server.dto.member.MemberDto;
import eci.server.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String type;
    private Long width;
    private Long height;
    private Long weight;
    private MemberDto member;
    private List<ImageDto> thumbnail;

    public static ItemDto toDto(Item Item) {
        return new ItemDto(
                Item.getId(),
                Item.getName(),
                Item.getType(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getWeight(),
                MemberDto.toDto(Item.getMember()),
                Item.getThumbnail().stream().map(i -> ImageDto.toDto(i)).collect(toList())
        );
    }



}