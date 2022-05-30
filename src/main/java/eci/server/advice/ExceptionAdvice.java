package eci.server.advice;

import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.exception.DesignNotLinkedException;
import eci.server.DesignModule.exception.DesignUpdateImpossibleException;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.exception.file.FileUploadFailureException;
import eci.server.ItemModule.exception.item.ItemCreateNotEmptyException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.item.ItemTypeSaveException;
import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
import eci.server.ItemModule.exception.member.MemberOverAssignedException;
import eci.server.ItemModule.exception.member.auth.*;
import eci.server.ItemModule.exception.member.auth.AccessDeniedException;
import eci.server.ItemModule.exception.member.auth.AccessExpiredException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.exception.member.sign.*;
import eci.server.ItemModule.exception.route.MemberNotAssignedException;
import eci.server.ItemModule.exception.route.RejectImpossibleException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;
import eci.server.ProjectModule.exception.CarTypeNotFoundException;
import eci.server.ProjectModule.exception.IdNotAppropriateException;
import eci.server.ProjectModule.exception.ProjectNotLinkedException;
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

    @ExceptionHandler(ItemCreateNotEmptyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response itemCreateNotEmptyException() {
        //채워지지 않은 항목 존재 (저장 시에)
        return Response.failure(400, "채워지지 않은 항목이 있습니다. 유효한 값을 넣어주세요");
    }

    @ExceptionHandler(ItemUpdateImpossibleException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response itemUpdateImpossibleException() {
        //회원가입 할 때 비밀번호 체크 부분
        return Response.failure(403, "임시저장이 아닌 아이템은 편집할 수 없습니다.");
    }

    @ExceptionHandler(ItemTypeSaveException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response itemTypeSaveException() {
        //회원가입 할 때 비밀번호 체크 부분
        return Response.failure(403, "유효한 아이템 타입을 정해주세요.");
    }

    @ExceptionHandler(PasswordNotSameException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response passwordNotSameException() {
        //회원가입 할 때 비밀번호 체크 부분
        return Response.failure(401, "비밀번호가 같지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return Response.failure(400, e.getMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response failureException() {
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
    @ExceptionHandler(RefreshExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response refreshExpiredException() {
        return Response.failure(401, "리프레쉬 재발급이 필요합니다.");
    }

    /**
     * 로그인 안됐을 시 에러
     * @return
     */
    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint() {
        return Response.failure(401, "리프레시가 필요합니다.");
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

    @ExceptionHandler(UpdateImpossibleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response updateImpossibleException(UpdateImpossibleException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "이미 승인 과정이 만료됐습니다.");
    }

    @ExceptionHandler(RejectImpossibleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response rejectImpossibleException(RejectImpossibleException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "reject가 불가한 항목입니다.");
    }

    @ExceptionHandler(MemberNotAssignedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotAssignedException(MemberNotAssignedException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "담당자가 배정받지 못한 항목이 있습니다.");
    }

    @ExceptionHandler(MemberOverAssignedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberOverAssignedException(MemberOverAssignedException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "담당자가 과도하게 배정되었습니다.");
    }

//    @ExceptionHandler(MemberNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Response memberNotFoundException(MemberNotFoundException e) {
//        log.info("e = {}", e.getMessage());
//        return Response.failure(404, "존재하지 않는 멤버입니다.");
//    }

    @ExceptionHandler(ProjectNotLinkedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ProjectNotLinkedException(ProjectNotLinkedException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "아이템과 링크된 프로젝트가 존재하지 않습니다.");
    }

    @ExceptionHandler(CarTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response CarTypeNotFoundException(CarTypeNotFoundException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "차종이 존재하지 않습니다.");
    }

    @ExceptionHandler(IdNotAppropriateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response IdNotAppropriateException(IdNotAppropriateException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "올바른 아이디 값이 아닙니다.");
    }

    @ExceptionHandler(DesignNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response DesignNotFoundException(DesignNotFoundException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(404, "해당하는 디자인이 없습니다.");
    }

    @ExceptionHandler(DesignUpdateImpossibleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response DesignUpdateImpossibleException(DesignUpdateImpossibleException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(404, "디자인을 수정할 수 없습니다.");
    }

    @ExceptionHandler(DesignNotLinkedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response DesignNotLinkedException(DesignNotLinkedException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "아이템과 링크된 디자인이 존재하지 않습니다.");
    }

    @ExceptionHandler(RouteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response RouteNotFoundException(RouteNotFoundException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "존재하지 않는 라우트입니다.");
    }


    @ExceptionHandler(ClassificationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ClassificationNotFoundException(ClassificationNotFoundException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(400, "존재하지 않는 분류입니다.");
    }

}