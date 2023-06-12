package com.kobe2.escrituraauth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CannedStatementException extends RuntimeException {
    public final static String message = "EXCEPTION THROWN";
}
