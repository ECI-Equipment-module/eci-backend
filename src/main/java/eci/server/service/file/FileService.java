package eci.server.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드와 삭제를 수행 인터페이스
 */
public interface FileService {
    void upload(MultipartFile file, String filename);
    void delete(String filename);
}