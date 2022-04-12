package eci.server.ItemModule.service.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 & 삭제 수행 인터페이스
 */

// 실제 파일 업로드, 삭제 로직 작동시킬 구현체 필요

public interface FileService {

    void upload(MultipartFile file, String filename);
    void delete(String filename);

}