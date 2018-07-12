package com.alanwgt.spotted.controllers;

import com.alanwgt.spotted.auth.Authentication;
import com.alanwgt.spotted.models.Post;
import com.alanwgt.spotted.models.Upvote;
import com.alanwgt.spotted.network.HttpRequest;
import com.alanwgt.spotted.services.PostService;
import com.alanwgt.spotted.services.UpvoteService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    public static URL RECAPTCHA_ENDPOINT;
    public static final String RECAPTCHA_SECRET = "6LdDvGMUAAAAADS17usPs4U1DC1zaMdwF0m5L0po";

    static {
        try {
            RECAPTCHA_ENDPOINT = new URL("https://www.google.com/recaptcha/api/siteverify");
        } catch (MalformedURLException e) {
            RECAPTCHA_ENDPOINT = null;
            e.printStackTrace();
        }
    }

    @Autowired
    private PostService postService;
    @Autowired
    private UpvoteService upvoteService;

    @GetMapping("/home")
    public String provideHomePage(HttpSession session, Model model) {
        String uid = Authentication.getUid(session);
        Boolean isAdm = Authentication.isAdmin(uid);

        List<Post> posts = postService.getPosts(uid);
        List<Upvote> upvotes = upvoteService.getUpvotes(uid);
        Map<Long, Upvote> upvoteMap = new HashMap<>();

        for (Upvote u : upvotes) {
            upvoteMap.put(u.getPost().getId(), u);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("upvotes", upvoteMap);
        model.addAttribute("admin", isAdm);

        System.out.println(posts.size());
        return "home";
    }

    @GetMapping("/signout")
    @ResponseStatus(value = HttpStatus.OK)
    public void providesSignOut(HttpSession session, HttpServletRequest request) {
        Authentication.invalidateSession(request, session);
    }

    public static void redirectToHome(HttpServletResponse response) {
        try {
            response.sendRedirect("/home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/sudo")
    public String providesAdminPage(HttpSession session, HttpServletResponse response) {
        String uid = Authentication.getUid(session);
        Boolean isAdm = Authentication.isAdmin(uid);

        if (!isAdm) {
            HomeController.redirectToHome(response);
            return null;
        }

        return "sudo";
    }

    @PostMapping("/give-sudo")
    public void providesSudo(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
            @RequestParam(name = "identifier") String identifier) {
        String uid = Authentication.getUid(session);
        Boolean isAdm = Authentication.isAdmin(uid);

        if (!isAdm) {
            HomeController.redirectToHome(response);
            return;
        }

        HttpRequest recaptchaRequest = new HttpRequest(RECAPTCHA_ENDPOINT);
        String recaptchaRequestData = String.format("secret=%s&response=%s", RECAPTCHA_SECRET, recaptchaResponse);

        recaptchaRequest.doPost(recaptchaRequestData).subscribe(res -> {
            if (!CaptchaValidator.validate(res)) {
                HomeController.redirectToHome(response);
                return;
            }

            UserRecord record = Authentication.getUserRecordByEmail(identifier);

            if (record == null) {
                HomeController.redirectToHome(response);
                return;
            }

            Map<String, Object> currentClaims = record.getCustomClaims();
            Map<String, Object> newClaims = new HashMap<>();

            if (Boolean.TRUE.equals(currentClaims.get("admin"))) {
                System.out.println("User is already an admin!");
                return;
            }

            for (String s : currentClaims.keySet()) {
                newClaims.put(s, currentClaims.get(s));
            }

            newClaims.put("admin", true);
            //propagate
            try {
                FirebaseAuth.getInstance().setCustomUserClaims(record.getUid(), newClaims);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }

            HomeController.redirectToHome(response);
        });
    }
}