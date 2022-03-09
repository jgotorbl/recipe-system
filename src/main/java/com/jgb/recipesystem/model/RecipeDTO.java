package com.jgb.recipesystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Recipe
 * <br>
 * <code>com.jgb.recipesystem.model.Recipe</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDTO {

    private String creationDateTime;
    private String name;
    private Boolean vegetarian;
    private Integer servings;
    private List<String> ingredients;
    private String cookingInstructions;
}
