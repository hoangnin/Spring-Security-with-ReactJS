package com.lenin.securedoc.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenin.securedoc.domain.Response;
import com.lenin.securedoc.dto.User;
import com.lenin.securedoc.dtorequest.LoginRequest;
import com.lenin.securedoc.service.JwtService;
import com.lenin.securedoc.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE;
import static com.lenin.securedoc.constant.Constants.LOGIN_PATH;
import static com.lenin.securedoc.domain.ApiAuthentication.*;
import static com.lenin.securedoc.enumeration.LoginType.LOGIN_ATTEMPT;
import static com.lenin.securedoc.enumeration.LoginType.LOGIN_SUCCESS;
import static com.lenin.securedoc.enumeration.TokenType.*;
import static com.lenin.securedoc.utils.RequestUtils.getResponse;
import static com.lenin.securedoc.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher(LOGIN_PATH,POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            var user = new ObjectMapper().configure(AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), LoginRequest.class);
            userService.updateLoginAttempt(user.getEmail(), LOGIN_ATTEMPT);
            var authentication = unauthenticated(user.getEmail(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        }catch (Exception exception){
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
            return null;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var user = (User) authentication.getPrincipal();
        userService.updateLoginAttempt(user.getEmail(), LOGIN_SUCCESS);
        var httpResponse = user.isMfa() ? sendQrCode(request, user) : sendResponse( request, response, user);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, REFRESH);
        return getResponse(request, Map.of("user", user), "Login success", OK);
    }

    private Object sendQrCode(HttpServletRequest request, User user) {
        return getResponse(request, Map.of("user", user), "Please enter QR code", OK);
    }
}
