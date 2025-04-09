package com.example.legatoapp.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.R;
import com.example.legatoapp.models.Profile;
import com.example.legatoapp.Adapter.ProfileAdapter;


import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_profiles);
        searchBar = view.findViewById(R.id.search_bar);

        List<Profile> profiles = Arrays.asList(
                new Profile("Alice Smith", "alice123", R.drawable.angelespfp),
                new Profile("Bob Johnson", "bobbyj", R.drawable.artist1),
                new Profile("Carla Diaz", "cdiaz", R.drawable.artist3)
        );

        adapter = new ProfileAdapter(profiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
}
