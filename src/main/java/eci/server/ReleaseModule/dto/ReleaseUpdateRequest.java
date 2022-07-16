package eci.server.ReleaseModule.dto;


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
public class ReleaseUpdateRequest {


    private Long releaseType; //{id,name}으로 해야할 듯 합니다. id는 OK

    private Long releaseItemId;

    private Long releaseCoId;

    private String releaseTitle;

    private String releaseContent;

    private List<Long> releaseOrganizationId;
    @Null
    private Long modifierId;
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
