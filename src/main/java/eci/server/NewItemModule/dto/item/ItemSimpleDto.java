package eci.server.NewItemModule.dto.item;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSimpleDto {
    private Long id;
    private String name;
    private String type;
    private String itemNumber;

    private String width;
    private String height;
    private String weight;

    private String username;
    private String materialName;
//    private String materialName;

    private String colorName;
    private String imageAdress;

    private List<String> attachmentName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
//    private List<ImageDto> thumbnail;


}