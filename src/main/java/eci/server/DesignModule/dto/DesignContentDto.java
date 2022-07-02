package eci.server.DesignModule.dto;

import eci.server.NewItemModule.entity.NewItem;
import eci.server.Socket.dto.design.DesignSocketDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
public class DesignContentDto {

    private boolean match;
    private String id;
    private String name;
    private String cardNumber;
    private int quantity;
    private String designData;
    private String designDataName;
    private String topAssyDrawing;
    private String topAssyDrawingName;
    private String dwg;
    private String dwgName;
    private String step;
    private String stepName;
    private String pdf;
    private String pdfName;
    List<DesignContentDto> children;

    public DesignContentDto(){
        super();
    }

    public static DesignContentDto toDto(
            NewItem newItem
    ){
        return new DesignContentDto(

                true,
                newItem.getId().toString(),
                newItem.getName(),
                newItem.getItemNumber(),
                3, //0703 : CAD 수정 필요
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "PF-I-123456.catpart",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "PF-I-123456.catdrawing",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "PF-I-123456.dwg",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "PF-I-123456.stp",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "PF-I-123456.pdf",
                new ArrayList<>()

        );
    }
}
