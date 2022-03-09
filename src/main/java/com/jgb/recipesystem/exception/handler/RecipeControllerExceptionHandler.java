package com.jgb.recipesystem.exception.handler;

import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * RecipeControllerExceptionHandler
 * <br>
 * <code>com.jgb.recipesystem.exception.handler.RecipeControllerExceptionHandler</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 08 March 2022
 */
@ControllerAdvice(basePackageClasses = com.jgb.recipesystem.controller.RecipeController.class)
public class RecipeControllerExceptionHandler {

    /**
     * Handles custom exceptions
     * and DataValidationException thrown during ProductsController calls
     * @param e exception thrown
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(value = {
            DuplicateEntryException.class,
            RecipeNotFoundException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<Object> customException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
