package swm.hkcc.LGTM.app.global.advice;

import io.sentry.Sentry;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.dto.ApiResponse;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        Sentry.captureException(e);
        return handleExceptionInternal(e, ResponseCode.VALIDATION_ERROR, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        Sentry.captureException(e);
        return handleExceptionInternal(e, e.getResponseCode(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        Sentry.captureException(e);
        return handleExceptionInternal(e, ResponseCode.INTERNAL_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Sentry.captureException(e);
        return handleExceptionInternal(
                e,
                ResponseCode.INTERNAL_ERROR,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ResponseCode responseCode, WebRequest request) {
        log.info("exception occurred from {} class : {}", e.getClass().getName(), e.getMessage());
        return handleExceptionInternal(e, responseCode, HttpHeaders.EMPTY, responseCode.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ResponseCode responseCode, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("exception occurred from {} class : {}", e.getClass().getName(), e.getMessage());
        return super.handleExceptionInternal(
                e,
                ApiResponse.of(false, responseCode.getCode(), responseCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}
