package com.ausy_technologies.demospring.RestErrorHandling;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        String error = "Argument Not Valid";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        String error = "Missing Path Variable";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String error = "Incorrect values in JSON";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String error = "Wrong Data Type";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
    }


    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException( NoSuchElementException ex, HttpServletResponse httpServletResponse){
        String error = "ID doesn't exist";
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, error, ex));
    }

    

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
