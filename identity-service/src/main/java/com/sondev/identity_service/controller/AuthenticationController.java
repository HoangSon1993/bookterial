package com.sondev.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.sondev.identity_service.dto.request.AuthenticationRequest;
import com.sondev.identity_service.dto.request.IntrospectRequest;
import com.sondev.identity_service.dto.request.LogoutRequest;
import com.sondev.identity_service.dto.request.RefreshTokenRequest;
import com.sondev.identity_service.dto.response.ApiResponse;
import com.sondev.identity_service.dto.response.AuthenticationResponse;
import com.sondev.identity_service.dto.response.InstrospectResponse;
import com.sondev.identity_service.dto.response.RefreshTokenResponse;
import com.sondev.identity_service.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        // spotless:off
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
        // spotless:on
    }

    @PostMapping("/introspect")
    ApiResponse<InstrospectResponse> authenticated(@RequestBody IntrospectRequest request) {
        InstrospectResponse result;
        try {
            result = authenticationService.instrospect(request);
        } catch (JOSEException | ParseException e) {
            result = InstrospectResponse.builder().valid(false).build();
        }
        // spotless:off
        return ApiResponse.<InstrospectResponse>builder()
                .result(result)
                .build();
        // spotless:on
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        // spotless:off
        return ApiResponse.<Void>builder()
                .build();
        // spotless:on
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        RefreshTokenResponse result = authenticationService.refreshToken(request);
        // spotless:off
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(result)
                .build();
        // spotless:on
    }
}
