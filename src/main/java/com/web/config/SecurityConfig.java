package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터 체인에 등록
public class SecurityConfig {
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	
	// React랑 연동 시, CORS문제 해결
	@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration(); // 특정 패턴에 대한 CORS 구성을 나타내는 클래스
        config.setAllowCredentials(true); // 인증 정보를 요청에 포함할 수 있도록 허용
        config.addAllowedOrigin("http://localhost:3000"); // 허용할 오리진(도메인)을 설정
        config.addAllowedHeader("*"); // 모든 헤더를 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드를 허용
        source.registerCorsConfiguration("/**", config); // 위에서 설정한 CorsConfiguration을 /** 패턴에 등록. 이는 모든 URL에 대해 CORS 구성이 적용됨을 의미함
        return new CorsFilter(source);  
    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults());
        http        
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);	// React랑 연동 시, CORS문제 해결
        http    
        		.csrf(csrf -> csrf.disable()); 			// CSRF 토큰 비활성화
        http    
        		.headers(headers -> headers.frameOptions().disable());
        http    
        		.authorizeRequests(requests -> requests
//        		   .antMatchers("/checkPwd", "/modify**").authenticated()
//                   .antMatchers("/user/**").hasRole("USER")
//                   .antMatchers("/admin/**").hasRole("ADMIN")
                   .anyRequest().permitAll());
	    http    
	    		.formLogin(login -> login
	               .loginPage("/loginForm")		// 미인증자일 경우 해당 uri 호출 // 현재 안됨. xxxxxxxx
	               .loginProcessingUrl("/login")	// "/login" 주소가 호출되면 시큐리티가 낚아채서(post로 오는것) 대신 로그인 진행시킴. -> 컨트롤러를 안만들어도 된다.
	               .defaultSuccessUrl("/")			// 특정 페이지에서 로그인 요청 받으면, 로그인 후에 특정 페이지로 들어감.
	               .usernameParameter("email"));		// username 파라미터값을 "email"로 받음	(로그인폼의 네임값 가져옴)
	               
        http    
        		.logout(logout -> logout
        				.invalidateHttpSession(true).logoutSuccessUrl("/"));	// logout 요청시 홈으로 이동
        
        http    
        		.oauth2Login(login -> login
                   .loginPage("/loginForm")
                   .defaultSuccessUrl("/loginSuccess")
                   .userInfoEndpoint()
                   .userService(principalOauth2UserService));	// 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip) 코드X. (엑세스토큰 + 사용자프로필정보)를 받음; 
               
			
		
		return http.build();
	}
    
    
}
