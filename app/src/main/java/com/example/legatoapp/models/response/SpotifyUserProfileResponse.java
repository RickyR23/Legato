package com.example.legatoapp.models.response;

import android.media.Image;

import java.util.List;
import java.util.Map;

public class SpotifyUserProfileResponse {
    private String display_name;
    private String email;
    private Map<String, String> external_urls;
    private List<Image> images;

    public String getDisplayName() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, String> getExternalUrls() {
        return external_urls;
    }

    public List<Image> getImages() {
        return images;
    }

    public static class Image {
        private String url;

        public String getUrl() {
            return url;
        }
    }

}
