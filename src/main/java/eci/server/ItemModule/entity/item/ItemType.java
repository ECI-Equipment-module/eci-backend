package eci.server.ItemModule.entity.item;


public enum ItemType {

    NONE(0),
    TYPE0(0), //자가개발
    TYPE1(1), //원재료
    TYPE2(2), // 외주구매품(단순)
    TYPE3(3), //외주구매품(시방)
    TYPE4(3), //사내가공품 (외주구매품 시방이랑 THE SAME)
    TYPE5(4),//제품
    TYPE6(0) //기타 - 엮일 것 없삼
    ;

    private final Integer label;

    ItemType(Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }
}

