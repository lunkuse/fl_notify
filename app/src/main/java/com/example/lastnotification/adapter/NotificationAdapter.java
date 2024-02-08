package com.example.lastnotification.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lastnotification.R;
import com.example.lastnotification.model.NotificationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private List<NotificationModel> notifications;

    public NotificationAdapter(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationModel notification = notifications.get(position);
        holder.contractNameTextView.setText(notification.getContractName());
        holder.logDetailsTextView.setText(notification.getLogDetails());

        holder.logDateTextView.setText(formatDate(notification.getCreatedAt()));
        String logDetails = notification.getContractName();
        if (logDetails.length() >= 2) {
            String capitalizedLastTwo = logDetails.substring(logDetails.length() - 2).toUpperCase(); // Capitalize here
            holder.avatarTextView.setText(capitalizedLastTwo);
//            holder.avatarTextView.setBackgroundColor(getRandomColor());
        } else {
            // Handle cases where logDetails is too short (e.g., set a placeholder)
            holder.avatarTextView.setText("FL");
        }


    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }
    private String formatDate(String dateString) {
        try {
            // Assuming the date is in ISO 8601 UTC format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = inputFormat.parse(dateString);

            // Format the date to dd/mm/yyyy
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Handle parsing errors gracefully, e.g., log the error and return a placeholder
            e.printStackTrace();
            return "Invalid date format";
        }
    }

//    private int getRandomColor() {
//        Random random = new Random();
//        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//    }
private int getRandomColor() {
    Random random = new Random();

    // Generate a random angle between 0 and 360 degrees
    double angle = random.nextDouble() * 360;

    // Convert angle to radians
    double radians = Math.toRadians(angle);

    // Calculate color components based on sine and cosine functions
    int red = (int) (Math.sin(radians) * 127 + 128);
    int green = (int) (Math.cos(radians) * 127 + 128);
    int blue = (int) (Math.sin(radians + Math.PI / 2) * 127 + 128);

    return Color.rgb(red, green, blue);
}
}

