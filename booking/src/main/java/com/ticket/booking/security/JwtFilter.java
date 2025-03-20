package com.ticket.booking.security;

import com.ticket.booking.models.User;
import com.ticket.booking.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends GenericFilterBean {
    @Autowired JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String auth = httpRequest.getHeader("Authorization");

        if(auth != null && auth.startsWith("Bearer ")){
            String token = auth.substring(7);

            if(jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                Optional<User> user = userRepository.findByEmail(email);
                if(user.isPresent()) {
                    User userObject = user.get();

                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(userObject.getEmail())
                            .password(userObject.getPassword())
                            .roles(role)
                            .build();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
