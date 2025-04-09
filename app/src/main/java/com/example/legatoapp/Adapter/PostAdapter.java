package com.example.legatoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<String> posts;

    public PostAdapter(List<String> posts) {
        this.posts = posts;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView legatoBackground, albumCover;
        TextView usernameText, captionText, musicText;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            legatoBackground = itemView.findViewById(R.id.post_legato_background);
            albumCover = itemView.findViewById(R.id.album_cover);
            usernameText = itemView.findViewById(R.id.username_text);
            captionText = itemView.findViewById(R.id.caption_text);
            musicText = itemView.findViewById(R.id.music_text);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        view.getLayoutParams().height = parent.getHeight(); // fullscreen swipe
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String post = posts.get(position);
        holder.usernameText.setText("@user" + (position + 1));
        holder.captionText.setText(post);
        holder.musicText.setText("🎵 Track " + (position + 1));
        holder.albumCover.setImageResource(R.drawable.song_example); // Replace with dynamic later
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
