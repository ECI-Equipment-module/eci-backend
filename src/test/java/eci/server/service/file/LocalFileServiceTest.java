package eci.server.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService();

    /**
     * 파일 업로드용 디렉토리 지정
     */
    String testLocation = new File("src/test/resources/static").getAbsolutePath() + "/";

    /**
     * ReflectionTestUtils 사용해
     * LocalFileService에 location 주입
     * @throws IOException
     */
    @BeforeEach
    void beforeEach() throws IOException {
        ReflectionTestUtils.setField(localFileService, "location", testLocation);
        FileUtils.cleanDirectory(new File(testLocation));
    }

    /**
     * MockMultipartFile을 생성 & 업로드 여부 체크
     */
    @Test
    void uploadTest() { // 3
        // given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "testFile.txt";

        // when
        localFileService.upload(file, filename);

        // then
        assertThat(isExists(testLocation + filename)).isTrue();
    }

    boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}