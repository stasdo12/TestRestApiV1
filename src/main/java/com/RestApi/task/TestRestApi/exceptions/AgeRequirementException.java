package com.RestApi.task.TestRestApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgeRequirementException extends RuntimeException{
    public AgeRequirementException(String message) {
        super(message);
    }
}
