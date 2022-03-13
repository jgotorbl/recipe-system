package com.jgb.recipesystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClientWebConfigJava
 * <br>
 * <code>com.jgb.recipesystem.configuration.ClientWebConfigJava</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 13 March 2022
 */
@EnableWebMvc
@Configuration
public class ClientWebConfigJava implements WebMvcConfigurer {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}
