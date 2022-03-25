package eci.server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.image.location}")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                // url에 /image/ 경로 설정 시
                .addResourceLocations("file:" + location)
                // FileSystem의 location 경로에서 파일 접근
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());
                // 업로드된 각 이미지는 고유 이름 & 수정 X => 캐시 설정
                // 자원 접근 시 새로 접근 x , 캐시 자원 이용

    }
}