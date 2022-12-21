package com.mygdx.game.lootlocker.domain;

public class Pagination {
    private Integer total;
    private Integer nextCursor;
    private Integer previousCursor;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Integer nextCursor) {
        this.nextCursor = nextCursor;
    }

    public Integer getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(Integer previousCursor) {
        this.previousCursor = previousCursor;
    }
}