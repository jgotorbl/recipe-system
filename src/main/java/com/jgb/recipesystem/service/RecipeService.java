package com.jgb.recipesystem.service;

import com.jgb.recipesystem.database.entity.Recipe;
import com.jgb.recipesystem.database.repository.RecipeRepository;
import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import com.jgb.recipesystem.mapper.RecipeMapper;
import com.jgb.recipesystem.model.RecipeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * RecipeService
 * <br>
 * <code>com.jgb.recipesystem.service.RecipeService</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * Handles the logic of creating a new recipe
     *
     * @param recipeDTO recipe object to be created
     * @throws DuplicateEntryException if there is a recipe with the same name in the DB
     */
    public void createRecipe(RecipeDTO recipeDTO) throws DuplicateEntryException {
        Optional<Recipe> existingRecipe = recipeRepository.findOneByName(recipeDTO.getName());
        if (existingRecipe.isPresent()) {
            throw new DuplicateEntryException("A recipe with name " +  recipeDTO.getName() + " already exists.");
        }
        Recipe recipe = RecipeMapper.MAPPER.toRecipeEntity(recipeDTO);
        recipe.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
        recipeRepository.save(recipe);
    }

    /**
     * Retrieves info about a specific recipe
     *
     * @param recipeName name of the recipe to retrieve
     * @return an object containing all the recipe information
     * @throws RecipeNotFoundException if a recipe with the name passed as argument is not found in the database
     */
    public RecipeDTO getRecipe(String recipeName) throws RecipeNotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findOneByName(recipeName);
        return recipeOptional.map(RecipeMapper.MAPPER::toRecipeDTO)
                .orElseThrow(() -> new RecipeNotFoundException("Could not find recipe with name" + recipeName));
    }

    /**
     * Updates a recipe, if a recipe with that name is found in the DB
     *
     * @param recipeDTO object containing information about the new recipe
     * @throws RecipeNotFoundException if a recipe with the name passed in recipeDTO is not found in the database
     */
    public void updateRecipe(RecipeDTO recipeDTO) throws RecipeNotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findOneByName(recipeDTO.getName());
        if (recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException("Could not find recipe with name " + recipeDTO.getName());
        }
        Recipe recipe = RecipeMapper.MAPPER.toRecipeEntity(recipeDTO);
        recipe.setId(recipeOptional.get().getId());
        recipe.setCreationDateTime(recipeOptional.get().getCreationDateTime());
        recipeRepository.save(recipe);

    }

    /**
     * Deletes a recipe
     *
     * @param recipeName name of the recipe to delete
     * @throws RecipeNotFoundException if a recipe with the name passed as argument is not found in the database.
     */
    public void deleteRecipe(String recipeName) throws RecipeNotFoundException {
        Optional<Recipe> recipeToDelete = recipeRepository.findOneByName(recipeName);
        if (recipeToDelete.isEmpty()) {
            throw new RecipeNotFoundException("Could not find recipe with name " + recipeName);
        }
        recipeRepository.deleteByName(recipeName);
    }

}
