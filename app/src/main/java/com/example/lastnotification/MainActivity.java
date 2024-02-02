package com.example.lastnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lastnotification.adapter.NotificationAdapter;
import com.example.lastnotification.model.NotificationModel;
import com.example.lastnotification.model.contract;
import com.example.lastnotification.services.FcmApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String AUTH_KEY = "AAAAT9sYIVQ:APA91bGEklNjcnnNV3NXGAd2nespUzBSs8c_LpsqA0cAmFFoRKjeCdthQj2d0iZRNyAxmZJ2Nk474qkovajOKxIlkQIm6MEoy0mVTO3Jb1uwWPMUbEo4KC4x08tJrBuyN1zhtAasJdQ6";
    private TextView mTextView;
    private String token;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    private ListView listView;


    private List<NotificationModel> notifications = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        // If the permission is not granted, request it.
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        listView = findViewById(R.id.listviewData);

//        List<NotificationModel> notifications = [];

//        NotificationModel notifs =[];
//
//        recyclerView = findViewById(R.id.notifications_recycler_view);
//        notificationAdapter = new NotificationAdapter(NotificationModel[]);
//        listView.setAdapter(new ArrayAdapter < String > (getApplicationContext(), android.R.layout.simple_list_item_1, names));
//        recyclerView.setAdapter(notificationAdapter); // Bind the adapter to the RecyclerView

//        mTextView = findViewById(R.id.txt);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String tmp = "";
//            for (String key : bundle.keySet()) {
//                Object value = bundle.get(key);
//                tmp += key + ": " + value + "\n\n";
//            }
////            mTextView.setText(tmp);
//        }



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            token = task.getException().getMessage();

                            Log.w("FCM TOKEN Failed", task.getException());
                        } else {
                            token = task.getResult();
                            Log.i("FCM TOKEN mmmmmmmmmm", token);
                            if (token != null && !token.isEmpty()) { // Check for both null and empty string
                                onlySendNotification();
                            } else {
                                // Handle the case where the token is null or empty
                                Log.e("Error", "Token is null or empty");
                                // Consider displaying a user-friendly message or taking alternative actions
                            }
                        }
                    }
                });



        getNotifications();



    }
//sending device token
    public void onlySendNotification() {
 Log.i("Sending FCM TOKEN mmmmmmmmmm", token);
        System.out.println("last my data to send: " + token);
 String url = "https://notify.hmvtechgroup.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an API service instance
        FcmApi apiService = retrofit.create(FcmApi.class);

        contract contract =  new contract(1,"lorem");

        contract.setToken(token);
        // Create a map to hold the token data
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", token);
        System.out.println("my data to send: " + tokenData);
        // Make the API call
        apiService.sendToken(token).enqueue(new Callback<ResponseBody>() {



            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.i("API Response", responseBody);
                        // Handle successful response with the parsed object (responseBody)
                        // Parse JSON (if applicable) and extract relevant information
                    } catch (IOException e) {
                        Log.e("API Error", e.getMessage());
                    }
                } else {
                    Log.e("API Error", response.code() + " - " + response);
                    // Handle API error based on HTTP code and message
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Log.e("API Exact Error", t.toString());
                // Handle network error (e.g., no internet connection)
            }
        });
    }
    public void getNotifications(){
        String url = "https://notify.hmvtechgroup.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an API service instance
        FcmApi apiService = retrofit.create(FcmApi.class);
//        System.out.println("API Response at any time"+ token);
        apiService.getNotifications().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
//                Log.i("last API Response at any time", response.body());
                if (response.isSuccessful()) {

                    List<NotificationModel> notifications = response.body();

                    System.out.println("notifyiList"+notifications);
                    if(notifications !=null){
//                        new NotificationAdapter(notifications);
                        listView.setAdapter(new ArrayAdapter < NotificationModel > (getApplicationContext(), android.R.layout.simple_list_item_1, notifications));







                    }else{
                        Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }


//                    recyclerView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.notification_item,notifications));
//                    if (notificationAdapter != null) {
//
//                        notificationAdapter.setNotifications(notifications);
//                    } else {
//                        Log.e("Error", "NotificationAdapter is null");
//                        // Display a user-friendly error message if needed
//                    }
                } else {
                    // Handle error
                    Toast.makeText(MainActivity.this, "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                // Handle network error
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
//
//        apiService.getNotifications().enqueue(new Callback<List<NotificationModel>>() {
//            @Override
//            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
//                if (response.isSuccessful()) {
//                    List<NotificationModel> notifications = response.body();
//                    notificationAdapter.setNotifications(notifications);
//
//                    // Ensure ScrollView is used for scrolling
//                    ScrollView scrollView = findViewById(R.id.your_scroll_view); // Assuming you have a ScrollView with the ID "your_scroll_view"
//                    scrollView.setAdapter(notificationAdapter);
//                } else {
//                    // Handle error
//                }
//            }
//
//            // ... (rest of the callback code as provided in the previous response)
//        });
    }


}