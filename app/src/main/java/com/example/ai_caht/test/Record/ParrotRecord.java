package com.example.ai_caht.test.Record;

import com.google.gson.annotations.SerializedName;

public class ParrotRecord {
    @SerializedName("page")
    public String page;

    @Override
    public String toString() {
        return "ParrotRecord{" +
                "page='" + page + '\'' +
                ", date='" + date + '\'' +
                ", parrotState=" + parrotState +
                ", feed=" + feed +
                ", feedCount=" + feedCount +
                ", playType=" + playType +
                ", playResult=" + playResult +
                ", chatCount=" + chatCount +
                '}';
    }

    @SerializedName("date")
    public String date;

    @SerializedName("parrotState")
    public int parrotState;

    @SerializedName("feed")
    public int feed;

    @SerializedName("feedCount")
    public int feedCount;

    @SerializedName("playType")
    public int playType;

    @SerializedName("playResult")
    public int playResult;

    @SerializedName("chatCount")
    public int chatCount;

    public ParrotRecord(String page, String date, int parrotState, int feed, int feedCount, int playType, int playResult, int chatCount) {
        this.page = page;
        this.date = date;
        this.parrotState = parrotState;
        this.feed = feed;
        this.feedCount = feedCount;
        this.playType = playType;
        this.playResult = playResult;
        this.chatCount = chatCount;
    }
}
