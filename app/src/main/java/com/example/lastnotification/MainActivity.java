package com.example.lastnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
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
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
//
        recyclerView = findViewById(R.id.notifications_recycler_view);
        // Initialize the SearchView
        // Assuming you have a SearchView in your layout with the id searchView
        SearchView searchView = findViewById(R.id.searchView);

// Set up a listener for text changes in the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Not needed for live search, handle if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Check if the search query is empty
                if (newText.isEmpty()) {
                    // Load all notifications if the search query is empty
                    getNotifications("");
                } else {
                    // Call the getNotifications method with the new search query
                    getNotifications(newText);
                }
                return true;
            }
        });

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            token = task.getException().getMessage();
//
//                            Log.w("FCM TOKEN Failed", task.getException());
//                        } else {
//                            token = task.getResult();
//                            Log.i("FCM TOKEN mmmmmmmmmm", token);
//                            if (token != null && !token.isEmpty()) { // Check for both null and empty string
//                                onlySendNotification();
//                            } else {
//                                // Handle the case where the token is null or empty
//                                Log.e("Error", "Token is null or empty");
//                                // Consider displaying a user-friendly message or taking alternative actions
//                            }
//                        }
//                    }
//                });
        getFirebaseToken();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.notifications_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        notificationAdapter = new NotificationAdapter(notifications);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(notificationAdapter);
//        getNotifications();
        getNotifications("");



    }
    //get device token
    public void getFirebaseToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            token = task.getException().getMessage();

                            Log.w("FCM TOKEN Failed", task.getException());
                        } else {
                            token = task.getResult();
                            Log.i("Got token FCM TOKEN mmmmmmmmmm", token);
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
    public void getNotifications1(){
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

                    System.out.println("notifyList"+notifications);
                    if (notificationAdapter != null) {
                        System.out.println("notifyList"+notifications);
                        notificationAdapter.setNotifications(notifications);
                    } else {
                        Log.e("Error", "NotificationAdapter is null");
                        // Display a user-friendly error message if needed
                    }
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

    public void getNotifications(String searchQuery) {
        String url = "https://notify.hmvtechgroup.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an API service instance
        FcmApi apiService = retrofit.create(FcmApi.class);

        apiService.getNotifications().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.isSuccessful()) {
                    List<NotificationModel> allNotifications = response.body();

                    // Filter notifications based on the search query
                    List<NotificationModel> filteredNotifications = filterNotifications(allNotifications, searchQuery);

                    if (notificationAdapter != null) {
                        notificationAdapter.setNotifications(filteredNotifications);
                    } else {
                        Log.e("Error", "NotificationAdapter is null");
                        // Display a user-friendly error message if needed
                    }
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
    }

    private List<NotificationModel> filterNotifications(List<NotificationModel> allNotifications, String query) {
        List<NotificationModel> filteredList = new ArrayList<>();

        if (allNotifications != null && !allNotifications.isEmpty()) {
            for (NotificationModel notification : allNotifications) {
                // Check if the contract name or log details contains the search query (case-insensitive)
                if ((notification.getContractName() != null && notification.getContractName().toLowerCase().contains(query.toLowerCase())) ||
                        (notification.getLogDetails() != null && notification.getLogDetails().toLowerCase().contains(query.toLowerCase()))) {
                    filteredList.add(notification);
                }
            }
        }

        return filteredList;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the list when the activity resumes
        if (notificationAdapter != null) {
            notificationAdapter.notifyDataSetChanged();
        }
    }
    // Method to filter notifications based on the search query
    private void filterNotifications(String query) {
        List<NotificationModel> filteredList = new ArrayList<>();

        if (notifications != null && notificationAdapter != null) {
            for (NotificationModel notification : notifications) {
                // Ensure that notification and getContractName() are not null
                if (notification != null && notification.getContractName() != null &&
                        notification.getContractName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(notification);
                }
            }

            // Update the RecyclerView with the filtered list
            notificationAdapter.setNotifications(filteredList);
        }
    }


}