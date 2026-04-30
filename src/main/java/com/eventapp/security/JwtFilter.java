package com.eventapp.security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String path = request.getServletPath();

    	if (path.startsWith("/events/search") ||
    		path.startsWith("/events/upcoming") ||
    		path.startsWith("/events/past") ||
    		path.startsWith("/students/login") ||
    		path.startsWith("/admin/loginAdmin") ||
    		path.startsWith("/students/add") ||
    	    path.startsWith("/admin/admin-reset-password") ||
    	    path.startsWith("/admin/admin-forgot-password"))
    	{ 

    		    filterChain.doFilter(request, response);
    		    return;
    		}

        String header = request.getHeader("Authorization");

        if (header != null && !header.isEmpty()) {

            try {
				String token;
				
				if (header.startsWith("Bearer ")) {
				    token = header.substring(7);
				} else {
				    
				    token = header;
				}

				String email = jwtUtil.extractEmail(token);
				String role = jwtUtil.extractRole(token);

				UsernamePasswordAuthenticationToken auth =
				        new UsernamePasswordAuthenticationToken(
				                email,
				                null,
				                List.of(new SimpleGrantedAuthority(role))
				        );
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 

				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        filterChain.doFilter(request, response);
    }
}