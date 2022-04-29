package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class ChatRequest {
    @SerializedName("chat")
    public String userchat;

    @SerializedName("login_id")
    public String userID;

    public ChatRequest(String userchat, String userID) {
        this.userchat = userchat;
        this.userID = userID;
    }

}
