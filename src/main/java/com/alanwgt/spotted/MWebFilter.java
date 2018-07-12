package com.alanwgt.spotted;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class MWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self' https://spotted-209117.firebaseapp.com/ https://www.google.com https://www.googleapis.com https://use.fontawesome.com https://www.gstatic.com https://cdn.firebase.com https://securetoken.googleapis.com https://fonts.googleapis.com https://fonts.gstatic.com https://apis.google.com 'unsafe-inline';");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
