package ru.topjava.lunchvote.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    VALIDATION_ERROR("error.validationError", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_NOT_FOUND("error.dataNotFound", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR("error.dataError", HttpStatus.CONFLICT),
    APP_ERROR("error.appError", HttpStatus.INTERNAL_SERVER_ERROR),
    REPEAT_VOTE_ERROR("error.repeatVote", HttpStatus.CONFLICT);

    private final String errorCode;
    private final HttpStatus status;

    ErrorType(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
