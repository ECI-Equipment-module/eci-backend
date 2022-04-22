package eci.server.ItemModule.entity.item;

public enum ItemType {
    NONE(0),
    ANNULUS_GEAR(100),
    ARM(101),
    ALTERNATOR(102),
    AXEL(103),
    BEARING(104),
    BOLT(105),
    CLUTCH(106),
    COIL_SPRING(107)
    ;

    private final Integer label;

    ItemType(Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }
}

