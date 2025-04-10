package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legatoapp.Adapter.FAQAdapter;
import com.example.legatoapp.models.FAQItem;
import com.example.legatoapp.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FAQAdapter adapter;
    private List<FAQItem> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // BACK TO SETTINGS BUTTON
        ImageButton backButton = findViewById(R.id.backToSettings);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewFaq);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        faqList = new ArrayList<>();
        faqList.add(new FAQItem("How do I change my display name?", "Go to your profile, click the pencil button to edit your profile, enter your new display name and click 'Save' to save changes made."));
        faqList.add(new FAQItem("How can I create a post?", "Simply go to the 'Create Post' page, click the search bar to find the song you would like to post, add a caption and click 'Post' when done."));
        faqList.add(new FAQItem("Is it possible to change my Notification settings?", "Yes, simply go to the Settings page and click the toggle button next to 'Notifications'."));
        faqList.add(new FAQItem("Can I change my top songs?", "Yes, go to 'Edit Profile', there you will be able to clear your current selection for Top Songs, search and select new ones. Once done, click 'Save' " +
                "to save any changes made."));
        // ToDo: Add more FAQs we come up with

        adapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(adapter);

    }
}
