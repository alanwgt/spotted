package com.alanwgt.spotted.controllers;

import com.alanwgt.spotted.models.CaptchaResponse;
import com.google.gson.Gson;

public class CaptchaValidator {

    private static Gson gson = new Gson();

    public static Boolean validate(String response) {
        CaptchaResponse captchaResponse;

        try {
            captchaResponse = gson.fromJson(response, CaptchaResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("RECEIVED CAPTCHA:");
        System.out.println(captchaResponse.toString());

        return captchaResponse.getSuccess();
    }

}
