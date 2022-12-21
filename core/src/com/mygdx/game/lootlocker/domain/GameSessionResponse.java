package com.mygdx.game.lootlocker.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameSessionResponse {
    private Boolean success;
    private String playerId;
    private String playerIdentifier;
    private String publicUid;
    private Boolean seenBefore;
    private String playerCreatedAt;
    private Boolean checkGrantNotifications;
    private Boolean checkDeactivationNotifications;
    private List<String> checkDlcs;
    @SerializedName("public")
    private Boolean ppublic;
    private String sessionToken;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }

    public void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    public String getPublicUid() {
        return publicUid;
    }

    public void setPublicUid(String publicUid) {
        this.publicUid = publicUid;
    }

    public Boolean getSeenBefore() {
        return seenBefore;
    }

    public void setSeenBefore(Boolean seenBefore) {
        this.seenBefore = seenBefore;
    }

    public String getPlayerCreatedAt() {
        return playerCreatedAt;
    }

    public void setPlayerCreatedAt(String playerCreatedAt) {
        this.playerCreatedAt = playerCreatedAt;
    }

    public Boolean getCheckGrantNotifications() {
        return checkGrantNotifications;
    }

    public void setCheckGrantNotifications(Boolean checkGrantNotification) {
        this.checkGrantNotifications = checkGrantNotification;
    }

    public Boolean getCheckDeactivationNotifications() {
        return checkDeactivationNotifications;
    }

    public void setCheckDeactivationNotifications(Boolean checkDeactivationNotifications) {
        this.checkDeactivationNotifications = checkDeactivationNotifications;
    }

    public List<String> getCheckDlcs() {
        return checkDlcs;
    }

    public void setCheckDlcs(List<String> checkDlcs) {
        this.checkDlcs = checkDlcs;
    }

    public Boolean getPpublic() {
        return ppublic;
    }

    public void setPpublic(Boolean ppublic) {
        this.ppublic = ppublic;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
