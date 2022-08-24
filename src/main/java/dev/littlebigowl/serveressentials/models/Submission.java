package dev.littlebigowl.serveressentials.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Submission {
    private String title;
    private String upvotes;
    private String comments;
    private String awards;
    private String creationDate;
    private String url;
    private String permalink;
    private String subredditName;
    private Subreddit subreddit;
    private String archived;
    private String NSFW;
    private String spoiler;
    private String crosspostable;
    private JsonObject data;

    public Submission(JsonObject data) {
        this.title = data.get("title").getAsString();
        this.upvotes = data.get("ups").getAsString();
        this.comments = data.get("num_comments").getAsString();
        this.awards = data.get("total_awards_received").getAsString();
        this.creationDate = data.get("created_utc").getAsString();
        this.url = data.get("url").getAsString();
        this.permalink = data.get("permalink").getAsString();
        this.subredditName = data.get("subreddit_name_prefixed").getAsString();
        this.subreddit = null;
        this.archived = data.get("archived").getAsString();
        this.NSFW = data.get("over_18").getAsString();
        this.spoiler = data.get("spoiler").getAsString();
        this.crosspostable = data.get("is_crosspostable").getAsString();
        this.data = data;
    }

    public static Submission fromUrl(String url) throws IOException {
        URL redditUrl = new URL(url);
        URLConnection request = redditUrl.openConnection();

        request.setRequestProperty("Content-Type", "application/json; utf-8");
        request.setRequestProperty("User-Agent", "dev.littlebigowl.redditdiscordtest.bot");

        InputStream inputStream = request.getInputStream();
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonArray obj = gson.fromJson(reader, JsonArray.class);
        JsonObject data = obj.get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject();
        
        return new Submission(data);
    }

    public String getTitle() {
        return this.title;
    }

    public String getUpvotes() {
        return this.upvotes;
    }

    public String getComments() {
        return this.comments;
    }

    public String getAwards() {
        return this.awards;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public Subreddit getSubreddit() throws IOException {
        if(this.subreddit == null) {
            URL subredditURL = new URL("https://www.reddit.com/" + this.subredditName + "/about.json");
            URLConnection subredditRequest = subredditURL.openConnection();;
            
            subredditRequest.setRequestProperty("Content-Type", "application/json; utf-8");
            subredditRequest.setRequestProperty("User-Agent", "dev.littlebigowl.redditdiscordtest.bot");

            InputStream inputStream = subredditRequest.getInputStream();
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            JsonObject data = gson.fromJson(reader, JsonObject.class).get("data").getAsJsonObject();
            this.subreddit = new Subreddit(data);
        }
        return this.subreddit;
    }

    public Boolean isArchived() {
        if(this.archived.equals("false")) { return false; } else { return true; }
    }

    public Boolean isNSFW() {
        if(this.NSFW.equals("false")) { return false; } else { return true; }
    }

    public Boolean isSpoiler() {
        if(this.spoiler.equals("false")) { return false; } else { return true; }
    }

    public Boolean isCrosspostable() {
        if(this.crosspostable.equals("false")) { return false; } else { return true; }
    }

    public JsonObject getRawJsonData() {
        return this.data;
    }
}
