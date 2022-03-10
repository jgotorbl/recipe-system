package com.jgb.recipesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgb.recipesystem.RecipeSystemApplication;
import com.jgb.recipesystem.database.entity.Recipe;
import com.jgb.recipesystem.database.repository.RecipeRepository;
import com.jgb.recipesystem.model.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * RecipeControllerTest
 * <br>
 * <code>com.jgb.recipesystem.controller.RecipeControllerTest</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 10 March 2022
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = RecipeSystemApplication.class)
public class RecipeControllerTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void createRecipe_whenRecipeDoesNotExistYet_returnsNoContent() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.save(any(Recipe.class))).thenReturn(getRecipeEntity());
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.empty());
        String json = objectMapper.writeValueAsString(recipeDTO);
        //when
        MvcResult mvcResult =  mockMvc.perform(post("/create-recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void createRecipe_whenRecipeWithSameNameExists_returnsBadRequestStatusCode() throws Exception {
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.save(any(Recipe.class))).thenReturn(getRecipeEntity());
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.of(getRecipeEntity()));
        String json = objectMapper.writeValueAsString(recipeDTO);
        MvcResult mvcResult =  mockMvc.perform(post("/create-recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void getRecipe_whenRecipeDoesNotExistYet_returnsBadRequestStatusCode() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.empty());
        //when
        MvcResult mvcResult =  mockMvc.perform(get("/get-recipe/{name}", recipeDTO.getName())).andReturn();
        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void getRecipe_whenRecipeExists_returnsRecipeObject() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.of(getRecipeEntity()));
        //When
        MvcResult mvcResult = mockMvc.perform(get("/get-recipe/{name}", recipeDTO.getName())).andReturn();
        //then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void updateRecipe_whenRecipeDoesNotExistYet_returnsBadRequestStatusCode() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.empty());
        String json = objectMapper.writeValueAsString(recipeDTO);
        //when
        MvcResult mvcResult =  mockMvc.perform(post("/update-recipe")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void updateRecipe_whenRecipeWithSameNameExists_returnsNoContent() throws Exception {
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.save(any(Recipe.class))).thenReturn(getRecipeEntity());
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.empty());
        String json = objectMapper.writeValueAsString(recipeDTO);
        MvcResult mvcResult =  mockMvc.perform(post("/update-recipe")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void deleteRecipe_whenRecipeDoesNotExist_returnsBadRequestStatusCode() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.empty());
        //when
        MvcResult mvcResult =  mockMvc.perform(delete("/delete-recipe/{name}", recipeDTO.getName())).andReturn();
        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void deleteRecipe_whenRecipeExists_deletesRecipeAndReturnsNoContent() throws Exception {
        //given
        RecipeDTO recipeDTO = getValidRecipeDTO();
        when(recipeRepository.findOneByName(recipeDTO.getName())).thenReturn(Optional.of(getRecipeEntity()));
        doNothing().when(recipeRepository).deleteByName(anyString());
        //When
        MvcResult mvcResult = mockMvc.perform(delete("/delete-recipe/{name}", recipeDTO.getName())).andReturn();
        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":1,\"ingredients\":[\"pizza crust\",\"\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":-1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"servings\":1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"\",\"vegetarian\":true,\"servings\":1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'"
    })
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void createRecipe_whenJsonIsNotValid_shouldReturnBadRequest(String json) throws Exception {
        MvcResult mvcResult =  mockMvc.perform(post("/create-recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":1,\"ingredients\":null,\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":-1,\"ingredients\":[],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"servings\":null,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":null,\"vegetarian\":true,\"servings\":1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'"
    })
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void updateRecipe_whenRequestIsNotValid_shouldReturnBadRequestCode(String json) throws Exception {
        MvcResult mvcResult =  mockMvc.perform(post("/update-recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    private Recipe getRecipeEntity() {
        Recipe recipe = new Recipe();
        recipe.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
        recipe.setId(1L);
        recipe.setName("four_cheese_pizza");
        recipe.setVegetarian(true);
        recipe.setServings(1);
        recipe.setIngredients(List.of("pizza crust", "mozzarella", "fontina", "parmigiano-reggiano", "gorgonzola"));
        recipe.setCookingInstructions("put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C");
        return recipe;
    }

    private RecipeDTO getValidRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("four_cheese_pizza");
        recipeDTO.setVegetarian(true);
        recipeDTO.setServings(1);
        recipeDTO.setIngredients(List.of("pizza crust", "mozzarella", "fontina", "parmigiano-reggiano", "gorgonzola"));
        recipeDTO.setCookingInstructions("put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C");
        return recipeDTO;
    }
}
