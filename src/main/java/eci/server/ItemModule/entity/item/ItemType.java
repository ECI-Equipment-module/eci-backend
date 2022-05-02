package eci.server.ItemModule.entity.item;


public enum ItemType {

    NONE(0),
    TYPE0(0), //자가개발
    TYPE1(1),//원재료
    TYPE2(2), // 외주구매품(단순)2
    TYPE3(3),//사내가공품 외주구매품(시방)3
    TYPE4(4)//제품4
    ;

    private final Integer label;

    ItemType(Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }
}

