package com.jgb.recipesystem.service;

import com.jgb.recipesystem.database.entity.Recipe;
import com.jgb.recipesystem.database.repository.RecipeRepository;
import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import com.jgb.recipesystem.model.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * RecipeServiceTest
 * <br>
 * <code>com.jgb.recipesystem.service.RecipeServiceTest</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 10 March 2022
 */
@SpringBootTest
public class RecipeServiceTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void createRecipe_whenRecipeNameExists_throwsDuplicateEntryException() {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.of(buildRecipe()));
        //when
        Exception exception = assertThrows(DuplicateEntryException.class, () ->  recipeService.createRecipe(recipeDto));
        //then
        String actualMessage = "A recipe with name " +  recipeDto.getName() + " already exists.";
        assertEquals(actualMessage, exception.getMessage());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void createRecipe_whenRecipeNameDoesNotExist_recipeIsSaved() throws DuplicateEntryException {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.empty());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(buildRecipe());
        //when
        recipeService.createRecipe(recipeDto);
        //then
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void getRecipe_whenRecipeNameExists_returnsRecipe() throws RecipeNotFoundException {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        Recipe recipeEntity = buildRecipe();
        recipeEntity.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.of(recipeEntity));
        //when
        RecipeDTO result = recipeService.getRecipe(recipeDto.getName());
        //then
        assertEquals(recipeDto.getName(), result.getName());
        verify(recipeRepository).findOneByName(anyString());
    }

    @Test
    void getRecipe_whenRecipeNameDoesNotExist_recipeNotFoundExceptionIsThrown() {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(RecipeNotFoundException.class, () ->  recipeService.getRecipe(recipeDto.getName()));
        //then
        String actualMessage = "Could not find recipe with name" + recipeDto.getName();
        assertEquals(actualMessage, exception.getMessage());
    }

    @Test
    void updateRecipe_whenRecipeNameDoesNotExist_thenThrowRecipeNotFoundException() {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(RecipeNotFoundException.class, () ->  recipeService.updateRecipe(recipeDto));
        //then
        String actualMessage = "Could not find recipe with name" + recipeDto.getName();
        assertEquals(actualMessage, exception.getMessage());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_whenRecipeNameIsFound_recipeIsSaved() throws RecipeNotFoundException {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.of(buildRecipe()));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(buildRecipe());
        //when
        recipeService.updateRecipe(recipeDto);
        //then
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void deleteRecipe_whenRecipeNameDoesNotExist_thenThrowRecipeNotFoundException() {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(RecipeNotFoundException.class, () ->  recipeService.deleteRecipe(recipeDto.getName()));
        //then
        String actualMessage = "Could not find recipe with name " + recipeDto.getName();
        assertEquals(actualMessage, exception.getMessage());
        verify(recipeRepository, never()).deleteByName(anyString());
    }

    @Test
    void deleteRecipe_whenRecipeNameDoesNotExist_recipeIsSaved() throws RecipeNotFoundException {
        //given
        RecipeDTO recipeDto = buildRecipeDTO();
        when(recipeRepository.findOneByName(anyString())).thenReturn(Optional.of(buildRecipe()));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(buildRecipe());
        //when
        recipeService.deleteRecipe(recipeDto.getName());
        //then
        verify(recipeRepository).deleteByName(anyString());
    }

    private Recipe buildRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("four_cheese_pizza");
        recipe.setVegetarian(true);
        recipe.setServings(1);
        recipe.setIngredients(List.of("pizza crust", "mozzarella", "fontina", "parmigiano-reggiano", "gorgonzola"));
        recipe.setCookingInstructions("put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C");
        return recipe;
    }

    private RecipeDTO buildRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("four_cheese_pizza");
        recipeDTO.setVegetarian(true);
        recipeDTO.setServings(1);
        recipeDTO.setIngredients(List.of("pizza crust", "mozzarella", "fontina", "parmigiano-reggiano", "gorgonzola"));
        recipeDTO.setCookingInstructions("put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C");
        return recipeDTO;
    }
}
