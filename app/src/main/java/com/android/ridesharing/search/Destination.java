package com.android.ridesharing.search;


public class Destination {
    private String name;
    private String difficulty;
    private String distance;
    private String imageUrl;

    public Destination(String name, String difficulty, String distance, String imageUrl) {
        this.name = name;
        this.difficulty = difficulty;
        this.distance = distance;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDistance() {
        return distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

