package eci.server.ItemModule.service.file;

import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 & 삭제 수행 인터페이스
 */

// 실제 파일 업로드, 삭제 로직 작동시킬 구현체 필요
@Primary //얘를 service 단에서 주입시키면 (required args) ,  구현체들 너무 많다고 어떤 구현체 주입시킬지
//모른다고 에러가 난다 따라서 얘를 primary로
public interface FileService {

    void upload(MultipartFile file, String filename);
    void delete(String filename);

}
