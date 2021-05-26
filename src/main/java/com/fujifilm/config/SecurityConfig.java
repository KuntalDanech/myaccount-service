package com.fujifilm.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fujifilm.filter.SecurityFilter;

/**
 * Most important class for Security
 * 
 * @author pce16
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true, 
		prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private InvalidAuthEntryPoint entryPoint;
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
	/**
	 * Authentication - user details will be fetched here
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		// Initially there are two users - InMemory users
		
		.passwordEncoder(encoder)		
		// First user
		.withUser("fujicustomer-1")
		.password(encoder.encode("password"))
		.roles("CUSTOMER")
		.and()
		// Second User
		.withUser("fujicustomer-2")
		.password(encoder.encode("password"))
		.roles("CUSTOMER");
	}
	
	/**
	 * Authorization
	 * Enable Which URL for which Roles.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		.cors()
		.and()
		.csrf().disable()
		.authorizeRequests()

		.antMatchers("login").permitAll()
		.anyRequest().authenticated()
		
		//If error then rest API response will be given
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(entryPoint)
		
		// Session creation policy will be state less
		
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)		
		
		// Register the request 2nd request onward
		.and()
		.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
	}
	/**
	 * Spring Security Cors Policy
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	    configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
