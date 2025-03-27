package com.example.legatoapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.Adapter.ArtistAdapter;
import com.example.legatoapp.models.MusicData;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ArtistSearchPopup {

    private Context context;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private ArtistAdapter artistAdapter;
    private List<String> artistList;
    private Map<String, Integer> artistImages;
    private OnArtistSelectedListener listener;

    public interface OnArtistSelectedListener {
        void onArtistSelected(String artist);
    }

    public ArtistSearchPopup(Context context, OnArtistSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.artistList = new ArrayList<>(MusicData.getArtistNames());
        this.artistImages = MusicData.getArtistImageMap();
    }

    public void showPopup(View anchorView) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_song_search, null);

        searchInput = popupView.findViewById(R.id.search_input);
        recyclerView = popupView.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        artistAdapter = new ArtistAdapter(new ArrayList<>(artistList), artistImages, artist -> {
            if (listener != null) {
                listener.onArtistSelected(artist);
            }
            popupWindow.dismiss();
        });

        recyclerView.setAdapter(artistAdapter);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }
}
