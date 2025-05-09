package com.example.legatoapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
        setCustomCurrentlyPlaying();
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

    private void setCustomCurrentlyPlaying() {
        currentlyPlayingSong.setText("Come on Over");
        currentlyPlayingArtist.setText("Royal Blood");

        currentlyPlayingAlbumCover.setImageResource(R.drawable.currently_example);
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

        // ToDo: Later replace this with the logic to retrieve the data from database
        /// ****** START ******
        // Retrieve data for song of the day
        String songOfDayTitle = sharedPreferences.getString("song_of_day_title", "No Song Set");
        String songOfDayArtist = sharedPreferences.getString("song_of_day_artist", "Unknown Artist");
        int songOfDayImage = sharedPreferences.getInt("song_of_day_image_url", R.drawable.album_cover_placeholder);

        // UI for song of the day
        TextView songOfDayTitleView = getView().findViewById(R.id.textViewSongOfTheDaySong);
        TextView songOfDayArtistView = getView().findViewById(R.id.textViewSongOfTheDayArtist);
        ImageView songOfDayImageView = getView().findViewById(R.id.songOfTheDayAlbumCover);

        // Set data for song of the day
        songOfDayTitleView.setText(songOfDayTitle);
        songOfDayArtistView.setText(songOfDayArtist);
        songOfDayImageView.setImageResource(songOfDayImage);

        // Retrieve data of songs from sharedPreferences
        String song1Title = sharedPreferences.getString("saved_song_1", "Default Song 1");
        String song1Artist = sharedPreferences.getString("saved_song_1_artist", "Default Artist 1");
        int song1Image = sharedPreferences.getInt("saved_song_1_image", R.drawable.album_cover_placeholder);

        String song2Title = sharedPreferences.getString("saved_song_2", "Default Song 2");
        String song2Artist = sharedPreferences.getString("saved_song_2_artist", "Default Artist 2");
        int song2Image = sharedPreferences.getInt("saved_song_2_image", R.drawable.album_cover_placeholder);

        String song3Title = sharedPreferences.getString("saved_song_3", "Default Song 3");
        String song3Artist = sharedPreferences.getString("saved_song_3_artist", "Default Artist 3");
        int song3Image = sharedPreferences.getInt("saved_song_3_image", R.drawable.album_cover_placeholder);

        //Retrieve data of artists from sharedPreferences
        String artist1Name = sharedPreferences.getString("saved_artist_1", "Default Artist 1");
        int artist1Image = sharedPreferences.getInt("saved_artist_1_image", R.drawable.profile_pic_placeholder);

        String artist2Name = sharedPreferences.getString("saved_artist_2", "Default Artist 2");
        int artist2Image = sharedPreferences.getInt("saved_artist_2_image", R.drawable.profile_pic_placeholder);

        String artist3Name = sharedPreferences.getString("saved_artist_3", "Default Artist 3");
        int artist3Image = sharedPreferences.getInt("saved_artist_3_image", R.drawable.profile_pic_placeholder);

        // Get UI references to song cards
        TextView songTitle1 = getView().findViewById(R.id.songCard1).findViewById(R.id.songTitle);
        TextView songArtist1 = getView().findViewById(R.id.songCard1).findViewById(R.id.songArtist);
        ImageView songImage1 = getView().findViewById(R.id.songCard1).findViewById(R.id.songImage);

        TextView songTitle2 = getView().findViewById(R.id.songCard2).findViewById(R.id.songTitle);
        TextView songArtist2 = getView().findViewById(R.id.songCard2).findViewById(R.id.songArtist);
        ImageView songImage2 = getView().findViewById(R.id.songCard2).findViewById(R.id.songImage);

        TextView songTitle3 = getView().findViewById(R.id.songCard3).findViewById(R.id.songTitle);
        TextView songArtist3 = getView().findViewById(R.id.songCard3).findViewById(R.id.songArtist);
        ImageView songImage3 = getView().findViewById(R.id.songCard3).findViewById(R.id.songImage);

        //Set data for Song Card 1
        songTitle1.setText(song1Title);
        songArtist1.setText(song1Artist);
        songImage1.setImageResource(song1Image);

        //Set data for Song Card 2
        songTitle2.setText(song2Title);
        songArtist2.setText(song2Artist);
        songImage2.setImageResource(song2Image);

        //Set data for Song Card 3
        songTitle3.setText(song3Title);
        songArtist3.setText(song3Artist);
        songImage3.setImageResource(song3Image);

        // Get UI references to artist cards
        TextView artistName1 = getView().findViewById(R.id.artistCard1).findViewById(R.id.text_artist_name);
        ImageView artistImage1 = getView().findViewById(R.id.artistCard1).findViewById(R.id.image_artist);

        TextView artistName2 = getView().findViewById(R.id.artistCard2).findViewById(R.id.text_artist_name);
        ImageView artistImage2 = getView().findViewById(R.id.artistCard2).findViewById(R.id.image_artist);

        TextView artistName3 = getView().findViewById(R.id.artistCard3).findViewById(R.id.text_artist_name);
        ImageView artistImage3 = getView().findViewById(R.id.artistCard3).findViewById(R.id.image_artist);

        //Set data for Artist Card 1
        artistName1.setText(artist1Name);
        artistImage1.setImageResource(artist1Image);

        //Set data for Artist Card 2
        artistName2.setText(artist2Name);
        artistImage2.setImageResource(artist2Image);

        //Set data for Artist Card 3
        artistName3.setText(artist3Name);
        artistImage3.setImageResource(artist3Image);

        /// ****** END ******

        //Update UI elements with retrieved data
        TextView displayNameTextView = getView().findViewById(R.id.textViewDisplayName);
        TextView bioTextView = getView().findViewById(R.id.textViewBio);

        displayNameTextView.setText(displayName);
        bioTextView.setText(bio);


    }

}