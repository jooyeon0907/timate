package com.zerobase.timate.security;

import com.zerobase.timate.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

	private final AuthService authService;

    @PostConstruct // Bean 이 생성된 후 자동 실행
    public void init() {
		// secretKey 를 HMAC-SHA 키로 변환하여 key 에 저장
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, Long id) {
		Claims claims = Jwts.claims().setSubject(email)
									.setId(id.toString());
        return Jwts.builder()
				.setClaims(claims)
//				.setId(String.valueOf(id))
//                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

	public boolean validateToken(String token){
		if (!StringUtils.hasText(token)) return false; // 토큰이 유효하지 않다면 false 반환

		var claims = this.parseClaims(token);
		return !claims.getExpiration().before(new Date()); // 토큰의 만료시간을 현재 시간과 비교하여 만료 여부 반환
	}

	public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.authService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // 사용자 정보와 사용자 권한 정보를 포함한 토큰
    }

	public String getUsername(String token) {
		return this.parseClaims(token).getSubject();
	}

	// 토큰이 유효한지 확인
	private Claims parseClaims(String token) {
		try{
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e){
        	return e.getClaims();
		}catch (JwtException e) {
			// JWT 관련 다른 예외 처리
			throw new IllegalArgumentException("Invalid JWT token", e);
		}
	}


}
