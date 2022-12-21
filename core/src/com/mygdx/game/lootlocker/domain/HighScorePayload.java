package com.mygdx.game.lootlocker.domain;

public class HighScorePayload {
    private Long score;
    private String metadata;

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
