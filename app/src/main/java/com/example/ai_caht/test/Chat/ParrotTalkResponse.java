package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class ParrotTalkResponse {
    @SerializedName("question")
    public String question;

    @SerializedName("answer")
    public String answer;
}
