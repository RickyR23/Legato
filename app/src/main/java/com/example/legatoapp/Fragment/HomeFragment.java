package com.example.legatoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.R;
import com.example.legatoapp.models.Profile;
import com.example.legatoapp.Adapter.ProfileAdapter;
import com.example.legatoapp.Adapter.PostAdapter;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts, recyclerViewProfiles;
    private PostAdapter postAdapter;
    private ProfileAdapter profileAdapter;
    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recyclerView_posts);
        recyclerViewProfiles = view.findViewById(R.id.recyclerView_profiles);
        searchBar = view.findViewById(R.id.search_bar);

        FrameLayout rootLayout = view.findViewById(R.id.home_fragment_container);

        View touchOverlay = view.findViewById(R.id.touch_overlay);

        touchOverlay.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerViewProfiles.setVisibility(View.GONE);
                searchBar.setText(""); // <-- clear search input
                searchBar.clearFocus();
                hideKeyboard(searchBar);
                touchOverlay.setVisibility(View.GONE);
                return true;
            }
            return false;
        });



        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && recyclerViewProfiles.getVisibility() == View.VISIBLE) {
                // Check if touch is outside the RecyclerView bounds
                if (!isTouchInsideView(event, recyclerViewProfiles) && !isTouchInsideView(event, searchBar)) {
                    recyclerViewProfiles.setVisibility(View.GONE);
                    searchBar.clearFocus();
                    hideKeyboard(view);
                }
            }
            v.performClick();
            return false;
        });




        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                searchBar.clearFocus(); // <-- This line removes the blinking cursor
                hideKeyboard(v);
                return true;
            }
            return false;
        });






        // === Post feed setup ===
        List<String> captions = Arrays.asList("Caption 1", "Caption 2", "Caption 3");
        postAdapter = new PostAdapter(captions); // item_post.xml is used for each item
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPosts.setAdapter(postAdapter);

        // === Profile search setup ===
        List<Profile> profiles = Arrays.asList(
                new Profile("Alice Smith", "alice123", R.drawable.angelespfp),
                new Profile("Bob Johnson", "bobbyj", R.drawable.artist1),
                new Profile("Carla Diaz", "cdiaz", R.drawable.artist3)
        );
        profileAdapter = new ProfileAdapter(profiles);
        recyclerViewProfiles.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProfiles.setAdapter(profileAdapter);
        recyclerViewProfiles.setVisibility(View.GONE); // Start hidden

        // === Search bar typing logic ===
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    recyclerViewProfiles.setVisibility(View.GONE);
                    touchOverlay.setVisibility(View.GONE);
                } else {
                    recyclerViewProfiles.setVisibility(View.VISIBLE);
                    touchOverlay.setVisibility(View.VISIBLE);
                    profileAdapter.filter(s.toString());
                }
            }


            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private boolean isTouchInsideView(MotionEvent event, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        float x = event.getRawX();
        float y = event.getRawY();

        return x >= left && x <= right && y >= top && y <= bottom;
    }


}
