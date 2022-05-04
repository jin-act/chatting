package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class ParrotTalkRequest {
    @SerializedName("state")
    public String state;

    public ParrotTalkRequest(String state) {
        this.state = state;
    }
}
