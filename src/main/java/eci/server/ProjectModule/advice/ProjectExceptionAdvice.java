package eci.server.ProjectModule.advice;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {

    /**
     * 진짜 저장 시, 양산 개발 필수 속성 값이 blank 인 경우 던지는 에러
     * @return
     */
    @ExceptionHandler(MassProductionSaveException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response massProductionSaveException() {
        return Response.failure(400, "양산 개발 필수 속성이 채워지지 않았습니다.");
    }

    /**
     * 채워지지 않은 항목 존재 (저장 시)
     * @return
     */
    @ExceptionHandler(ProjectCreateNotEmptyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response ProjectCreateNotEmptyException() {

        return Response.failure(400, "채워지지 않은 항목이 있습니다. 유효한 값을 넣어주세요");
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response projectNotFoundException() {
        return Response.failure(404, "해당하는 프로젝트가 존재하지 않습니다");
    }

    @ExceptionHandler(ProjectTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response projectTypeNotFoundException() {
        return Response.failure(400, "해당하는 프로젝트 타입이 없습니다.");
    }

    @ExceptionHandler(ProjectLevelNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response projectLevelNotFoundException() {
        return Response.failure(400, "해당하는 프로젝트 레벨이 없습니다.");
    }

    @ExceptionHandler(ProduceOrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response produceOrganizationNotFoundException() {
        return Response.failure(400, "해당하는 생산조직이 없습니다.");
    }

    @ExceptionHandler(ClientOrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response clientOrganizationNotFoundException() {
        return Response.failure(400, "해당하는 고객사가 없습니다.");
    }

    @ExceptionHandler(ProjectUpdateImpossibleException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response projectUpdateImpossibleException() {
        return Response.failure(400, "이미 저장된 프로젝트는 수정할 수 없습니다.");
    }



}
