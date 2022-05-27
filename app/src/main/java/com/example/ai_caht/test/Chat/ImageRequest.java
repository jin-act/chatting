package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class ImageRequest {
    @SerializedName("image")
    public String image;

    @SerializedName("type")
    public String type;

    public ImageRequest(String image, String type) {
        this.image = image;
        this.type = type;
    }
}
