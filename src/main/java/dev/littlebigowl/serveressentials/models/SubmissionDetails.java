package dev.littlebigowl.serveressentials.models;

public class SubmissionDetails {
    
    private String id;
    private String title;
    private String upvotes;
    private String comments;
    private String awards;
    private String creationDate;
    private String permalink;
    private String subreddit;
    private Boolean archived;
    private Boolean NSFW;
    private Boolean spoiler;
    private Boolean crosspostable;
    
    public SubmissionDetails(Submission submission, String id){
        this.id = id;
        this.title = submission.getTitle();
        this.upvotes = submission.getUpvotes();
        this.comments = submission.getComments();
        this.awards = submission.getAwards();
        this.creationDate = submission.getCreationDate();
        this.permalink = "https://www.reddit.com" + submission.getRawJsonData().get("permalink").getAsString();
        this.subreddit = submission.getRawJsonData().get("subreddit_name_prefixed").getAsString();
        this.archived = submission.isArchived();
        this.NSFW = submission.isNSFW();
        this.spoiler = submission.isSpoiler();
        this.crosspostable = submission.isCrosspostable();
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUpvotes() {
        return this.upvotes;
    }

    public String getCommentCount() {
        return this.comments;
    }

    public String getAwards() {
        return this.awards;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public String getSubredditName() {
        return this.subreddit;
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

}
