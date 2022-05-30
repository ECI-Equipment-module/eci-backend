package eci.server.ItemModule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
//단순 응답 형태만 통일하실꺼면은
// 인터페이스나 추상클래스 하나 만드셔서
// Response로 보낼
// 베이스 클래스 하나 만드시고,
// 그걸 상속 받는 콘크리트 클래스 만드시는게 더
// 수월할 수 있어요.
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