package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class IdResponse {
    @SerializedName("id")
    public Long id;

    public IdResponse(Long id){
        this.id = id;
    }
}
