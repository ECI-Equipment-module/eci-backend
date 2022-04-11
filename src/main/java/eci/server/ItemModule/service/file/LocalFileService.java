package eci.server.ItemModule.service.file;

import eci.server.ItemModule.exception.file.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            LocalDateTime now = LocalDateTime.now();
            //날짜별로 파일을 나누기 위해 시간 받아오기
            //now로 디렉토리를 생성하고, 거기에다 파일 저장할 것
            String targetDir = Path.of(
                    location,
                    now.format(DateTimeFormatter.ISO_DATE)
            ).toString();
            //현재 시간을 어떤 형식으로 나타내줄지 정하는 것

            File dirNow = new File(targetDir);
            if(!dirNow.exists()) dirNow.mkdir();//디렉토리 없으면 만들기

            file.transferTo(Path.of(
                    targetDir,
                    filename
            ));

        } catch(IOException e) {
            throw new FileUploadFailureException(e);
        }

    }

    @Override
    public void delete(String filename) {
        new File(location+ filename).delete();
    }

    public byte[] getImage(String fileTime, String value) // yyyymmdd_HHmmssZ)
    {
        FileInputStream fis=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String[] fileAr = fileTime.split("_");
        String filePath = fileAr[0];

        System.out.println(
                filePath.substring(0, 10)
                );
//location + filePath.substring(0, 10) + uniquename
        String fileDir = location + filePath.substring(0, 10) + "\\"  + value ; // 파일경로

        try{
            fis = new FileInputStream(fileDir);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }
}