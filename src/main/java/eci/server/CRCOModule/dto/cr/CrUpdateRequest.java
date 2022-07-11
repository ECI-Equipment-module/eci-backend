package eci.server.CRCOModule.dto.cr;

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
public class CrUpdateRequest {

    private String crNumber;

    private Long crReasonId;

    @Nullable
    private String crReason;

    private Long crImportanceId;

    private Long crSourceId;

    private String name;

    private String content;

    private String solution;

    private Long itemId;

    @Null
    private Long memberId;

    @Null
    private Long modifierId; //05-22

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
