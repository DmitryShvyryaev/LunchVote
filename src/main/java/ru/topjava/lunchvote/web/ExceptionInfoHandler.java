package ru.topjava.lunchvote.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.topjava.lunchvote.exception.ErrorInfo;
import ru.topjava.lunchvote.exception.ErrorType;
import ru.topjava.lunchvote.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import static ru.topjava.lunchvote.exception.ErrorType.*;
import static ru.topjava.lunchvote.util.ValidationUtil.getMessage;
import static ru.topjava.lunchvote.util.ValidationUtil.getRootCause;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private final MessageSourceAccessor messageSourceAccessor;

    public ExceptionInfoHandler(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, MethodArgumentNotValidException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, VALIDATION_ERROR, details);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, DATA_NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, APP_ERROR);
    }

    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, ErrorType type, String... details) {
        Throwable rootCause = getRootCause(e);
        log.error(type + " at request " + req.getRequestURL() + rootCause.toString());
        return ResponseEntity.status(type.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), messageSourceAccessor.getMessage(type.getErrorCode()),
                        details.length != 0 ? details : new String[]{getMessage(rootCause)}));
    }
}
