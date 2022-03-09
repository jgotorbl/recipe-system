package com.jgb.recipesystem.controller;

import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import com.jgb.recipesystem.model.RecipeDTO;
import com.jgb.recipesystem.service.RecipeService;
import com.jgb.recipesystem.validation.RecipeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * RecipeController
 * <br>
 * <code>com.jgb.recipesystem.controller.RecipeController</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeValidator recipeValidator;
    private final RecipeService recipeService;

    @InitBinder("recipeDTO")
    public void initRecipeBinder(WebDataBinder binder) {
        binder.addValidators(recipeValidator);
    }

    @PostMapping(value = "/create-recipe", consumes = {"application/json"})
    public ResponseEntity<Void> createRecipe(@Validated @RequestBody RecipeDTO recipeDTO) throws DuplicateEntryException {
        log.info("Recipe DTO: {}", recipeDTO.toString());
        recipeService.createRecipe(recipeDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/get-recipe/{name}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable("name") String recipeName) throws RecipeNotFoundException {
        RecipeDTO recipeDTO = recipeService.getRecipe(recipeName);
        return ResponseEntity.ok(recipeDTO);
    }

    @PostMapping(value = "/update-recipe", produces = {"application/json"})
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO) throws RecipeNotFoundException {
        recipeService.updateRecipe(recipeDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete-recipe/{name}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("name") String recipeName) throws RecipeNotFoundException {
        recipeService.deleteRecipe(recipeName);
        return ResponseEntity.noContent().build();
    }

}
