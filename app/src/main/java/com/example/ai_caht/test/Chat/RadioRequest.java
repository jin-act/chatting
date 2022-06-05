package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class RadioRequest {
    @SerializedName("radio")
    public int radio;

    public RadioRequest(int radio) {
        this.radio = radio;
    }
}
