package com.cloud.license.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * File: HttpUtils.java
 * Author: Landy
 * Create: 2019/7/11 10:08
 */
public class HttpUtils {

    public static String httpGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
