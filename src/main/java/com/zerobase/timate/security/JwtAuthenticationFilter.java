package com.zerobase.timate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

	// 요청이 들어올 때마다 필터가 컨트롤러에 실행되기 전에 먼저 실행되면서 요청의 헤더에 토큰이 있는지 확인하고,
	// 토큰이 유효하다면 인증정보를 context 에 담음
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveTokenFromRequest(request);

		if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
			Authentication auth = this.tokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth); //현재 인증된 사용자의 정보를 Spring Security 의 보안 컨텍스트에 설정

			log.info(String.format("[%s] -> %s", this.tokenProvider.getUsername(token), request.getRequestURI()));
		}

		filterChain.doFilter(request, response);

    }


	private String resolveTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_HEADER);

		if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
			return token.substring(TOKEN_PREFIX.length());
		}

		return null;
	}
}
