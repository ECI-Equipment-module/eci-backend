package eci.server.CRCOModule.dto.co;

import com.sun.istack.Nullable;
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
public class CoUpdateRequest {

    private Long clientOrganizationId;

    private String clientItemNumber;

    private String coNumber;

    private String coPublishPeriod;

    private String coReceivedPeriod;

    private Boolean difference;

    @Nullable
    private Long carTypeId;

    private Boolean costDifferent;

    private String costDifference;

    private Long coReasonId;
    private String coReason;

    private Long coStageId;

    private String applyPeriod;

    private List<Long> coEffectId;

    private Long coImportanceId;

    private String name;

    private String content;

    @Null
    private Long modifierId;


    //선택한 cr
    private List<Long> changeRequestIds = new ArrayList<>();
    //아래 세개가 co-
    private List<Long> changeFeaturesIds = new ArrayList<>();
    private List<String> changeContents = new ArrayList<>();
    private List<Long> newItemsIds = new ArrayList<>();

    /**
     * 추가된 파일을 첨부
     */
    private List<MultipartFile> addedAttachments = new ArrayList<>();
    private List<Long> addedTag = new ArrayList<>();
    private List<String> addedAttachmentComment = new ArrayList<>();

    /**
     * 삭제될 파일 아이디 입력 - is deleted 만 true
     */
    private List<Long> deletedAttachments = new ArrayList<>();

}
