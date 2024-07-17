package com.example.cloud.eureka.backtask.configs;


import com.example.cloud.eureka.backtask.services.JWTUtils;
import com.example.cloud.eureka.backtask.services.UserDetailService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailService userDetailService;
    public static String jwt = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if(authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken=authHeader.substring(7).trim();
        jwt=jwtToken;
        try{
            username=jwtUtils.extractUsername(jwtToken);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=userDetailService.loadUserByUsername(username);

                if(jwtUtils.isTokenValid(jwtToken, userDetails)){
                    SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        }
        catch (MalformedJwtException e) {
            // Log the exception and handle it gracefully
            logger.error("Invalid JWT token: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            // Log the exception and handle it gracefully
            logger.error("User not found: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
