package com.parkee.parkee.exceptions;

import com.parkee.parkee.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.UnknownHostException;

@ControllerAdvice
@Slf4j
public class GlobalExceptions {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<String>> handleAllExceptions(Exception ex) {

        log.error(ex.getMessage(), ex);

        if (ex.getCause() instanceof UnknownHostException) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(),
                    ex.getLocalizedMessage());
        }

        return Response.failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "We are unable to process your request at this time, please try again later.", ex.getMessage());
    }
    @ExceptionHandler(DataConstraintException.class)
    public ResponseEntity<Response<Object>> handleDataNotFoundException(DataConstraintException ex) {
        return Response.failedResponse(ex.getHttpStatus().value(), ex.getMessage());
    }
}
