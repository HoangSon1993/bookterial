package org.sondev.identity.configuration;

import java.text.ParseException;

import org.sondev.identity.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private final AuthenticationService authenticationService;

    public CustomJwtDecoder(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            var issueTime = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            var jsonHeader = signedJWT.getHeader().toJSONObject();
            var claims = signedJWT.getJWTClaimsSet().getClaims();

            return new Jwt(token, issueTime, expirationTime, jsonHeader, claims);
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
