package com.adobecontest.android.apphub.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by brajesh on 15/11/14.
 */
public class RestClient {
    private static final String TAG = RestClient.class.getSimpleName();
    private static final boolean DEBUG = false;
    private String mUrl;
    private String mRequestMethod;

    public RestClient(String mUrl, String requestMethod) {
        this.mUrl = mUrl;
        this.mRequestMethod = requestMethod;
    }

    public String  getJSONData(){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;
        try {
            URL url = new URL(mUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(mRequestMethod);
            urlConnection.connect();
            // read input stream in stream
            InputStream input = urlConnection.getInputStream();
            if(null != input){
                reader = new BufferedReader(new InputStreamReader(input));
                String line;
                StringBuffer buffer = new StringBuffer();

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                    // add new line since there is no harm in adding this
                    // because our data is json
                    buffer.append("\n");
                }
                if(buffer.length() != 0){
                    jsonStr = buffer.toString();
                    if (DEBUG){
                        Log.d(TAG, jsonStr);
                    }
                }
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "ERROR", e);
        } catch (IOException e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "ERROR CLOSING STREAM", e);
                }
            }
        }

        return jsonStr;
    }

}
