package com.adobecontest.android.apphub.helper;

import android.net.Uri;

import java.util.Map;

/**
 * Created by brajesh on 16/11/14.
 */
public class NetworkHelper {
    // making it private so the that it cant be instantiated
    private NetworkHelper(){}

    public static String makeUrl(String baseUrl, Map<String , String > params){
        Uri.Builder uriBuilder = Uri.parse(baseUrl).buildUpon();
        if(params != null ){
            for(Map.Entry<String, String> entry : params.entrySet()){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder.build().toString();
    }
}
