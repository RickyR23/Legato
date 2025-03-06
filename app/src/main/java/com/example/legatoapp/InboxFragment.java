package com.example.legatoapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample notifications with commentNotif
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem(R.drawable.ic_user, "Alex", "has followed you.", "2h ago", "followNotif"));
        notificationList.add(new NotificationItem(R.drawable.ic_user, "Jordan", "has liked your daily post!", "1h ago", "likeNotif"));
        notificationList.add(new NotificationItem(R.drawable.ic_user, "Emily", "has left a comment on your daily post!", "30m ago", "commentNotif"));
        notificationList.add(new NotificationItem(R.drawable.ic_user, "Chris", "has posted their daily song!", "5m ago", "postNotif"));
        notificationList.add(new NotificationItem(R.drawable.ic_settings, "Legato System", "System maintenance update", "1d ago", "systemNotif"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
