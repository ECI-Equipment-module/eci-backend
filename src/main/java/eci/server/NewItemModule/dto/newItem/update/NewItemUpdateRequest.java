
package eci.server.NewItemModule.dto.newItem.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemUpdateRequest {

    private Long classification1Id;

    private Long classification2Id;

    private Long classification3Id;

    private Long typeId;
    //ItemTypes 으로 변환하기

    @Null
    private String itemNumber;
    private String name;
    private boolean sharing;

    private Long carTypeId;

    // 맞춤 속성 ) boolean 빼고 다 만듦
    // integrate; curve;  forming;

    private String forming; //ChoiceField 5,6

    private String curve; //ChoiceField 3(평면),4 (곡면)

    private String integrate;

    private String width;

    private String height;

    private String thickness;

    private String weight;

    private String importance;

    private Long colorId;

    private String loadQuantity;

    private Long coatingWayId;

    private Long coatingTypeId;

    private String modulus;

    private String screw;

    private String cuttingType;

    private String lcd;

    private String displaySize;

    private String screwHeight;

    private Long clientOrganizationId;

    private Long supplierOrganizationId;


    private List<MultipartFile> thumbnail = new ArrayList<>();

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    private List<Long> makersId = new ArrayList<>();

    private List<String> partnumbers = new ArrayList<>();

    @Null
    private Long memberId;
}

