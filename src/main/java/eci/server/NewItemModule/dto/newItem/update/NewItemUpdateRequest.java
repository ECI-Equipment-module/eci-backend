
package eci.server.NewItemModule.dto.newItem.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
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
    private Boolean sharing;

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

    @Null
    private Long modifierId;

    @Nullable
    private MultipartFile thumbnail;

    /**
     * 추가된 파일을 첨부
     */
    private List<MultipartFile> addedAttachments = new ArrayList<>();
    private List<Long> addedTag = new ArrayList<>();
    private List<String> addedAttachmentComment = new ArrayList<>();

    //새로 thumbnail이 들어오게 된다면 기존 애를 삭제처리 진행하면 됨 (그 아이의 new_item을 null로 처리)
    //private List<Long> deletedImages = new ArrayList<>();

    /**
     * 삭제될 파일 아이디 입력 - is deleted 만 true
     */
    private List<Long> deletedAttachments = new ArrayList<>();

    //private List<Long> makersId = new ArrayList<>();
    private Long makersId ;
    //private List<String> partnumbers = new ArrayList<>();

    private String partnumbers ;

    @Null
    private Long memberId;
}

