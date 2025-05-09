package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legatoapp.Adapter.ProfileAdapter;
import com.example.legatoapp.models.Profile;
import com.example.legatoapp.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FollowersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<Profile> followersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);

        // BACK TO Profile BUTTON
        ImageButton backButton = findViewById(R.id.backToProfileFragment);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewFollowers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        followersList = new ArrayList<>();
        // ToDo: Replace this hardcoded examples from info from db or api
        followersList.add(new Profile("angelito", "angel.vitamins", R.drawable.profile10));
        followersList.add(new Profile("nate", "n8dog", R.drawable.profile11));
        followersList.add(new Profile("dino", "dinoatemyusername", R.drawable.profile12));

        adapter = new ProfileAdapter(followersList);
        recyclerView.setAdapter(adapter);
    }
}
