package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class ChatRequest {
    @SerializedName("chat")
    public String userchat;

    public ChatRequest(String userchat) {
        this.userchat = userchat;
    }

}
