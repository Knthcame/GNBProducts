package com.gnb.products.services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {
    private static HttpService INSTANCE;

    private static final String TAG = "HttpService";

    private HttpService(){
    }

    public static HttpService getInstance() {
        if(INSTANCE == null){
            synchronized (HttpService.class){
                INSTANCE = new HttpService();
            }
        }
        return INSTANCE;
    }

    public String Get(URL url) {
        String response;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "Application/json");

            Log.d(TAG, "Sending a GET request to " + url);

            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine())!= null){
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            response = null;
        }

        return response;
    }
}
