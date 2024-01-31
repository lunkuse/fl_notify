package com.example.lastnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.txt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
            }
            mTextView.setText(tmp);
        }

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (!task.isSuccessful()) {
//                    token = task.getException().getMessage();
//                    Log.w("FCM TOKEN Failed", task.getException());
//                } else {
//                    token = task.getResult().getToken();
//                    Log.i("FCM TOKEN", token);
//                }
//            }
//        });

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
                        }
                    }
                });
    }



    public void showToken(View view) {
        mTextView.setText(token);
    }











    public void sendNotification(View view) {



        // 1. Capture EditText values
        EditText contractNameEditText = findViewById(R.id.contract_name_edit_text); // Replace with actual ID
        EditText detailsEditText = findViewById(R.id.details_edit_text); // Replace with actual ID

        String contractName = contractNameEditText.getText().toString();
        String details = detailsEditText.getText().toString();
        // Check for empty fields
        if (contractName.isEmpty()) {
            contractNameEditText.setError("Contract name is required");
            return; // Prevent further execution if invalid
        }

        if (details.isEmpty()) {
            detailsEditText.setError("Details are required");
            return;
        }
        Log.i("Sending FCM TOKEN mmmmmmmmmm", token);
        Log.i("Sending this contractName captured data", contractName);
        Log.i("Sending this details captured data", details );
        // Create a Retrofit instance
//        Retrofit retrofit = new Retrofit.Builder()
//          .baseUrl("https://notify.hmvtechgroup.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();


String url = "https://notify.hmvtechgroup.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an API service instance
        FcmApi apiService = retrofit.create(FcmApi.class);

        contract contract =  new contract(1,"lorem");
        contract.setDetails(details);
        contract.setToken(token);
        // Create a map to hold the token data
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("contract_name", contractName);
        tokenData.put("details", details);
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

    public void subscribe(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        mTextView.setText(R.string.subscribed);
    }

    public void unsubscribe(View view) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
        mTextView.setText(R.string.unsubscribed);
    }

    public void sendToken(View view) {
        sendWithOtherThread("token");
    }

    public void sendTokens(View view) {
        sendWithOtherThread("tokens");
    }

    public void sendTopic(View view) {
        sendWithOtherThread("topic");
    }

    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_notification");

            jData.put("picture", "https://miro.medium.com/max/1400/1*QyVPcBbT_jENl8TGblk52w.png");

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
                    ja.put(token);
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/news");
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", token);
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

//            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            URL url = new URL("https://fcm.googleapis.com/v1/lastnotification/messages:send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}