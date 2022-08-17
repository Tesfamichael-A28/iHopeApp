package com.example.MovieStoreList;

public class Model {
    private int id;
    private String name;
    private String season;
    private String episode;
    private byte[] image;

    public Model(int id, String name, String season, String episode, byte[] image) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.episode = episode;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
