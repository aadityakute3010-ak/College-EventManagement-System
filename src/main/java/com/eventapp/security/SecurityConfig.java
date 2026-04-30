package com.eventapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private CustomAuthEntryPoint customAuthEntryPoint;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))

				.authorizeHttpRequests(auth -> auth

						//PUBLIC APIs
						.requestMatchers("/admin/loginAdmin", "/students/login", "/students/add", "/events/search", "/events/upcoming","/events/past","/events/category/*","/students/forgot-password","/students/reset-password","/admin/admin-forgot-password","/admin/admin-reset-password").permitAll()

						//STUDENT + ADMIN (VIEW EVENTS)
						.requestMatchers(HttpMethod.GET, "/events").hasAnyAuthority("ADMIN", "STUDENT")

						.requestMatchers(HttpMethod.GET, "/events/*").hasAnyAuthority("ADMIN", "STUDENT")
						
						.requestMatchers("/dashboard").hasAnyAuthority("ADMIN", "STUDENT")

						//STUDENT ONLY
						.requestMatchers(HttpMethod.POST, "/registrations/register","/registrations/pay/**","/payment/pay/**").hasAuthority("STUDENT") 
						
						.requestMatchers(HttpMethod.GET, "/events/recommendations")
						.hasAuthority("STUDENT")
						
						.requestMatchers(HttpMethod.PUT, "/students/update-password")
						.hasAuthority("STUDENT")
						
						//ADMIN ONLY,

						// Admin APIs
						.requestMatchers("/admin/**").hasAuthority("ADMIN")
						
						.requestMatchers(HttpMethod.PUT, "/admin/change-password")
						.hasAuthority("ADMIN")

						// Student management
						.requestMatchers("/students/allStudents", "/students/*")
						.hasAuthority("ADMIN")

						// Event management
						.requestMatchers(HttpMethod.POST, "/events").hasAuthority("ADMIN")

						.requestMatchers(HttpMethod.DELETE, "/events/*").hasAuthority("ADMIN")

						.requestMatchers("/events/*/students").hasAuthority("ADMIN")

						// Registration management
						.requestMatchers("/registrations/approve", "/registrations/pending/*",
								"/registrations/approved/*", "/registrations/allReg")
						.hasAuthority("ADMIN")

						//ANY OTHER REQUEST
						.anyRequest().authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
	
}