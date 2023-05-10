package com.timolisa.fashionblogapi.exception.advice;

import com.timolisa.fashionblogapi.dto.response.ErrorMessageDTO;
import com.timolisa.fashionblogapi.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseStatus
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidInputsException.class)
    public ResponseEntity<ErrorMessageDTO> invalidInputsException(InvalidInputsException exception) {
        ErrorMessageDTO errorMessage =
                new ErrorMessageDTO(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> postNotFoundException(PostNotFoundException exception) {
        ErrorMessageDTO errorMessage =
                new ErrorMessageDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorMessageDTO> unauthorizedAccessException(UnauthorizedAccessException exception) {
        ErrorMessageDTO errorMessage =
                new ErrorMessageDTO(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorMessageDTO> userDoesNotExistException(UserDoesNotExistException exception) {
        ErrorMessageDTO errorMessage =
                new ErrorMessageDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ErrorMessageDTO> usernameExistsException(UsernameExistsException exception) {
        ErrorMessageDTO errorMessage =
                new ErrorMessageDTO(HttpStatus.ALREADY_REPORTED, exception.getMessage());
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(errorMessage);
    }
    @ExceptionHandler(InvalidTokenRequestException.class)
    public ResponseEntity<ErrorMessageDTO> invalidTokenRequestException(InvalidTokenRequestException exception) {
        ErrorMessageDTO errorMessageDTO =
                new ErrorMessageDTO(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessageDTO);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDTO> constraintViolationException(ConstraintViolationException exception) {
        ErrorMessageDTO errorMessageDTO =
                new ErrorMessageDTO(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessageDTO);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessageDTO> nullPointerException(NullPointerException exception) {
        ErrorMessageDTO errorMessageDTO =
                new ErrorMessageDTO(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessageDTO);
    }
}
