package eci.server.factory.item;


import eci.server.dto.item.ItemReadCondition;

import java.util.List;

public class ItemReadConditionFactory {
    public static ItemReadCondition createItemReadCondition(Integer page, Integer size) {
        return new ItemReadCondition(page, size, List.of());
    }

    public static ItemReadCondition createItemReadCondition(Integer page, Integer size, List<Long> memberIds) {
        return new ItemReadCondition(page, size, memberIds);
    }
}