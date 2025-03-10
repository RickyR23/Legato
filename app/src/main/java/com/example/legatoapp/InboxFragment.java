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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample notifications
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem(R.drawable.notifcation_pfp, "Angel", "has followed you.", "5m ago", "followNotif"));
        notificationList.add(new NotificationItem(R.drawable.angelespfp, "Angeles", "has liked your daily post!", "30m ago", "likeNotif"));
        notificationList.add(new NotificationItem(R.drawable.snoopypfp, "Snoopy", "has left a comment on your daily post!", "2h ago", "commentNotif"));
        notificationList.add(new NotificationItem(R.drawable.rickypfp, "Ricky", "has posted their daily song!", "5h ago", "postNotif"));
        notificationList.add(new NotificationItem(R.drawable.legato_logo, "Legato System", "System maintenance update", "10h ago", "systemNotif"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
