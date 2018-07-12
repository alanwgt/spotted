package com.alanwgt.spotted.controllers;

import com.alanwgt.spotted.auth.Authentication;
import com.alanwgt.spotted.models.Report;
import com.alanwgt.spotted.network.HttpRequest;
import com.alanwgt.spotted.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.alanwgt.spotted.controllers.HomeController.RECAPTCHA_SECRET;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/report")
    public void providesCreate(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(name = "post_id") String post_id,
            @RequestParam(name = "reason") String reason,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse) {
        String uid = Authentication.getUid(session);

        HttpRequest recaptchaRequest = new HttpRequest(HomeController.RECAPTCHA_ENDPOINT);
        String recaptchaRequestData = String.format("secret=%s&response=%s", RECAPTCHA_SECRET, recaptchaResponse);

        recaptchaRequest.doPost(recaptchaRequestData).subscribe(res -> {
            if (CaptchaValidator.validate(res)) {
                Report report = new Report();
                report.setReason(reason);
                report.setUid(uid);
                reportService.create(report, post_id);
            }

            HomeController.redirectToHome(response);
        });

    }

}
