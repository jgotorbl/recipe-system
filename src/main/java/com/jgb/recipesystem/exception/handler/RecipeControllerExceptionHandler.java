package com.jgb.recipesystem.exception.handler;

import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * RecipeControllerExceptionHandler
 * <br>
 * <code>com.jgb.recipesystem.exception.handler.RecipeControllerExceptionHandler</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 08 March 2022
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackageClasses = com.jgb.recipesystem.controller.RecipeController.class)
public class RecipeControllerExceptionHandler {

    /**
     * Handles exception coming from input validation
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<ErrorMessage> errorMessageList = errors.stream().map(this::mapToErrorMessage).toList();
        return ResponseEntity.badRequest().body(errorMessageList);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ErrorMessage> errorMessageList = exception.getConstraintViolations().stream().map(e ->
                ErrorMessage.builder().fieldName("recipeName").rejectedValue(e.getInvalidValue())
                        .message(e.getMessage()).build()).toList();
        return ResponseEntity.badRequest().body(errorMessageList);
    }

    /**
     * Handles DuplicateEntryException and RecipeNotFoundException exceptions
     * @param e exception thrown
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(value = {
            DuplicateEntryException.class,
            RecipeNotFoundException.class
    })
    public ResponseEntity<Object> customException(Exception e) {
        return ResponseEntity.badRequest().body(ErrorMessage.builder().message(e.getMessage()).build());
    }

    private ErrorMessage mapToErrorMessage(FieldError e) {
        return ErrorMessage.builder().code(e.getCode()).message(e.getDefaultMessage())
                .rejectedValue(e.getRejectedValue()).fieldName(e.getField()).build();
    }

}
