package com.alanwgt.spotted;

import com.alanwgt.spotted.auth.Authentication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpottedApp {

    private static final String JSON_CONFIG_FILE = "spotted-209117-firebase-adminsdk-j77xl-7e98530d27.json";
    private static final String UID = "IZxiBPpzgVZSiV78fdeQdLadVVf1";
    
    private static void initFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(SpottedApp.class.getClassLoader().getResource(JSON_CONFIG_FILE).getFile());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://spotted-209117.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }

    @PostConstruct
    public void onApplicationEvent() {
        try {
            initFirebase();
            setFirebaseCustomClaims();

//            new Thread(() -> {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                setFirebaseCustomClaims();
//            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setFirebaseCustomClaims() {
        UserRecord record = Authentication.getUserRecord(UID);

        if (record == null) {
            System.err.println("COULDN'T FIND USER RECORD!");
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
    }

}
