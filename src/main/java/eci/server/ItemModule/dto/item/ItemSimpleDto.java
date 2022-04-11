package eci.server.ItemModule.dto.item;


import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.entity.material.ItemMaterial;
import eci.server.ItemModule.entity.material.Material;
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
    private Integer itemNumber;

    private String width;
    private String height;

    //private List<ItemMaterial> itemMaterialList;
    private String material;
    private String weight;
    private String color;

    private String thumbnailaddress;
    //location + filePath.substring(0, 10) + uniquename

    private String attachmentaddress;

    private String username;
//

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

//    private String thumbnailadress;

//    private String lifecyclePhase;
//
//    private String workflowPhase;
//
//    private List<String> files;

}