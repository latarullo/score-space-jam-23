package com.mygdx.game.lootlocker.domain;

import java.util.List;

public class HighScoreResponse {
    private Pagination pagination;
    private List<HighScoreItem> items;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<HighScoreItem> getItems() {
        return items;
    }

    public void setItems(List<HighScoreItem> items) {
        this.items = items;
    }
}

