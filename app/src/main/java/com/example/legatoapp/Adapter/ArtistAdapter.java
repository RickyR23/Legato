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
import java.util.Map;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<String> artistList;
    private Map<String, Integer> artistImages;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String artist); // Ensure it takes a String artist name
    }

    public ArtistAdapter(List<String> artistList, Map<String, Integer> artistImages, OnItemClickListener listener) {
        this.artistList = artistList;
        this.artistImages = artistImages;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        String artist = artistList.get(position);
        holder.artistName.setText(artist);

        // Fetch correct artist image
        if (artistImages.containsKey(artist)) {
            holder.artistImage.setImageResource(artistImages.get(artist));
        } else {
            holder.artistImage.setImageResource(R.drawable.angelespfp);
        }

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(artist); // Passes artist name correctly
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public void updateList(List<String> newList) {
        artistList.clear();
        artistList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView artistImage;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            artistImage = itemView.findViewById(R.id.artist_image);
        }
    }
}
