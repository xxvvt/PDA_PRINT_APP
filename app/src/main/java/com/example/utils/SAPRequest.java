package com.example.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SAPRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "SAPRequest";

    private String requestUrl;
    private String jsonInputString;

    public SAPRequest(String requestUrl, String jsonInputString) {
        this.requestUrl = requestUrl;
        this.jsonInputString = jsonInputString;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonInputString);

            os.flush();
            os.close();

            Log.i(TAG, "Response Code: " + conn.getResponseCode());
            Log.i(TAG, "Response Message: " + conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
