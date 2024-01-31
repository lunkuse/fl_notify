package com.example.lastnotification.services;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface service {

    @POST("/api/notification/send/")
    Call<ResponseBody> sendToken(@Body Map<String, String> tokenData);
}