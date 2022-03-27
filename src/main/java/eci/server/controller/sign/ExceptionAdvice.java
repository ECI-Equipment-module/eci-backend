package eci.server.controller.sign;

import eci.server.dto.response.Response;
import eci.server.exception.member.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

//    @ExceptionHandler(RoleNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Response roleNotFoundException() { // 7
//        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Response exception(Exception e) { // 1
//        log.info("e = {}", e.getMessage());
//        return Response.failure(500, "오류가 발생하였습니다.");
    }

//}