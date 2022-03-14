package com.jgb.recipesystem.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgb.recipesystem.RecipeSystemApplication;
import com.jgb.recipesystem.database.repository.RecipeRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * RecipeValidatorTest
 * <br>
 * <code>com.jgb.recipesystem.validation.RecipeValidatorTest</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 14 March 2022
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = RecipeSystemApplication.class)
public class RecipeValidatorTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":1,\"ingredients\":[\"pizza crust\",\"\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":-1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"four_cheese_pizza\",\"servings\":1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'",
            "'{\"name\":\"\",\"vegetarian\":true,\"servings\":1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'"
    })
    @WithMockUser(username = "admin", roles = { "ADMIN","USER" })
    void createRecipe_whenJsonIsNotValid_shouldReturnBadRequest(String json) throws Exception {
        //when
        MvcResult mvcResult =  mockMvc.perform(post("/create-recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        //then
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
        //when
        MvcResult mvcResult =  mockMvc.perform(post("/update-recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

}
