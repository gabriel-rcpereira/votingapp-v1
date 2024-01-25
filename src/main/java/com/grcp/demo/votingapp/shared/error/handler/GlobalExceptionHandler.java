package com.grcp.demo.votingapp.shared.error.handler;

import com.grcp.demo.votingapp.shared.error.exception.BaseException;
import com.grcp.demo.votingapp.shared.error.exception.BusinessException;
import com.grcp.demo.votingapp.shared.error.exception.EntityNotFoundException;
import com.grcp.demo.votingapp.shared.error.handler.model.ApplicationErrorResponse;
import com.grcp.demo.votingapp.shared.error.handler.model.DetailedErrorResponse;
import com.grcp.demo.votingapp.shared.service.MessageSourceAdapter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSourceAdapter messageSourceAdapter;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApplicationErrorResponse> handler(BusinessException ex) {
        ApplicationErrorResponse body = toApplicationErrorResponse(ex);
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApplicationErrorResponse> handler(EntityNotFoundException ex) {
        ApplicationErrorResponse body = toApplicationErrorResponse(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationErrorResponse> handler(ConstraintViolationException ex) {
        List<DetailedErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(this::toDetailedErrorResponse)
                .toList();
        ApplicationErrorResponse body = new ApplicationErrorResponse(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ApplicationErrorResponse toApplicationErrorResponse(BaseException ex) {
        String message = messageSourceAdapter.getMessage(ex.code(), ex.args());
        List<DetailedErrorResponse> errors = List.of(new DetailedErrorResponse(ex.code(), message));
        return new ApplicationErrorResponse(errors);
    }

    private DetailedErrorResponse toDetailedErrorResponse(ConstraintViolation<?> it) {
        String message = messageSourceAdapter.getMessage(it.getMessage());
        return new DetailedErrorResponse(it.getMessage(), message);
    }
}
