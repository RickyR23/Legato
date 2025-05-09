package com.example.legatoapp.Activity;

import static java.security.AccessController.getContext;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legatoapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileActivity extends AppCompatActivity {
    private CircleImageView profilePicImageView;
    private TextView currentlyPlayingArtist, currentlyPlayingSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);
        profilePicImageView = findViewById(R.id.profilePicImageView);

        // ToDo: Implement the logic to retrieve all the data from db like display name, pfp, top songs, artists, bio, followers, following, etc

        // Get data from Intent
        String username = getIntent().getStringExtra("USERNAME");
        String displayName = getIntent().getStringExtra("DISPLAY_NAME");
        int profileImageResId = getIntent().getIntExtra("PROFILE_IMAGE_RES_ID", R.drawable.profile_pic_placeholder);

        // Bind data
        TextView displayNameText = findViewById(R.id.textViewDisplayName);
        ImageView profileImage = findViewById(R.id.profilePicImageView);

        displayNameText.setText(displayName);
        profileImage.setImageResource(profileImageResId);

        loadProfileData();

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

        // Follow button changes when clicked on
        Button followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            private boolean isFollowing = false;
            // ToDo: Retrieve if the user follows this other user, and also implement the logic so it is added or removed from following list

            @Override
            public void onClick(View view) {
                isFollowing = !isFollowing;

                if (isFollowing) {
                    followButton.setText("Following");
                    followButton.setBackgroundTintList(ContextCompat.getColorStateList(OtherUserProfileActivity.this, android.R.color.darker_gray));
                } else {
                    followButton.setText("Follow");
                    followButton.setBackgroundTintList(ContextCompat.getColorStateList(OtherUserProfileActivity.this, android.R.color.white));
                }
            }
        });

        // Follower and following
        LinearLayout followersContainer = findViewById(R.id.followersContainer);
        LinearLayout followingContainer = findViewById(R.id.followingContainer);

        // OnClickListener when followers is clicked
        followersContainer.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FollowersListActivity.class);
            startActivity(intent);
        });

        // OnClickListener when following is clicked
        followingContainer.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FollowingListActivity.class);
            startActivity(intent);
        });

        // Back button logic
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }


    private void loadProfileData() {
        // Get UI references to song cards
        TextView songTitle1 = findViewById(R.id.songCard1).findViewById(R.id.songTitle);
        TextView songArtist1 = findViewById(R.id.songCard1).findViewById(R.id.songArtist);
        ImageView songImage1 = findViewById(R.id.songCard1).findViewById(R.id.songImage);

        TextView songTitle2 = findViewById(R.id.songCard2).findViewById(R.id.songTitle);
        TextView songArtist2 = findViewById(R.id.songCard2).findViewById(R.id.songArtist);
        ImageView songImage2 = findViewById(R.id.songCard2).findViewById(R.id.songImage);

        TextView songTitle3 = findViewById(R.id.songCard3).findViewById(R.id.songTitle);
        TextView songArtist3 = findViewById(R.id.songCard3).findViewById(R.id.songArtist);
        ImageView songImage3 = findViewById(R.id.songCard3).findViewById(R.id.songImage);

        //Set data for Song Card 1
        songTitle1.setText("Sober");
        songArtist1.setText("Childish Gambino");
        int song1 = R.drawable.song14;
        songImage1.setImageResource(song1);

        //Set data for Song Card 2
        songTitle2.setText("505");
        songArtist2.setText("Arctic Monkeys");
        int song2 = R.drawable.song13;
        songImage2.setImageResource(song2);

        //Set data for Song Card 3
        songTitle3.setText("Just");
        songArtist3.setText("Radiohead");
        int song3 = R.drawable.song16;
        songImage3.setImageResource(song3);

        // Get UI references to artist cards
        TextView artistName1 = findViewById(R.id.artistCard1).findViewById(R.id.text_artist_name);
        ImageView artistImage1 = findViewById(R.id.artistCard1).findViewById(R.id.image_artist);

        TextView artistName2 = findViewById(R.id.artistCard2).findViewById(R.id.text_artist_name);
        ImageView artistImage2 = findViewById(R.id.artistCard2).findViewById(R.id.image_artist);

        TextView artistName3 = findViewById(R.id.artistCard3).findViewById(R.id.text_artist_name);
        ImageView artistImage3 = findViewById(R.id.artistCard3).findViewById(R.id.image_artist);

        //Set data for Artist Card 1
        artistName1.setText("Frank Ocean");
        int artist1 = R.drawable.artist11;
        artistImage1.setImageResource(artist1);

        //Set data for Artist Card 2
        artistName2.setText("Mac DeMarco");
        int artist2 = R.drawable.artist15;
        artistImage2.setImageResource(artist2);

        //Set data for Artist Card 3
        artistName3.setText("Queen");
        int artist3 = R.drawable.artist13;
        artistImage3.setImageResource(artist3);

        /// ****** END ******

    }

}
