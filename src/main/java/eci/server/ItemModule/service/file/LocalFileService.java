package eci.server.ItemModule.service.file;


import eci.server.ItemModule.exception.file.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class LocalFileService implements FileService {

    /**
     * 파일 업로드 위치
     */
    @Value("${upload.image.location}")
    private String location;



    /**
     * 파일 업로드 디렉토리 생성
     */
    @PostConstruct
    void postConstruct() {

        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * MultipartFile 을 실제 파일 지정 위치에 저장
     * @param file
     * @param filename
     */
    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            file.transferTo(new File(location + filename));
        } catch(IOException e) {
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(String filename) {
        new File(location+ filename).delete();
    }
}