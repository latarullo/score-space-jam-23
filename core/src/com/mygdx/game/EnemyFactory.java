package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.gameobjects.AbstractEnemy;
import com.mygdx.game.gameobjects.Enemy1;
import com.mygdx.game.gameobjects.Enemy2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyFactory {
    private enum EnemyType{
        ONE, TWO
    }

    private Stage stage;
    private MyGdxGame game;
    private int waveCount = 0;
    private int waveMultiplier = 2;
    private float increaseSpeedModifier = 0.25f;
    private float increaseFireRateModifier = 0.5f;

    public EnemyFactory(Stage stage, MyGdxGame game) {
        this.stage = stage;
        this.game = game;
    }

    public void createNextEnemyWave() {
        game.getEnemies().clear();

        EnemyType enemyType;

        if (Math.random() > 0.5) {
            enemyType = EnemyType.ONE;
        } else {
            enemyType = EnemyType.TWO;
        }

        for (int i = 0; i < 10 + waveCount * waveMultiplier; i++) {
            AbstractEnemy enemy = createEnemy(enemyType);
            int randomX = new Random().nextInt((int) (GameConstants.WIDTH - enemy.getWidth()));
            int randomY = new Random().nextInt(10);
            enemy.setPosition(randomX, 700 - randomY * 10);
            enemy.setSize(64, 64);
            enemy.increaseSpeed(waveCount * increaseSpeedModifier);
            //enemy.increateFireRate(waveCount * increaseFireRateModifier);
            game.getEnemies().add(enemy);
            stage.addActor(enemy);
        }
        waveCount++;
    }

    public AbstractEnemy createEnemy(EnemyType enemyType){
        switch(enemyType) {
            case ONE:
                return new Enemy1(game);
            case TWO:
                return new Enemy2(game);
            default:
                return new Enemy1(game);
        }
    }

    public int getWaveCount(){
        return waveCount;
    }
}