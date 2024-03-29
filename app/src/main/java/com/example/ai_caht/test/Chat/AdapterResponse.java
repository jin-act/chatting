package com.example.ai_caht.test.Chat;

import com.google.gson.annotations.SerializedName;

public class AdapterResponse {
    @SerializedName("id")
    public long id;

    @SerializedName("profile")
    public int profile;

    @SerializedName("contents")
    public String contents;

    @SerializedName("position")
    public int position;

    @SerializedName("time")
    public String time;

    @SerializedName("visibility")
    public int visibility;

    @SerializedName("textBox")
    public int textBox;

    @SerializedName("radio")
    public int radio;

    @SerializedName("timeText")
    public int timeText;

    public AdapterResponse(long id, int profile, String contents, int position, String time, int visibility,
                          int textBox, int radio, int timeText) {
        this.id = id;
        this.profile = profile;
        this.contents = contents;
        this.position = position;
        this.time = time;
        this.visibility = visibility;
        this.textBox = textBox;
        this.radio = radio;
        this.timeText = timeText;
    }

}
