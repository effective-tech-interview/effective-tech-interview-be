package com.sparcs.teamf.api.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparcs.teamf.api.error.dto.ErrorResponseDto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 사용자가 인증되지 않은 상태에서 보호된 자원에 액세스하려고 할 때 401 UnAuthorized 에러 리턴
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.info("인증 정보가 유효하지 않습니다.");
        response.setContentType("application/json;charset=UTF-8");
        String result = objectMapper.writeValueAsString(
                new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), "인증 정보가 유효하지 않습니다."));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(result);
    }
}
