package eci.server.NewItemModule.dto.responsibility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DesignResponsibleDto extends  ResponsibleDto{
    private String color; //gray black none

    public DesignResponsibleDto(Long id, String name, String range) {
        super(id, name, range);
    }

    public DesignResponsibleDto(Long id, String name, String range, String color) {
        super(id, name, range);
        this.color = color;
    }


//    private Long id;
//    private String name;
//    private String range;
//    private String color; //gray black none

}

