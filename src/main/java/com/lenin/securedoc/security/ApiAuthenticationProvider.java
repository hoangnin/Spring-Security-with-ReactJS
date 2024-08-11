package com.lenin.securedoc.security;


import com.lenin.securedoc.domain.ApiAuthentication;
import com.lenin.securedoc.domain.UserPrincipal;
import com.lenin.securedoc.exception.ApiException;
import com.lenin.securedoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.lenin.securedoc.constant.Constants.NINETY_DAYS;
import static com.lenin.securedoc.domain.ApiAuthentication.*;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       var apiAuthentication = authenticationFunction.apply(authentication);
       var user = userService.getUserByEmail(apiAuthentication.getEmail());
       if (user != null){
           var userCredential = userService.getUserCredentialById(user.getId());
           if (userCredential.getUpdateAt().minusDays(NINETY_DAYS).isAfter(LocalDateTime.now())){
               throw new ApiException("Credentials are expired. Please reset your password");
           }
           var userPrincipal = new UserPrincipal(user, userCredential);
           validAccount.accept(userPrincipal);
           if (encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())){
               return authenticated(user, userPrincipal.getAuthorities());
           }else throw new BadCredentialsException("Email and/or password incorrect. Please try again");
       }else throw new ApiException("Unable to authenticate");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if (userPrincipal.isAccountNonLocked()){throw new LockedException("Your account is currently locked");}
        if (userPrincipal.isEnabled()){throw new DisabledException("Your account is currently disabled");}
        if (userPrincipal.isCredentialsNonExpired()){throw new CredentialsExpiredException("Your password has expired. Please update your password");}
        if (userPrincipal.isAccountNonExpired()){throw new DisabledException("Your password has expired. Please contact administrator");}
    };
}
//9:24
