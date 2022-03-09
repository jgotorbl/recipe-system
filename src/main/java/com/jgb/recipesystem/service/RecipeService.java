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

    public void createRecipe(RecipeDTO recipeDTO) throws DuplicateEntryException {
        Optional<Recipe> existingRecipe = recipeRepository.findOneByName(recipeDTO.getName());
        if (existingRecipe.isPresent()) {
            throw new DuplicateEntryException("A recipe with name " +  recipeDTO.getName() + " already exists.");
        }
        Recipe recipe = RecipeMapper.MAPPER.toRecipeEntity(recipeDTO);
        recipe.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
        recipeRepository.save(recipe);
    }

    public void updateRecipe(RecipeDTO recipeDTO) throws RecipeNotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findOneByName(recipeDTO.getName());
        if (recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException("Could not find recipe with name" + recipeDTO.getName());
        }
        Recipe recipe = RecipeMapper.MAPPER.toRecipeEntity(recipeDTO);
        recipe.setId(recipeOptional.get().getId());
        recipe.setCreationDateTime(recipeOptional.get().getCreationDateTime());
        recipeRepository.save(recipe);

    }

    public void deleteRecipe(String recipeName) throws RecipeNotFoundException {
        Optional<Recipe> recipeToDelete = recipeRepository.findOneByName(recipeName);
        if (recipeToDelete.isEmpty()) {
            throw new RecipeNotFoundException("Could not find recipe with name " + recipeName);
        }
        recipeRepository.deleteByName(recipeName);
    }

    public RecipeDTO getRecipe(String recipeName) throws RecipeNotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findOneByName(recipeName);
        return recipeOptional.map(RecipeMapper.MAPPER::toRecipeDTO)
                .orElseThrow(() -> new RecipeNotFoundException("Could not find recipe with name" + recipeName));
    }

}
