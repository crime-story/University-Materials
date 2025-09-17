package com.unibuc.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() { }
    public ResourceNotFoundException(String msg) { super(msg); }
    public ResourceNotFoundException(String msg, Throwable t) { super(msg, t); }
}