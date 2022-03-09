package com.jgb.recipesystem.database.repository;

import com.jgb.recipesystem.database.entity.Recipe;
import com.jgb.recipesystem.model.RecipeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RecipeRepository
 * <br>
 * <code>com.jgb.recipesystem.database.repository.RecipeRepository</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findOneByName(String recipeName);

    void deleteByName(String name);
}
