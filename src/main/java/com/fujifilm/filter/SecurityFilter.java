package com.fujifilm.filter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fujifilm.util.JWTTokenUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JWTTokenUtil jwtUtil;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// 1. Read the token from Header section
		String token = request.getHeader("Authorization");
		if(token!=null) {
			System.out.println(token);
			String userName = jwtUtil.getUserName(token);
			// If user name !=null && SecurityContext should be null
						if(userName!=null 
								&& SecurityContextHolder
								.getContext().getAuthentication()==null) {
							boolean isValid = jwtUtil.validateToken(token, userName);
							if(isValid) {		
								System.out.println("Valid");
								Set<String> roles = Set.of("ROLE_CUSTOMER");
								UsernamePasswordAuthenticationToken upAuthToken = 
										new UsernamePasswordAuthenticationToken(
												"fujicustomer-1", 
												encoder.encode("password"),
												roles.stream()
												.map(role->new SimpleGrantedAuthority(role))
												.collect(Collectors.toList()));
								
								upAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								// final object storing in Security context authentication details
								SecurityContextHolder.getContext().setAuthentication(upAuthToken);
							}
						}
		}
	//  It will go without security context object if token==null or invlid token
	filterChain.doFilter(request, response);
		
	}
		
}
