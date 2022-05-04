package com.example.ai_caht.test.Signup;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {
    @SerializedName("login_id")
    public String inputId;

    @SerializedName("password")
    public String inputPw;

    @SerializedName("username")
    public String inputName;

    @SerializedName("email")
    public String inputEmail;

    public SignupRequest(String inputId, String inputPw, String inputName, String inputEmail) {
        this.inputId = inputId;
        this.inputPw = inputPw;
        this.inputName = inputName;
        this.inputEmail = inputEmail;
    }
}