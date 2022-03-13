package com.jgb.recipesystem.validation;

import com.jgb.recipesystem.model.RecipeDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;

/**
 * RecipeValidator
 * <br>
 * <code>com.jgb.recipesystem.validation.RecipeValidator</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Component
public class RecipeValidator implements Validator {

    private static final String OBJECT_NAME = RecipeDTO.class.getSimpleName();

    @Override
    public boolean supports(Class<?> clazz) {
        return RecipeDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors instanceof AbstractBindingResult bindingResult) {
            if (RecipeDTO.class.isAssignableFrom(target.getClass())) {
                validateRecipe((RecipeDTO) target, bindingResult);
            }
        }
    }

    private void validateRecipe(RecipeDTO recipe, AbstractBindingResult bindingResult) {
        validateString(recipe.getName(), "name", bindingResult);
        validateBoolean(recipe.getVegetarian(), bindingResult);
        validateInteger(recipe.getServings(), bindingResult);
        validateIngredientList(recipe.getIngredients(), bindingResult);
        validateString(recipe.getCookingInstructions(), "cookingInstructions", bindingResult);
    }

    private void validateIngredientList(List<String> value, AbstractBindingResult bindingResult) {
        if (value ==null) {
            buildErrorCode("ingredients", null, ValidationCodes.NOT_NULL, bindingResult);
        } else if (value.isEmpty()) {
            buildErrorCode("ingredients", value, ValidationCodes.NOT_EMPTY, bindingResult);
        } else if (value.stream().anyMatch(i -> Objects.isNull(i) || i.isBlank())) {
            buildErrorCode("ingredients", value, ValidationCodes.NOT_EMPTY, bindingResult);
        }
    }

    private void validateInteger(Integer integer, AbstractBindingResult bindingResult) {
        if (integer == null) {
            buildErrorCode("servings", integer, ValidationCodes.NOT_NULL, bindingResult);
        } else if (integer <= 0) {
            buildErrorCode("servings", integer, ValidationCodes.GT_ZERO, bindingResult);
        }
    }

    private void validateBoolean(Boolean value, AbstractBindingResult bindingResult) {
        if (value == null) {
            buildErrorCode("vegetarian", null, ValidationCodes.NOT_NULL, bindingResult);
        }
    }

    private void validateString(String value, String fieldName, AbstractBindingResult bindingResult) {
        if (value ==null) {
            buildErrorCode(fieldName, null, ValidationCodes.NOT_NULL, bindingResult);
        } else if (value.isBlank()) {
            buildErrorCode(fieldName, value, ValidationCodes.NOT_EMPTY, bindingResult);
        }
    }

    private void buildErrorCode(String field, Object value, ValidationCodes codes, AbstractBindingResult bindingResult) {
        bindingResult.addError(new FieldError(OBJECT_NAME, field, value, false,
                new String[]{codes.getCode()}, null, codes.getMessage()));
    }
}

@Getter
@ToString
@RequiredArgsConstructor
enum ValidationCodes {

    NOT_NULL("non-null","Value may not be null."),
    GT_ZERO("greater_than_zero","Please specify a value greater than zero."),
    NOT_EMPTY("non_empty", "Value may not be empty."),
    INVALID_LIST_VALUE("invalid_list_value", "One or more values in the list are null or empty");

    private final String code;
    private final String message;
}
