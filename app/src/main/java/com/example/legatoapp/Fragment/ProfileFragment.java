package com.example.legatoapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.animation.ObjectAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.legatoapp.Activity.EditProfileActivity;
import com.example.legatoapp.Activity.FollowersListActivity;
import com.example.legatoapp.Activity.FollowingListActivity;
import com.example.legatoapp.Activity.SettingsActivity;
import com.example.legatoapp.R;
import com.example.legatoapp.Services.helper.SpotifyProfileDataHelper;
import com.example.legatoapp.models.response.SpotifyUserCurrentTrackResponse;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private CircleImageView profilePicImageView;

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
        profilePicImageView = view.findViewById(R.id.profilePicImageView);

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

        // Find the edit profile button by its ID
        ImageButton editProfileButton = view.findViewById(R.id.editProfileButton);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to EditProfileActivity
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Zoom-in and out animation whenever the user clicks the profile picture
        profilePicImageView.setOnClickListener(new View.OnClickListener() {
            private boolean isZoomedIn = false;

            @Override
            public void onClick(View view) {
                // Toggle zoom state
                isZoomedIn = !isZoomedIn;

                // Define zoom scale factor
                float scale = isZoomedIn ? 2.5f : 1f;

                // Set pivot point to the center of the screen
                profilePicImageView.setPivotX(profilePicImageView.getWidth() / 2f);
                profilePicImageView.setPivotY(profilePicImageView.getHeight() / 2.5f);

                // Create animations for scaling
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(profilePicImageView, "scaleX", scale);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(profilePicImageView, "scaleY", scale);

                // Set animation duration
                scaleX.setDuration(200);
                scaleY.setDuration(200);

                // Start animations
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.start();
            }
        });

        // Follower and following
        LinearLayout followersContainer = view.findViewById(R.id.followersContainer);
        LinearLayout followingContainer = view.findViewById(R.id.followingContainer);

        // OnClickListener when followers is clicked
        followersContainer.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), FollowersListActivity.class);
            startActivity(intent);
        });

        // OnClickListener when following is clicked
        followingContainer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FollowingListActivity.class);
            startActivity(intent);
        });

        fetchLastSong();
        fetchProfile();
        return view;
    }

    // Refresh profile data whenever the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        loadProfileData();
    }

    //*******METHODS*******//
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

    private void loadProfileData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LegatoPrefs", MODE_PRIVATE);

        //Retrieve display name and bio from shared preferences
        String displayName = sharedPreferences.getString("saved_display_name", "Default Name");
        String bio = sharedPreferences.getString("saved_bio", "Add a bio");

        //Retrieve profile pic
        String profilePic = sharedPreferences.getString("saved_profile_pic",null);

        if (profilePic != null) {
            Uri imageUri = Uri.parse(profilePic);
            profilePicImageView.setImageURI(imageUri);
        }

        // Arrays for song data, artist data and UI elements
        String[] songTitles = new String[3];
        String[] songArtists = new String[3];
        String[] artistNames = new String[3];
        int[] songTitleViews = {R.id.song1Title, R.id.song2Title, R.id.song3Title};
        int[] songArtistViews = {R.id.song1Artist, R.id.song2Artist, R.id.song3Artist};
        int[] artistNameViews = {R.id.artist1Name, R.id.artist2Name, R.id.artist3Name};

        // Retrieve saved song data and artist
        for (int i = 0; i < 3; i++) {
            songTitles[i] = sharedPreferences.getString("saved_song_" + (i + 1), "Select a song");
            songArtists[i] = sharedPreferences.getString("saved_song_" + (i + 1) + "_artist", "");
            artistNames[i] = sharedPreferences.getString("saved_artist_" + (i + 1), "Select an artist");
        }

        //Update UI elements with retrieved data
        TextView displayNameTextView = getView().findViewById(R.id.textViewDisplayName);
        TextView bioTextView = getView().findViewById(R.id.textViewBio);

        displayNameTextView.setText(displayName);
        bioTextView.setText(bio);

        // Update the song and artist UI elements with data saved
        for (int i = 0; i < 3; i++) {
            TextView songTitleView = getView().findViewById(songTitleViews[i]);
            TextView songArtistView = getView().findViewById(songArtistViews[i]);
            TextView artistNameView = getView().findViewById(artistNameViews[i]);

            songTitleView.setText(songTitles[i]);
            songArtistView.setText(songArtists[i]);
            artistNameView.setText(artistNames[i]);
        }
    }

}