package dev.littlebigowl.serveressentials.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class Submission {
    private String title;
    private int upvotes;
    private int comments;
    private int awards;
    private int creationDate;
    private String url;
    private String permalink;
    private String subreddit;
    private Boolean archived;
    private Boolean NSFW;
    private Boolean spoiler;
    private Boolean crosspostable;

    public Submission(String title, int upvotes, int comments, int awards, int creationDate, String url, String permalink, String subreddit, Boolean archived, Boolean NSFW, Boolean spoiler, Boolean crosspostable) {
        this.title = title;
        this.upvotes = upvotes;
        this.comments = comments;
        this.awards = awards;
        this.creationDate = creationDate;
        this.url = url;
        this.permalink = permalink;
        this.subreddit = subreddit;
        this.archived =archived;
        this.NSFW = NSFW;
        this.spoiler = spoiler;
        this.crosspostable = crosspostable;
    }

    public ArrayList<JsonObject> getData(String url) throws IOException {
        URL redditUrl = new URL(url);
        URLConnection request = redditUrl.openConnection();

        request.setRequestProperty("Content-Type", "application/json; utf-8");
        request.setRequestProperty("User-Agent", "dev.littlebigowl.redditdiscordtest.bot");

        InputStream inputStream = request.getInputStream();
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonArray obj = gson.fromJson(reader, JsonArray.class);
        JsonObject data = obj.get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject();


        URL subredditURL = new URL("https://www.reddit.com/" + this.subreddit + "/about.json");
        URLConnection subredditRequest = subredditURL.openConnection();;
        
        subredditRequest.setRequestProperty("Content-Type", "application/json; utf-8");
        subredditRequest.setRequestProperty("User-Agent", "dev.littlebigowl.redditdiscordtest.bot");

        inputStream = subredditRequest.getInputStream();
        gson = new Gson();
        reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject subredditData = gson.fromJson(reader, JsonObject.class).get("data").getAsJsonObject();
        
        ArrayList<JsonObject> totalData = new ArrayList<>();
        totalData.add(data);
        totalData.add(subredditData);
        
        return totalData;
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
        
        String title = data.get("title").getAsString();
        int upvotes = data.get("ups").getAsInt();
        int comments = data.get("num_comments").getAsInt();
        int awards = data.get("total_awards_received").getAsInt();
        int creationDate = Math.round(data.get("created_utc").getAsFloat());
        String submiurl = "https://www.reddit.com" + data.get("permalink").getAsString();
        String permalink = data.get("url").getAsString();
        String subreddit = data.get("subreddit_name_prefixed").getAsString();
        Boolean archived = data.get("archived").getAsBoolean();
        Boolean NSFW = data.get("over_18").getAsBoolean();
        Boolean spoiler = data.get("spoiler").getAsBoolean();
        Boolean crosspostable = data.get("is_crosspostable").getAsBoolean();

        return new Submission(title, upvotes, comments, awards, creationDate, submiurl, permalink, subreddit, archived, NSFW, spoiler, crosspostable);
    }

    public String getTitle() {
        return this.title;
    }

    public int getUpvotes() {
        return this.upvotes;
    }

    public int getComments() {
        return this.comments;
    }

    public int getAwards() {
        return this.awards;
    }

    public int getCreationDateUNIX() {
        return this.creationDate;
    }

    public String getCreationDate() {
        int unix = this.getCreationDateUNIX();
        Date date = new Date(unix*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        return sdf.format(date);
    }

    public String getUrl() {
        return this.url;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public Boolean isArchived() {
        return this.archived;
    }

    public Boolean isNSFW() {
        return this.NSFW;
    }

    public Boolean isSpoiler() {
        return this.spoiler;
    }    

    public Boolean isCrosspostable() {
        return this.crosspostable;
    }

    public String getSubreddit() throws IOException {
        return this.subreddit;
    }

    public String getDescription(String url) throws IOException {
        JsonObject data = this.getData(url).get(0);
        return data.get("selftext").getAsString();
    }

    public String downloadMedia(String url) throws IOException {
        JsonObject data = this.getData(url).get(0);

        InputStream videoInputStream = new URL(data.get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("fallback_url").getAsString()).openStream();

        String res = data.get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("fallback_url").getAsString().split(".mp4")[0].split("DASH_")[1];
        InputStream audioInputStream = new URL(data.get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("fallback_url").getAsString().replace("DASH_" + res, "DASH_audio")).openStream();

        File mediaFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ServerEssentials").getDataFolder() + "\\videos\\" + "media.mp4");
        FileUtils.copyInputStreamToFile(videoInputStream, mediaFile);
        File audioFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ServerEssentials").getDataFolder() + "\\videos\\" + "audio.mp4");
        FileUtils.copyInputStreamToFile(audioInputStream, audioFile);

        String[] cmd = new String[] {
            "ffmpeg",
            "-y",
            "-i", 
            audioFile.getAbsolutePath().toString(), 
            "-i",
            mediaFile.getAbsolutePath().toString(),
            "-acodec",
            "copy",
            "-vcodec",
            "copy",
            audioFile.getAbsolutePath().toString().replace("audio.mp4", "video.mp4")
        };

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Bukkit.getLogger().info(res);
        pb.start();

        return audioFile.getAbsolutePath().toString().replace("audio.mp4", "video.mp4");
    }

    public int getVideoDuration(String url) throws IOException {
        JsonObject data = this.getData(url).get(0);
        return Integer.parseInt(data.get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("duration").getAsString());
    }

    public String getSubredditIcon(String url) throws IOException {
        return this.getData(url).get(1).get("community_icon").getAsString().split("\\?")[0];
    }
}
