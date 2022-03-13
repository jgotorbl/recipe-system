package com.jgb.recipesystem.controller;

import com.jgb.recipesystem.exception.DuplicateEntryException;
import com.jgb.recipesystem.exception.RecipeNotFoundException;
import com.jgb.recipesystem.model.RecipeDTO;
import com.jgb.recipesystem.service.RecipeService;
import com.jgb.recipesystem.validation.RecipeValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    /**
     * Submits the necessary info to create a new recipe
     *
     * @param recipeDTO recipe to be created
     * @return ResponseEntity with status no-content if the request is processed successfully
     * @throws DuplicateEntryException if there is a recipe with the same name stored in the database
     */
    @Operation(
            method = "POST",
            description = "Submits information about a new recipe",
            security = {
                @SecurityRequirement(name = "Basic Auth")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                content = {
                        @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject("'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":-1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'"),
                            }
                        )
                }
            ),
            responses = {
                @ApiResponse(responseCode = "204", description = "The server has successfully fulfilled the request and that there is no additional content to send in the responde payload body."),
                @ApiResponse(
                        responseCode = "400",
                        description = "The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).",
                        content = {
                                @Content(
                                        mediaType = "text/plain",
                                        examples = {
                                                @ExampleObject("[{\"code\": \"greater_than_zero\",\"fieldName\": \"servings\",\"rejectedValue\": -1,\"message\": \"Please specify a value greater than zero.\"}]"),
                                                @ExampleObject("{\"message\": \"A recipe with name pizza_quatro_fromaggi already exists.\"}")
                                        }
                                )
                        }
                ),
                @ApiResponse(responseCode = "401", description = "The server understood the request but the user has unauthorized access to this resource."),
                @ApiResponse(responseCode = "403", description = "The server understood the request but the user has forbidden access to this resource."),
                @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented it from fulfilling the request.")
            }
    )
    @PostMapping(value = "/create-recipe", consumes = {"application/json"})
    public ResponseEntity<Void> createRecipe(@Validated @RequestBody RecipeDTO recipeDTO) throws DuplicateEntryException {
        log.info("Recipe DTO: {}", recipeDTO.toString());
        recipeService.createRecipe(recipeDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint used to get information about a specific recipe on the system
     *
     * @param recipeName name of the recipe from which the user is requesting information
     * @return ResponseEntity with HttpStatus.OK and a response body containing info about the recipe
     * @throws RecipeNotFoundException if a recipe with that name is not found on the system
     */
    @Operation(
            parameters = {
                    @Parameter(name = "name", in = ParameterIn.PATH, required = true)
            },
            security = {
                    @SecurityRequirement(name = "Basic Auth")
            },
            method = "GET",
            description = "Retrieves information about a specific recipe, if a recipe that matches the name passed as parameter is found ",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "The request has succeeded.",
                            content = {
                                @Content(
                                    mediaType = "application/json",
                                    examples = {
                                        @ExampleObject("'{\"creationDateTime\": \"09‐03‐2022 19:39\",\"name\": \"pizza_quatro_fromaggi\",\"vegetarian\": false,\"servings\": 4,\"ingredients\": [\"pizza crust\",\"tomato\",\"blue cheese\",\"roquefort\",\"mozarella\",\"gouda\"],\"cookingInstructions\": \"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"\n}'"),
                                    }
                                )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).",
                            content = {
                                    @Content(
                                            mediaType = "text/plain",
                                            examples = @ExampleObject("A recipe with name pizza_quatro_fromaggi already exists.")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The server understood the request but the user has forbidden access to this resource."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected condition that prevented it from fulfilling the request."
                    )
            }
    )
    @GetMapping(value = "/get-recipe/{name}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable("name") String recipeName) throws RecipeNotFoundException {
        RecipeDTO recipeDTO = recipeService.getRecipe(recipeName);
        return ResponseEntity.ok(recipeDTO);
    }

    /**
     * Endpoint used to update recipes in the recipe system
     *
     * @param recipeDTO name of the recipe the user is updating
     * @return ResponseEntity with status no-content if the request is processed successfully
     * @throws RecipeNotFoundException if a recipe with that name is not found on the system
     */
    @Operation(
            method = "POST",
            description = "Updates an existing recipe",
            security = {
                    @SecurityRequirement(name = "Basic Auth")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject("'{\"name\":\"four_cheese_pizza\",\"vegetarian\":true,\"servings\":-1,\"ingredients\":[\"pizza crust\",\"mozzarella\",\"fontina\",\"parmigiano-reggiano\",\"gorgonzola\"],\"cookingInstructions\":\"put all the ingredients on top of each other and cook in the oven for 15 minutes at 200C\"}'"),
                                    }
                            )
                    }
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "The server has successfully fulfilled the request and that there is no additional content to send in the responde payload body."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                    @ExampleObject("[{\"code\": \"non-null\",\"fieldName\": \"vegetarian\",\"message\": \"Value may not be null.\"}]"),
                                                    @ExampleObject("{\"message\": \"Could not find recipe with namepizza_quatro_fromagg\"}")
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "The server understood the request but the user has unauthorized access to this resource."),
                    @ApiResponse(responseCode = "403", description = "The server understood the request but the user has forbidden access to this resource."),
                    @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented it from fulfilling the request.")
            }
    )
    @PostMapping(value = "/update-recipe", produces = {"application/json"})
    public ResponseEntity<Void> updateRecipe(@Validated @RequestBody RecipeDTO recipeDTO) throws RecipeNotFoundException {
        recipeService.updateRecipe(recipeDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint used to delete recipes from the recipe system
     *
     * @param recipeName the name of the recipe to be deleted
     * @return return ResponseEntity with HttpStatus.NO_CONTENT if a recipe is deleted from the system
     * @throws RecipeNotFoundException if no recipe with recipeName is found in the database
     */
    @Operation(
            parameters = {
                    @Parameter(name = "name", in = ParameterIn.PATH, required = true)
            },
            security = {
                    @SecurityRequirement(name = "Basic Auth")
            },
            method = "DELETE",
            description = "Deletes a recipe with a specific name from the system, if any is found",
            responses = {
                    @ApiResponse(responseCode = "204", description = "The server has successfully fulfilled the request and that there is no additional content to send in the responde payload body."),
                    @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing)."),
                    @ApiResponse(responseCode = "401", description = "The server understood the request but the user has unauthorized access to this resource."),
                    @ApiResponse(responseCode = "403", description = "The server understood the request but the user has forbidden access to this resource."),
                    @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented it from fulfilling the request.")
            }
    )
    @DeleteMapping(value = "/delete-recipe/{name}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("name") String recipeName) throws RecipeNotFoundException {
        recipeService.deleteRecipe(recipeName);
        return ResponseEntity.noContent().build();
    }

}
