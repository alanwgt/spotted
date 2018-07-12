package com.alanwgt.spotted.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@Configuration
public class AuthInterceptor extends HandlerInterceptorAdapter {
//    https://github.com/alanwgt/poow-final/blob/master/src/main/java/me/alanwe/poowfinal/auth/AuthInterceptor.java
    private static final String[] URL_WHITE_LIST = new String[] {
        "authn",
        "signin",
        "assets"
    };

    public static final String AUTHN_VIEW = "/authn";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI().replaceFirst("/", "");
        String[] explodedRequest = requestUri.split("/");

        // approve requests that are in the whitelist
        if (Arrays.stream(URL_WHITE_LIST).anyMatch(explodedRequest[0]::equals)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        // if the session is null, the user is not logged yet
        if (session == null) {
            redirectToAuthenticationPage(request, response);
            return false;
        }

        FirebaseToken token = Authentication.getTokenFromSession(session);

        if (token == null) {
            redirectToAuthenticationPage(request, response);
            return false;
        }

        return true;
    }

    private void redirectToAuthenticationPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(AUTHN_VIEW);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
