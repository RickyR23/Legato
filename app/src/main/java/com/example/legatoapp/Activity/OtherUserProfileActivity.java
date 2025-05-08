package com.example.legatoapp.Activity;

import static java.security.AccessController.getContext;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
}
