package eci.server.ProjectModule.advice;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.exception.MassProductionSaveException;
import eci.server.ProjectModule.exception.ProjectCreateNotEmptyException;
import eci.server.ProjectModule.exception.ProjectTypeNotFoundException;
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
    public Response projectCreateNotEmptyException() {

        return Response.failure(400, "채워지지 않은 항목이 있습니다. 유효한 값을 넣어주세요");
    }

    @ExceptionHandler(ProjectTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response projectTypeNotFoundException() {
        return Response.failure(400, "해당하는 프로젝트 타입이 없습니다.");
    }


}
