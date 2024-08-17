package com.restApi.social_media_app.security;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class MyConfig {
	
	@Autowired
	private JwtFilter filter;
	
	@Autowired
	private UserService userDetailsServiceImple;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsServiceImple);
		daoProvider.setPasswordEncoder(passwordEncoder());
		
		return daoProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		   .authorizeHttpRequests()
		   .requestMatchers("/public/**").permitAll()
		   .requestMatchers("/api/users/**", "/api/posts/**" , "/api/comments/**" ,"/api/reels/**").hasRole("USER")
		   .anyRequest().authenticated()
		   .and()
		   .csrf().disable()
		   .cors(cors -> cors.configurationSource(CorsConfigurationSource()))
		   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	          .and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
			  http.authenticationProvider(daoAuthenticationProvider());	
			  return http.build();
	}
	
	
	private CorsConfigurationSource CorsConfigurationSource() {
		return new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfgConfiguration = new CorsConfiguration();
				cfgConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
				cfgConfiguration.setAllowedMethods(Collections.singletonList("*"));
				cfgConfiguration.setAllowCredentials(true);
				cfgConfiguration.setAllowedHeaders(Collections.singletonList("*"));
				cfgConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
				cfgConfiguration.setMaxAge(3600L);
				
				return cfgConfiguration;
			}
		};
	}

}
