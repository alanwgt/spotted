package com.alanwgt.spotted.controllers;

import com.alanwgt.spotted.auth.Authentication;
import com.alanwgt.spotted.models.Post;
import com.alanwgt.spotted.models.Report;
import com.alanwgt.spotted.models.Upvote;
import com.alanwgt.spotted.network.HttpRequest;
import com.alanwgt.spotted.services.PostService;
import com.alanwgt.spotted.services.ReportService;
import com.alanwgt.spotted.services.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alanwgt.spotted.controllers.HomeController.RECAPTCHA_SECRET;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private ReportService reportService;

    @PostMapping("/post")
    public void providesPost(HttpSession session,
                             HttpServletResponse response,
                             @RequestParam(value = "message") String message,
                             @RequestParam(name = "g-recaptcha-response") String recaptchaResponse) {
        String uid = Authentication.getUid(session);

        HttpRequest recaptchaRequest = new HttpRequest(HomeController.RECAPTCHA_ENDPOINT);
        String recaptchaRequestData = String.format("secret=%s&response=%s", RECAPTCHA_SECRET, recaptchaResponse);

        recaptchaRequest.doPost(recaptchaRequestData).subscribe(res -> {
            if (CaptchaValidator.validate(res)) {
                postService.create(new Post(uid, message));
            }

            HomeController.redirectToHome(response);
        });
    }

    @GetMapping("/liked")
    public String providesLikedPosts(HttpSession session, Model model) {
        String uid = Authentication.getUid(session);
        Boolean isAdm = Authentication.isAdmin(uid);

        List<Upvote> liked = upvoteService.getUpvotes(uid);
        List<Post> likedPosts = new ArrayList<>();

        Map<Long, Upvote> upvoteMap = new HashMap<>();

        for (Upvote u : liked) {
            likedPosts.add(u.getPost());
            upvoteMap.put(u.getPost().getId(), u);

        }

        model.addAttribute("admin", isAdm);
        model.addAttribute("posts", likedPosts);
        model.addAttribute("upvotes", upvoteMap);

        return "home";
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/like")
    public void providesLikePost(HttpSession session, @RequestParam(name = "post_id") String post_id, @RequestParam(name = "delete", required = false) String delete) {
        String uid = Authentication.getUid(session);

        if (delete != null && delete.equals("true")) {
            upvoteService.delete(uid, Long.valueOf(post_id));
        } else {
            Upvote upvote = new Upvote();
            upvote.setUid(uid);
            upvoteService.create(upvote, post_id);
        }
    }

    @GetMapping("/reported")
    public String providesReportedPosts(HttpSession session, HttpServletResponse response, Model model) {
        String uid = Authentication.getUid(session);

        if (!Authentication.isAdmin(uid)) {
            // user doesn't have credentials
            HomeController.redirectToHome(response);
            return null;
        }

        List<Report> reports = reportService.getAllReports();
        List<Post> reported = postService.getPostsWithReports();
        Map<Long, ArrayList<Report>> reportedMap = new HashMap<>();

        for (Report r : reports) {
            reportedMap.computeIfAbsent(r.getPost().getId(), k -> new ArrayList<>());
            reportedMap.get(r.getPost().getId()).add(r);
        }

        model.addAttribute("admin", Boolean.TRUE);
        model.addAttribute("posts", reported);
        model.addAttribute("reports", reportedMap);

        return "home";
    }

    @PostMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK)
    public void providesDeletePost(HttpSession session, @RequestParam(name = "post_id") String post_id) {
        String uid = Authentication.getUid(session);

        if (!Authentication.isAdmin(uid)) {
            // user doesn't have credentials
            return;
        }

        postService.softDelete(Long.valueOf(post_id), uid);
    }
}
