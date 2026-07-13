package com.identityaccessmanagement.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.identityaccessmanagement.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http

				.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/api/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**")
						.permitAll()

						.requestMatchers(HttpMethod.POST, "/api/accounts/**").hasAnyRole("ADMIN", "ACCOUNTANT")

						.requestMatchers(HttpMethod.PUT, "/api/accounts/**").hasAnyRole("ADMIN", "ACCOUNTANT")

						.requestMatchers(HttpMethod.DELETE, "/api/accounts/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/api/accounts/**").authenticated()

						.requestMatchers(HttpMethod.POST, "/api/transactions/credit").hasAnyRole("ADMIN", "ACCOUNTANT")

						.requestMatchers(HttpMethod.POST, "/api/transactions/debit").hasAnyRole("ADMIN", "ACCOUNTANT")

						.requestMatchers(HttpMethod.POST, "/api/transactions/transfer").hasRole("ADMIN")

						.anyRequest().authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}
}
