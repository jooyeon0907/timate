package com.zerobase.timate.security;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/auth/sign-up", "/auth/sign-in", // 로그인 & 회원가입은 허용
					"/v3/api-docs/**",  // OpenAPI 문서
					"/swagger-ui/**",   // Swagger UI 정적 리소스
					"/swagger-ui.html"  // Swagger UI 메인 페이지
				).permitAll()
				.anyRequest().authenticated()) // 그 외 요청은 인증 필요
			.formLogin(form -> form.disable())
			.logout(logout -> logout.disable())
			.httpBasic(hb -> hb.disable())
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
			.addFilterBefore(this.jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class)
		;

		http
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(customAuthenticationEntryPoint))
		;


		return http.build();
	}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
