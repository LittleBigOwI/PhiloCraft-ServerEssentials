package dev.littlebigowl.serveressentials.models;

import java.util.Date;

public class Home {
    private String playerName;
    private String name;
    private String location;
    private Date created;
    public Home(String playerName, String name, String location) {
        this.playerName = playerName;
        this.name = name;
        this.location = location;
        this.created = new Date();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getCreated() {
        return created;
    }
}
