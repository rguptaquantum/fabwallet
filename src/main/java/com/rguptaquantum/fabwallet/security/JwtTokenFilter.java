package com.rguptaquantum.fabwallet.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        try {
            if (token != null) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JWTVerificationException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(401, "Invalid Authentication Token");
            return;
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(401, "Invalid User");
            return;

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
