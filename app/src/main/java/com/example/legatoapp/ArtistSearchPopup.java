package com.example.legatoapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
        this.artistList = new ArrayList<>(MusicData.getArtistNames()); // ✅ Fetch artist names from MusicData
        this.artistImages = MusicData.getArtistImageMap(); // ✅ Fetch artist images from MusicData
    }

    public void showPopup(View anchorView) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_song_search, null);

        searchInput = popupView.findViewById(R.id.search_input);
        recyclerView = popupView.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Pass both artist names and images to adapter
        artistAdapter = new ArtistAdapter(new ArrayList<>(artistList), artistImages, artist -> {
            if (listener != null) {
                listener.onArtistSelected(artist); // ✅ Pass correct artist name to CreateFragment
            }
            popupWindow.dismiss();
        });

        recyclerView.setAdapter(artistAdapter);

        // Configure PopupWindow to be centered
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Show popup in center of the screen
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        // Request focus and show keyboard when the popup appears
        searchInput.requestFocus();
        searchInput.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        // ✅ Filter artists as user types
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterArtists(s.toString()); // ✅ Keeps list updated in real time
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ✅ Hide keyboard on Enter key press while keeping filtering active
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard(v);
                filterArtists(searchInput.getText().toString()); // ✅ Reapply filtering after hiding keyboard
                return true;
            }
            return false;
        });
    }

    // ✅ Function to filter artists dynamically
    private void filterArtists(String query) {
        List<String> filteredList = new ArrayList<>();

        if (query.trim().isEmpty()) {
            filteredList.addAll(MusicData.getArtistNames()); // ✅ Restore full list if search is cleared
        } else {
            for (String artist : MusicData.getArtistNames()) {
                if (artist.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(artist);
                }
            }
        }

        artistAdapter.updateList(filteredList);
    }

    // ✅ Function to hide the keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
