package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gameobjects.AbstractEnemy;
import com.mygdx.game.gameobjects.Spaceship;
import com.mygdx.game.lootlocker.LootLockerService;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends Game {
    private SpriteBatch spriteBatch;
    private Spaceship spaceship;
    private List<AbstractEnemy> enemies = new ArrayList<>();
    private long score;
    private LootLockerService lootLockerService = LootLockerService.getInstance();

    @Override
    public void create() {
        lootLockerService.createSession();

        spriteBatch = new SpriteBatch();
        newGame();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        lootLockerService.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public List<AbstractEnemy> getEnemies() {
        return enemies;
    }

    public void newGame(){
        this.score = 0;
        this.enemies = new ArrayList<>();
        spaceship = new Spaceship(this);
    }
}
