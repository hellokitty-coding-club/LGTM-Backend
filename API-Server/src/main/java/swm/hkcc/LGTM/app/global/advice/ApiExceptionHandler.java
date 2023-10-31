package swm.hkcc.LGTM.app.global.advice;

import io.sentry.Sentry;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.dto.ApiResponse;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

import java.util.HashSet;
import java.util.Set;

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
        StringBuilder sb = new StringBuilder();

        // field errors info
        Set<FieldError> fieldErrors = new HashSet<>(e.getBindingResult().getFieldErrors());
        fieldErrors
                .forEach(error -> {
                    sb.append(error.getDefaultMessage()).append("\n");
                });
        // other errors info
        e.getBindingResult().getAllErrors()
                .stream()
                .filter(error -> !fieldErrors.contains(error))
                .forEach(error -> {
                    sb.append(error.getDefaultMessage()).append("\n");
                });

        GeneralException ge = new GeneralException(ResponseCode.VALIDATION_ERROR, sb.toString(), e.getCause());
        return handleExceptionInternal(ge, ge.getResponseCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        GeneralException ge = new GeneralException(ResponseCode.VALIDATION_ERROR, ex.getMessage(), ex.getCause());
        return handleExceptionInternal(ge, ge.getResponseCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        GeneralException ge = new GeneralException(ResponseCode.VALIDATION_ERROR, ex.getMessage(), ex.getCause());
        return handleExceptionInternal(ge, ge.getResponseCode(), request);
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
