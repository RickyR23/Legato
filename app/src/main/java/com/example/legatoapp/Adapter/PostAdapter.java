package com.example.legatoapp.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        ImageView likeIcon, commentIcon, spotifyIcon;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            legatoBackground = itemView.findViewById(R.id.post_legato_background);
            albumCover = itemView.findViewById(R.id.album_cover);
            usernameText = itemView.findViewById(R.id.username_text);
            captionText = itemView.findViewById(R.id.caption_text);
            musicText = itemView.findViewById(R.id.music_text);

            likeIcon = itemView.findViewById(R.id.like_icon);
            commentIcon = itemView.findViewById(R.id.comment_icon);
            spotifyIcon = itemView.findViewById(R.id.spotify_icon);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String post = posts.get(position);
        holder.usernameText.setText("@user" + (position + 1));
        holder.captionText.setText(post);
        holder.musicText.setText("🎵 Track " + (position + 1));
        holder.albumCover.setImageResource(R.drawable.song_example); // Replace with dynamic later

        // === CLICK HANDLERS ===
        // Set initial tint ON
        holder.likeIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        holder.likeIcon.setTag(true); // true = tint is currently ON

        holder.likeIcon.setOnClickListener(v -> {
            boolean isTinted = (boolean) holder.likeIcon.getTag();

            if (isTinted) {
                // Turn tint OFF
                //COLOR RED
                holder.likeIcon.setImageTintList(null);
            } else {
                // Turn tint ON
                //BACK TO WHITE
                holder.likeIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            }

            // Toggle state
            holder.likeIcon.setTag(!isTinted);
        });



        holder.commentIcon.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            android.content.Intent intent = new android.content.Intent(context, com.example.legatoapp.Activity.CommentsActivity.class);
            context.startActivity(intent);
        });


        holder.spotifyIcon.setOnClickListener(v -> {
            // Open Spotify
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
