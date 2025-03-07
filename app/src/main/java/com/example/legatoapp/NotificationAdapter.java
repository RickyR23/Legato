package com.example.legatoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);
        holder.profileImage.setImageResource(notification.getProfileImage());
        holder.username.setText(notification.getUsername());
        holder.message.setText(notification.getMessage());
        holder.timestamp.setText(notification.getTimestamp());

        // Set notification icon based on type
        int iconRes = 0;
        switch (notification.getNotificationType()) {
            case "likeNotif":
                iconRes = R.drawable.ic_heart;
                break;
            case "commentNotif":
                iconRes = R.drawable.ic_comment;
                break;
            case "followNotif":
                iconRes = R.drawable.ic_follow;
                break;
            case "postNotif":
                iconRes = R.drawable.ic_music_note;
                break;
            case "systemNotif":
                iconRes = R.drawable.notifications_settings;
                break;
            default:
                holder.notificationIcon.setVisibility(View.GONE);
                return; // Exit early if no valid icon is found
        }

        // Apply the icon and make it visible
        holder.notificationIcon.setImageResource(iconRes);
        holder.notificationIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, notificationIcon;
        TextView username, message, timestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.notificationMessage);
            timestamp = itemView.findViewById(R.id.timestamp);
            notificationIcon = itemView.findViewById(R.id.notificationIcon);
        }
    }
}
