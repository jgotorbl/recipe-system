package com.jgb.recipesystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfig
 * <br>
 * <code>com.jgb.recipesystem.configuration.WebSecurityConfig</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 09 March 2022
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String BASE_PATH = "/recipe-system";

    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String anyPath = BASE_PATH.concat("/**");
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, anyPath).hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, anyPath).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(this.getPasswordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("admin").password(this.getPasswordEncoder().encode("password")).roles("USER", "ADMIN");
    }
}
