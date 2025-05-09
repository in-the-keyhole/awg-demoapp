package com.awginc.sm.web;

public record IndexModel(String basePath) {

    public String basePath() {
        return basePath;
    }

    public String version() {
        return "1";
    }

}