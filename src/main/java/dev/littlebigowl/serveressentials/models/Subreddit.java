package dev.littlebigowl.serveressentials.models;

import com.google.gson.JsonObject;

public class Subreddit {
    private String name;
    private String iconUrl;

    public Subreddit(JsonObject data) {
        this.name = data.get("display_name_prefixed").getAsString();
        this.iconUrl = data.get("community_icon").getAsString().split("\\?")[0];
    }

    public String getName() {
        return this.name;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }
}
