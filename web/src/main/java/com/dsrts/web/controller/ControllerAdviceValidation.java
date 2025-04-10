package com.dsrts.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ControllerAdviceValidation {
    /**
     * jpa validations will throw constraint violations for failed validations.
     * by default, spring-data-rest throws a 500 when this happens.
     * but this feels like a "bad request", so returning bad request here
     *
     * @param request
     * @param exception
     * @throws IOException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolation(ServletWebRequest request, ConstraintViolationException exception) throws IOException {
        log.error("handleConstraintViolation()",exception);
        request.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST, exception.toString());

    }
}
