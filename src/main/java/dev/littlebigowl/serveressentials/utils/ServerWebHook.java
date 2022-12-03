package dev.littlebigowl.serveressentials.utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedFooter;

public class ServerWebHook {

    private final String webHookURL;
    private String username;
    private String avatarURL;
       
    public ServerWebHook(String webHookURL, String username, String avatarURL) {
        this.webHookURL = webHookURL;
        this.username = username;
        this.avatarURL = avatarURL;
    }

    public void sendMessage(String messageContent) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setContent(messageContent);
        builder.setAvatarUrl(this.avatarURL);
        builder.setUsername(this.username);

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(this.webHookURL);
        WebhookClient client = webBuilder.build();
        client.send(message);
    }

    public void sendEmbed(int color, String messageContent) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(color).setDescription(messageContent).build());
        builder.setAvatarUrl(this.avatarURL);
        builder.setUsername(this.username);

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(this.webHookURL);
        WebhookClient client = webBuilder.build();
        client.send(message);
    }

    public void sendEmbed(int color, String messageContent, String authorAvatarURL) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(color).setAuthor(new WebhookEmbed.EmbedAuthor(messageContent, authorAvatarURL, "")).build());
        builder.setAvatarUrl(this.avatarURL);
        builder.setUsername(this.username);

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(this.webHookURL);
        WebhookClient client = webBuilder.build();
        client.send(message);
    }

    public void sendEmbed(int color, String messageContent, String authorAvatarURL, String description) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(color).setAuthor(new WebhookEmbed.EmbedAuthor(messageContent, authorAvatarURL, "")).setFooter(new EmbedFooter(description, "")).build());
        builder.setAvatarUrl(this.avatarURL);
        builder.setUsername(this.username);

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(this.webHookURL);
        WebhookClient client = webBuilder.build();
        client.send(message);
    }

}
