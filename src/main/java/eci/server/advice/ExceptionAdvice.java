package eci.server.advice;

import eci.server.dto.response.Response;
import eci.server.exception.member.auth.AccessDeniedException;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.exception.member.sign.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) { // 2
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() { // 3
        return Response.failure(401, "로그인에 실패하였습니다.");
    }
    @ExceptionHandler(PasswordNotValidateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response passwordNotValidateException() { // 3
        return Response.failure(401, "로그인에 실패하였습니다.");
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) { // 4
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() { // 6
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() { // 7
        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) { // 1
        log.info("e = {}", e.getMessage());
        return Response.failure(500, "오류가 발생하였습니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint() {
        return Response.failure(401, "인증되지 않은 사용자입니다.");
    }

    /**
     * 접근 권한 없음
     * @return 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return Response.failure(403, "접근이 거부되었습니다.");
    }

    /**
     * 헤더 누락 시 에러
     * @param e
     * @return 400
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response missingRequestHeaderException(MissingRequestHeaderException e) {
        return Response.failure(400, e.getHeaderName() + " 요청 헤더가 누락되었습니다.");
    }
}