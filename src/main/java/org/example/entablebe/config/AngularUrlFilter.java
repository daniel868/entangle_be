package org.example.entablebe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AngularUrlFilter extends OncePerRequestFilter {
    public static final String MAIN_PAGE = "/main";
    public static final String ADD_CONDITION = "/add";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean matchUrl = request.getRequestURI().contains(MAIN_PAGE) || request.getRequestURI().contains(ADD_CONDITION);
        if (matchUrl) {
            response.sendRedirect(request.getContextPath() + MAIN_PAGE);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
