package com.awginc.storemaster.web;

public record IndexModel(String basePath) {

    public String basePath() {
        return basePath;
    }

    public String version() {
        return "1";
    }

}