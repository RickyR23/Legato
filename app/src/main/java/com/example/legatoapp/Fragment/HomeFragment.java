package com.example.legatoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
                } else {
                    recyclerViewProfiles.setVisibility(View.VISIBLE);
                    profileAdapter.filter(s.toString());
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // === Hide profile search on outside touch ===
        view.setOnTouchListener((v, event) -> {
            if (searchBar.isFocused()) {
                searchBar.clearFocus();
                recyclerViewProfiles.setVisibility(View.GONE);
                hideKeyboard();
            }
            return false;
        });

        return view;
    }

    private void hideKeyboard() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
