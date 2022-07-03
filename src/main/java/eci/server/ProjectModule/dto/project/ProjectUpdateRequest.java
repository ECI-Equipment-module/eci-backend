package eci.server.ProjectModule.dto.project;

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
public class ProjectUpdateRequest {
    //애초에 한번 임시저장하고 만든거면 NULL 값이 없긴 하겠지
    //근데 변심해서 이름 안쓰고 저장할 수도 있잖아?
    //NULL 이라면 기존 값을 그대로 쓸 수 있도록 해야지

    private String name;

    private Long projectTypeId;

    private String clientItemNumber;


    private String protoStartPeriod;

    private String protoOverPeriod;

    private String p1StartPeriod;

    private String p1OverPeriod;

    private String p2StartPeriod;

    private String p2OverPeriod;

    private String sopStartPeriod;

    private String sopOverPeriod;


    // 로그인 된 멤버 자동 주입
    @Null
    private Long memberId;

    @Null
    private Long modifierId; //05-22

    private Long itemId;

    private Long projectLevelId;

    private Long clientOrganizationId; //양산 아니면 없어도 됨

    private Long supplierId;

    private Long carType; //양산 아니면 없어도 됨

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