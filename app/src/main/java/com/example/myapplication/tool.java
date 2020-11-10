package com.example.myapplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class tool {


    public static String HttpGET(String URL) throws Exception {
        InputStream inputStream = null;
        java.net.URL url = new URL(URL);
        if (URL.toLowerCase().startsWith("https")) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            inputStream = httpsURLConnection.getInputStream();
        } else {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\r\n");
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }
}
