package com.jgb.recipesystem.validation;

import com.jgb.recipesystem.model.RecipeDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import javax.management.loading.MLetContent;
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
        if (errors instanceof AbstractBindingResult) {
            AbstractBindingResult bindingResult = (AbstractBindingResult) errors;
            if (RecipeDTO.class.isAssignableFrom(target.getClass())) {
                validateRecipe((RecipeDTO) target, bindingResult);
            }
        }
    }

    private void validateRecipe(RecipeDTO recipe, AbstractBindingResult bindingResult) {
        validateString(recipe.getName(), "name", bindingResult);
        validateBoolean(recipe.getVegetarian(), "vegetarian", bindingResult);
        validateInteger(recipe.getServings(), "servings", bindingResult);
        validateIngredientList(recipe.getIngredients(), "ingredients", bindingResult);
        validateString(recipe.getCookingInstructions(), "cookingInstructions", bindingResult);
    }

    private void validateIngredientList(List<String> value, String fieldName, AbstractBindingResult bindingResult) {
        if (value ==null) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_NULL.getCode()));
        } else if (value.isEmpty()) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_EMPTY.getCode()));
        } else if (value.stream().anyMatch(i -> Objects.isNull(i) || i.isBlank())) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.INVALID_LIST_VALUE.getCode()));
        }
    }

    private void validateInteger(Integer integer, String fieldName, AbstractBindingResult bindingResult) {
        if (integer == null) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_NULL.getCode()));
        } else if (integer <= 0) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.GT_ZERO.getCode()));
        }
    }

    private void validateBoolean(Boolean value, String fieldName, AbstractBindingResult bindingResult) {
        if (value == null) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_NULL.getCode()));
        }
    }

    private void validateString(String value, String fieldName, AbstractBindingResult bindingResult) {
        if (value ==null) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_NULL.getCode()));
        } else if (value.isBlank()) {
            bindingResult.addError(new FieldError(OBJECT_NAME, fieldName, ValidationCodes.NOT_EMPTY.getCode()));
        }
    }
}

@Getter
@ToString
@RequiredArgsConstructor
enum ValidationCodes {

    NOT_NULL("Value may not be null."),
    GT_ZERO("Please specify a value greater than zero."),
    NOT_EMPTY("Value may not be empty."),
    INVALID_LIST_VALUE("One or more values in the list are null or empty");

    private final String code;
}
