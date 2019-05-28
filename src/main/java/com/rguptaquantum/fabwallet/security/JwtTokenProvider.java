package com.rguptaquantum.fabwallet.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.expirelength}")
    private long validityInMilliseconds = 3600000;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private static final String TOKEN_TYPE = "Bearer";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    public AuthenticationToken createToken(String username) {


        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);


        String token = JWT.create()
                .withIssuer("auth0")
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);

        return new AuthenticationToken(token,TOKEN_TYPE,validityInMilliseconds);
    }

    public Authentication getAuthentication(String token) throws JWTVerificationException, UsernameNotFoundException  {
        DecodedJWT decodedToken = decodeToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedToken.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_TYPE+" ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private DecodedJWT decodeToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build(); //Reusable verifier instance
        return verifier.verify(token);
    }
}
