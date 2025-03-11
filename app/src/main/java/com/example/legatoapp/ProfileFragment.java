package com.example.legatoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.legatoapp.Services.helper.SpotifyProfileDataHelper;
import com.example.legatoapp.models.response.SpotifyUserCurrentTrackResponse;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Button spotifyButton;
    private ImageView currentlyPlayingAlbumCover;
    private TextView currentlyPlayingArtist, currentlyPlayingSong;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        currentlyPlayingAlbumCover = view.findViewById(R.id.currentlyPlayingAlbumCover);
        currentlyPlayingArtist = view.findViewById(R.id.textViewCurrentlyPlayingArtist);
        currentlyPlayingSong = view.findViewById(R.id.textViewCurrentlyPlayingSong);
        spotifyButton = view.findViewById(R.id.buttonProfileSpotify);

        // Find the settings button by its ID
        ImageButton settingsButton = view.findViewById(R.id.settingsButton);

        // Set the OnClickListener for the settings button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to SettingsActivity
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        fetchLastSong();
        fetchProfile();
        return view;
    }

    private void fetchProfile(){
        Disposable disposable = SpotifyProfileDataHelper.fetchSpotifyUserProfile(requireContext())
                .subscribe(profile -> {
                    String spotifyProfileUrl = profile.getSpotify();
                    if (spotifyProfileUrl != null) {
                        spotifyButton.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyProfileUrl));
                            startActivity(intent);
                        });
                    }
                    else {
                        spotifyButton.setEnabled(false);
                        // fallback if for some reason the spotify URL retrieval isnt successful, we wont show the button to end-user
                    }
                }, error -> {
                    error.printStackTrace();
                });

        compositeDisposable.add(disposable);
    }

    private void fetchLastSong(){
        Disposable disposable = SpotifyProfileDataHelper.fetchSpotifyUserLastTrackPlayed(requireContext())
                .subscribe(response -> {
                    SpotifyUserCurrentTrackResponse.Item item = response.getLastPlayedTrack();

                    if(item != null){
                        currentlyPlayingSong.setText(item.getTrack().getName());
                        currentlyPlayingArtist.setText(item.getTrack().getFirstArtist());

                        String albumCoverUrl = item.getTrack().getAlbum().getAlbumImage();

                        if(albumCoverUrl != null){
                            Glide.with(requireContext())
                                    .load(albumCoverUrl)
                                    .into(currentlyPlayingAlbumCover);
                        }
                    }
                }, error ->{
                    error.printStackTrace();
                });
        compositeDisposable.add(disposable);
    }
}