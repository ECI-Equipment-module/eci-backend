package eci.server.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // 1
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 2
@Getter // 3
public class Response {
    /**
     * 일관화된 응답 방식을 위한 클래스
     * 성공여부 - 반환코드 - 결과메시지
     */
    private boolean success;
    private int code;
    private Result result;

    public static Response success() {
        return new Response(true, 0, null);
    }

    public static <T> Response success(T data) {
        return new Response(true, 0, new Success<>(data));
    }

    public static Response failure(int code, String msg) {
        return new Response(false, code, new Failure(msg));
    }

}