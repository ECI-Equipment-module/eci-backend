package eci.server.ItemModule.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import eci.server.ItemModule.exception.file.FileUploadFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AWSFileService implements FileService{
    private final AmazonS3 amazonS3;
    /**
     * 파일 업로드 위치
     */
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드 디렉토리 생성
     */
    @PostConstruct
    void postConstruct() {

        File dir = new File(bucket);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    public void upload(MultipartFile file, String filename){//(List<MultipartFile> multipartFile) {

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new FileUploadFailureException(e);
            }
    }

    public void delete(String filename) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, filename));
    }

}
