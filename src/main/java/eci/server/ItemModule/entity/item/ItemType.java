package eci.server.ItemModule.entity.item;


public enum ItemType {

    NONE(0),
    자가개발(0), //자가개발
    원재료(1), //원재료
    단순외주구매품(2), // 외주구매품(단순)
    시방외주구매품(3), //외주구매품(시방)
    사내가공품(3), // (외주구매품 시방이랑 THE SAME)
    제품(4),//제품
    기타(0), //기타 - 엮일 것 없삼
    부자재(1) //부자재
    ;

    private final Integer label;

    ItemType(Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }
}

