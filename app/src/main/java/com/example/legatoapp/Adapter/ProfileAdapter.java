package com.example.legatoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.R;
import com.example.legatoapp.models.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> originalList;
    private List<Profile> filteredList;

    public ProfileAdapter(List<Profile> profileList) {
        this.originalList = new ArrayList<>(profileList);
        this.filteredList = profileList;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView displayName, username;

        public ProfileViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.profile_image);
            displayName = view.findViewById(R.id.display_name);
            username = view.findViewById(R.id.username);
        }
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Profile profile = filteredList.get(position);
        holder.displayName.setText(profile.getDisplayName());
        holder.username.setText("@" + profile.getUsername());
        holder.profileImage.setImageResource(profile.getProfileImageResId());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList = originalList.stream()
                .filter(p -> p.getDisplayName().toLowerCase().contains(query.toLowerCase()) ||
                        p.getUsername().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }
}
