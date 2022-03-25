package eci.server.factory.item;

import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.entity.item.ItemType;
import eci.server.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

import static eci.server.factory.member.MemberFactory.createMember;

public class ItemFactory {
    public static Item createItem() {
        return createItem(createMember());
    }

    public static Item createItem(Member member) {
        return new Item(
                "title",
                "BEARING",
                ItemType.BEARING.label(),
                123L,
                12L,
                13L,
                member,
                new ArrayList<>());
    }

    public static Item createItemWithImages(Member member, List<Image> images) {
        return new Item(
                "title",
                "BEARING",
                ItemType.BEARING.label(),
                123L,
                12L,
                13L,
                member,
                images);
    }

    public static Item createItemWithImages(List<Image> images) {
        return new Item(
                "title",
                "BEARING",
                ItemType.BEARING.label(),
                123L,
                12L,
                13L,
                createMember(),
                images
        );
    }
}