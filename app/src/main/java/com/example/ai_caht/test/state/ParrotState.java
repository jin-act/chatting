package com.example.ai_caht.test.state;

import com.google.gson.annotations.SerializedName;

public class ParrotState {
    @SerializedName("hunger")
    public String hunger;

    @SerializedName("stress")
    public String stress;

    @SerializedName("boredom")
    public String boredom;

    @SerializedName("affection")
    public String affection;

    @SerializedName("level")
    public String level;

    @SerializedName("counter")
    public String counter;

    @SerializedName("lastTime")
    public long lastTime;

    public ParrotState(String hunger, String stress, String boredom, String affection, String level, String counter, long lastTime) {
        this.hunger = hunger;
        this.stress = stress;
        this.boredom = boredom;
        this.affection = affection;
        this.level = level;
        this.counter = counter;
        this.lastTime = lastTime;
    }
}
