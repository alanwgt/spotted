package com.alanwgt.spotted.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class Authentication {

    public static final String FIREBASE_ID_TOKEN = "ID_TOKEN";

    @GetMapping("/authn")
    public String provideAuthn(HttpSession session, HttpServletResponse response) {
        if (isSessionValid(session)) {
            try {
                response.sendRedirect("/home");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return "authn";
    }

    public static HttpSession invalidateSession(HttpServletRequest request, HttpSession session) {
        if (!session.isNew()) {
            session.invalidate();
        }

        return request.getSession();
    }

    public static FirebaseToken getDecodedToken(String token) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(token, true);
    }

    public static void signUser(HttpSession session, FirebaseToken firebaseToken) {
        session.setAttribute(FIREBASE_ID_TOKEN, firebaseToken);
    }

    public static String getUid(HttpSession session) {
        FirebaseToken token = getTokenFromSession(session);

        if (token == null) {
            return null;
        }

        return token.getUid();
    }

    public static UserRecord getUserRecord(String uid) {
        UserRecord user;

        try {
            user = FirebaseAuth.getInstance().getUser(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public static UserRecord getUserRecordByEmail(String email) {
        UserRecord user;

        try {
            user = FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public static boolean isAdmin(String uid) {
        UserRecord user = getUserRecord(uid);

        if (user == null) {
            return false;
        }

        return Boolean.TRUE.equals(user.getCustomClaims().get("admin"));
    }

    public static void revokeToken(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().revokeRefreshTokens(uid);
        UserRecord user = FirebaseAuth.getInstance().getUser(uid);

        long revocationSecond = user.getTokensValidAfterTimestamp() / 1000;
        System.out.println("Tokens revoked at: " + revocationSecond);
    }

    public static FirebaseToken getTokenFromSession(HttpSession session) {
        if (session == null) {
            return null;
        }

        return (FirebaseToken) session.getAttribute(FIREBASE_ID_TOKEN);
    }

    public static boolean isSessionValid(HttpSession session) {
        if (session == null) {
            return false;
        }

        FirebaseToken token = (FirebaseToken) session.getAttribute(FIREBASE_ID_TOKEN);
        return token != null;
    }

    public static void redirectToAuthn(HttpServletResponse response) {
        try {
            response.sendRedirect("/authn");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static boolean authenticate(String stringToken) throws FirebaseAuthException {
//        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(stringToken);
//        String uid = decodedToken.getUid();
//    }

}
