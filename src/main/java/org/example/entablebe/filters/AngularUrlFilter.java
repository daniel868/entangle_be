package org.example.entablebe.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entablebe.utils.AppUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Component
public class AngularUrlFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean matchUrl = AppUtils.isAngularUrl(request.getRequestURI());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .replacePath(null).replaceQuery(null).fragment(null).build().toUriString();
        if (matchUrl) {
            response.sendRedirect(baseUrl);
            return;
        }
        filterChain.doFilter(request, response);
    }


}
