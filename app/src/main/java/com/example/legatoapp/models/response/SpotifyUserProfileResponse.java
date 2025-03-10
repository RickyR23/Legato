package com.example.legatoapp.models.response;

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

    public String getSpotify() {
        return external_urls != null ? external_urls.get("spotify") : null;
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