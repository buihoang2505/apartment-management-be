package com.apartment.app.auth.handler;

import com.apartment.app.auth.TokenProvider;
import com.apartment.app.auth.command.LoginCommand;
import com.apartment.app.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandHandler {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public TokenResponse handle(LoginCommand cmd) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(cmd.username(), cmd.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails.getUsername());
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        return new TokenResponse(token, userDetails.getUsername(), role, tokenProvider.getExpiration());
    }
}