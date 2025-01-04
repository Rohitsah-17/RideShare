package com.android.ridesharing.Helper;

import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;

public class FCMNotificationSender {

    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/rideshare2-8d86c/messages:send";


    public static void sendNotification(final String deviceToken, final String title, final String body) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String accessToken = getAccessToken();

                    // Construct the JSON payload
                    String jsonPayload = "{"
                            + "\"message\": {"
                            + "\"token\": \"" + deviceToken + "\","
                            + "\"notification\": {"
                            + "\"title\": \"" + title + "\","
                            + "\"body\": \"" + body + "\""
                            + "}"
                            + "}"
                            + "}";

                    OkHttpClient client = new OkHttpClient();

                    RequestBody bodyRequest = RequestBody.create(
                            jsonPayload,
                            MediaType.parse("application/json; charset=utf-8")
                    );


                    Request request = new Request.Builder()
                            .url(FCM_URL)
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .addHeader("Content-Type", "application/json")
                            .post(bodyRequest)
                            .build();


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                System.out.println("Notification sent successfully: " + response.body().string() + "device token : " + deviceToken);
                            } else {
                                System.err.println("Failed to send notification: " + response.body().string());
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static String getAccessToken() throws IOException {
        try (InputStream serviceAccountStream = FCMNotificationSender.class.getClassLoader().getResourceAsStream("accesstoken.json")) {
            if (serviceAccountStream == null) {
                throw new IOException("Service account file not found in resources folder.");
            }

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging");

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        }
    }
}