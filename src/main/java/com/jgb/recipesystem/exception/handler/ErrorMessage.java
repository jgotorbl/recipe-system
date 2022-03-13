package com.jgb.recipesystem.exception.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * ErrorMessage
 * <br>
 * <code>com.jgb.recipesystem.exception.handler.ErrorMessage</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 13 March 2022
 */
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private String code;
    private String fieldName;
    private Object rejectedValue;
    private String message;

}
