package com.fujifilm.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT Token Generation Logic 
 * 
 * @author pce16
 *
 */
@Component
public class JWTTokenUtil {

	@Value("{fujifilm.secret}")
	private String secret;

	@Value("{fujifilm.issuer}")
	private String issuer;
	
	// Validate token
	public boolean validateToken(String token,String dbUserName) {
		String userName = getUserName(token);
		return (userName.equals(dbUserName));
	}
	
	// Read subject/username
	public String getUserName(String token) {
		return getClaims(token).getSubject();
	}
	
	// Read claims
	public Claims getClaims(String token) {
		
		return Jwts.parser()
				.setSigningKey(secret.getBytes())
				.parseClaimsJws(token)
				.getBody()
				;
	}	
	
	/**
	 * Generate Token Logic
	 * 
	 * @param subject
	 * @return
	 */
	public String generateToken(String subject) {
				
		return Jwts.builder()
				// # This is username
				.setSubject(subject)
				// # Who issue the token
				.setIssuer(issuer)
				// # When token has been issued ?
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// # Which signature algorithm, We have used with secret key
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}
}
