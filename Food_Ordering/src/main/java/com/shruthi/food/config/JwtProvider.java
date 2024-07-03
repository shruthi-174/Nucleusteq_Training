package com.shruthi.food.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	
	private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	//generate token
	public String generateToken(Authentication auth) {
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		String roles = populateAuthorities(authorities);

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 50L * 24 * 60 * 60 * 1000);

		String jwt = Jwts.builder()
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.claim("email", auth.getName())
				.claim("authorities", roles)
				.signWith(key)
				.compact();
				
		return jwt;
	}
	
	//extracting email
	public String getEmailFromJwtToken(String jwt) {
		jwt=jwt.substring(7);	
		Claims claim=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		
		String email=String.valueOf(claim.get("email"));
		return email;
	}

	//adding authorities in list seperated by commmas
	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> auth=new HashSet<>();
		
		for(GrantedAuthority authority:authorities) {
			auth.add(authority.getAuthority());
		}
		return String.join(",", auth);
	}
}
