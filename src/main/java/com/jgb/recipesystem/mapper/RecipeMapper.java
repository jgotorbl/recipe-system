package com.jgb.recipesystem.mapper;

import com.jgb.recipesystem.database.entity.Recipe;
import com.jgb.recipesystem.model.RecipeDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RecipeMapper
 * <br>
 * <code>com.jgb.recipesystem.mapper.RecipeMapper</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 08 March 2022
 */
@Mapper(imports = LocalDateTime.class)
public interface RecipeMapper {

    RecipeMapper MAPPER = Mappers.getMapper(RecipeMapper.class);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "vegetarian", source = "vegetarian"),
            @Mapping(target = "servings", source = "servings"),
            @Mapping(target = "ingredients", source = "ingredients"),
            @Mapping(target = "cookingInstructions", source = "cookingInstructions"),
            @Mapping(target = "creationDateTime", ignore = true)
    })
    Recipe toRecipeEntity(RecipeDTO recipeDTO);

    @Mappings({
            @Mapping(target = "creationDateTime", source = "creationDateTime", qualifiedByName = "mapRecipeCreationDateTime")
    })
    RecipeDTO toRecipeDTO(Recipe recipe);

    @Named("mapRecipeCreationDateTime")
    default String mapRecipeCreationDateTime(Timestamp creationDateTime) {
        LocalDateTime recipeCreation = creationDateTime.toLocalDateTime();
        return recipeCreation.format(DateTimeFormatter.ofPattern("dd‐MM‐yyyy HH:mm"));
    }

}
