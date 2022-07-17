package eci.server.DocumentModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpdateRequest {

        private Long classification1Id;

        private Long classification2Id;

        private Long tagId;

        private String title;

        @Lob
        private String content;

        @Null
        private Long modifierId;
        /**
         * 추가된 파일을 첨부
         */
        private List<MultipartFile> addedAttachments = new ArrayList<>();

        /**
         * 삭제될 파일 아이디 입력 - is deleted 만 true
         */
        private List<Long> deletedAttachments = new ArrayList<>();

    }
