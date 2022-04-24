package eci.server.ItemModule.entity.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public enum ItemType {

    NONE(0),
    ANNULUS_GEAR(1),
    ARM(2),
    ALTERNATOR(3),
    AXEL(1),
    BEARING(2),
    BOLT(3),
    CLUTCH(1),
    COIL_SPRING(2)
    ;

    private final Integer label;

    ItemType(Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }
}

