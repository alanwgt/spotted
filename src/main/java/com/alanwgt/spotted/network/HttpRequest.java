package com.alanwgt.spotted.network;

import io.reactivex.Single;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpRequest {

    private final URL url;
    private String contentType = "application/x-www-form-urlencoded; charset=UTF-8";

    public HttpRequest(URL url) {
        this.url = url;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Single<String> doPost(String data) {
        return Single.create(subscriber -> {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty( "Content-Type", contentType);

//            String encodedData = URLEncoder.encode(data, "UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            connection.disconnect();
            subscriber.onSuccess(content.toString());
        });
    }

}
