package eci.server.advice;

import eci.server.dto.response.Response;
import eci.server.exception.file.FileUploadFailureException;
import eci.server.exception.item.ItemNotFoundException;
import eci.server.exception.member.auth.AccessDeniedException;
import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.exception.member.sign.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }
    @ExceptionHandler(PasswordNotValidateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response passwordNotValidateException() {
        return Response.failure(401, "비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }

    /**
     * 회원 존재 x
     * @return 404
     */
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }

    /**
     * 가입 시 옳지 않은 등급으로 가입
     * @return 404
     */
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    /**
     * 서버 오류
     * @param e
     * @return 500
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Response exception(Exception e) {
//        log.info("e = {}", e.getMessage());
//        return Response.failure(500, "오류가 발생하였습니다.");
//    }

    /**
     * 액세스 유효하지 않을 때 에러
     * @return
     */
    @ExceptionHandler(AccessExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response accessExpiredException() {
        return Response.failure(401, "리프레시가 필요합니다.");
    }

    /**
     * 리프레시 토큰이 유효하지 않을 때 에러
     * @return
     */
    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint() {
        return Response.failure(401, "리프레쉬 재발급이 필요합니다.");
    }

    /**
     * 접근 권한 없음
     * @return 403
     */
<<<<<<< HEAD
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return Response.failure(403, "접근이 거부되었습니다.");
    }
=======
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public Response accessDeniedException() {
//        return Response.failure(403, "접근이 거부되었습니다.");
//    }
>>>>>>> 2478d557f1572cc48483d4cd4ece8afe736c7b7f

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

<<<<<<< HEAD
//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Response bindException(BindException e) {
//        return Response.failure(400, e.getMessage());//.getFieldError().getDefaultMessage());
//    }
=======
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return Response.failure(400, e.getMessage());//.getFieldError().getDefaultMessage());
    }
>>>>>>> 2478d557f1572cc48483d4cd4ece8afe736c7b7f

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(404, "파일 업로드에 실패하였습니다.");
    }
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response itemNotFoundException(ItemNotFoundException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(404, "존재하지 않는 아이템입니다.");
    }

}