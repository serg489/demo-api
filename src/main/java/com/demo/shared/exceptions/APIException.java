package com.demo.shared.exceptions;

public class APIException extends CommonException {

    public APIException(String errorMessage) {
        super(errorMessage);
    }

    public APIException(String errorMessage, Exception ex) {
        super(errorMessage, ex);
    }
}
