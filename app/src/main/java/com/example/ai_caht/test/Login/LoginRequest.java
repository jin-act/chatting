package com.example.ai_caht.test.Login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("login_id")
    public String inputId;

    @SerializedName("password")
    public String inputPw;

    public LoginRequest(String inputId, String inputPw) {
        this.inputId = inputId;
        this.inputPw = inputPw;
    }
}