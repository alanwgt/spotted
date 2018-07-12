package com.alanwgt.spotted.controllers;

import com.alanwgt.spotted.auth.AuthInterceptor;
import com.alanwgt.spotted.auth.Authentication;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class FirebaseCallbackController {
//
//    @GetMapping("/callback")
//    public String providesCallback(HttpSession session, HttpServletResponse response) {
//        if (Authentication.isSessionValid(session)) {
//            HomeController.redirectToHome(response);
//            return null;
//        }
//        return "callback";
//    }

    @PostMapping("/signin")
    public void providesSignin(HttpServletResponse response, HttpSession session, @RequestBody String id_token) {
        FirebaseToken token;
        id_token = id_token.replace("id_token=", "");
        try {
            System.out.println("trying to parse: " + id_token);
            token = Authentication.getDecodedToken(id_token);
            if (token == null) {
                throw new Exception("Couldn't validate token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect(AuthInterceptor.AUTHN_VIEW);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        Authentication.signUser(session, token);

        try {
            response.sendRedirect("/home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
