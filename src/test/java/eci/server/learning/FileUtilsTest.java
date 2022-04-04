package eci.server.learning;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileUtilsTest {

    String testLocation = new File("src/test/resources/static").getAbsolutePath() + "/";


    /**
     * 생성파일이 메소드 의해 제거 여부 체크
     * @throws Exception
     */
    @Test
    void cleanDirectoryTest() throws Exception{
        // given
        String filePath = testLocation + "cleanDirectoryTest.txt";
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        file.transferTo(new File(filePath));
        boolean beforeCleaning = isExists(filePath);

        // when
        FileUtils.cleanDirectory(new File(testLocation));

        // then
        boolean afterCleaning = isExists(filePath);
        assertThat(beforeCleaning).isTrue();
        assertThat(afterCleaning).isFalse();
    }

    boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}