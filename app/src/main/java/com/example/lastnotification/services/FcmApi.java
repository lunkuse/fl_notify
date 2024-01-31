package com.example.lastnotification.services;


import com.example.lastnotification.model.contract;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FcmApi {
String  token = "test"
;//    @POST("/api/notification/send/")
//Call<contract> sendToken(@Body Map<String, String> tokenData);
//    @POST("api/notification/send/")
   @GET("api/notification/send/{token}")
   Call<ResponseBody> sendToken(@Path("token") String tokenData);
//    @Headers({"Content-Type: application/json"})
//    Call<ResponseBody> sendToken(@Body contract tokenData);
}
//    @POST("https://notify.hmvtechgroup.com//api/notification/send/")
//@POST("/api/notification/send/")
//    Call<ResponseBody> sendToken(@Body Map<String, String> tokenData);
//}
//@POST("/api/notification/send/")
//Call<contract> sendToken(@Body contract tokenData);

//    @GET("user/")
//    Call<List<contract>> getUsers();
//}


//    @POST("add/")
//    Call<User> addUser(@Body User user);
