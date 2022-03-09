package com.jgb.recipesystem.database.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Recipe
 * <br>
 * <code>com.jgb.recipesystem.database.entity.Recipe</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 07 March 2022
 */
@Entity
@Table(name = "recipe")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Timestamp creationDateTime;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean vegetarian;
    @Column(nullable = false)
    private Integer servings;
    @ElementCollection
    @CollectionTable(name = "ingredients_list", joinColumns = @JoinColumn(name = "id")) // 2
    @Column(name = "ingredients_list")
    private List<String> ingredients;
    private String cookingInstructions;

}
