package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.gameobjects.AbstractEnemy;
import com.mygdx.game.gameobjects.Spaceship;
import com.mygdx.game.gameobjects.Star;
import com.mygdx.game.lootlocker.LootLockerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;

    private final Texture spaceshipEnemyTexture = new Texture(Gdx.files.internal("images/spaceship-enemy.png"));
    private Sound music = Gdx.audio.newSound(Gdx.files.internal("sounds/game-music.wav"));

    private Texture backgroundTexture1 = new Texture(Gdx.files.internal("images/background.png"));
    private Texture backgroundTexture2 = new Texture(Gdx.files.internal("images/background.png"));

    private float spaceshipMoveSpeed = 200f;
    private float backgroundMoveSpeed = 250f;

    private Spaceship spaceship;
    private List<AbstractEnemy> enemies = new ArrayList<>();
    private int yMax = GameConstants.HEIGHT;
    private int background1positionY = yMax;
    private int background2positionY = yMax - GameConstants.HEIGHT;

    private EnemyFactory enemyFactory;

    public GameScreen(final MyGdxGame game) {
        this.game = game;
        game.newGame();
        stage = new Stage(new ScreenViewport());
        enemyFactory = new EnemyFactory(stage, game);
        spaceship = game.getSpaceship();
        createEnemies();
        createSpaceShip();
        createScoreGUI();
        createHighScoreGUI();
        GameSoundPlayer.playMusic(music);
    }

    private void createSpaceShip() {
        spaceship.setPosition(GameConstants.WIDTH / 2 + spaceship.getWidth() / 2, 10);
        stage.addActor(spaceship);
    }

    private void createHighScoreGUI() {
        Label.LabelStyle labelStyle1 = GameFontGenerator.getInstance().generateLabelStyle(GameFontSizeEnum.SMALL, Color.WHITE);
        Label highScoreLabel = new Label("HIGH SCORE", labelStyle1);
        Label highScoreValueLabel = new Label("0", labelStyle1);

        Table highScoreTable = new Table();
        highScoreTable.setFillParent(true);
        highScoreTable.add(highScoreLabel).padRight(5);
        highScoreTable.add(highScoreValueLabel);
        highScoreTable.align(Align.topRight);
        stage.addActor(highScoreTable);

        highScoreValueLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Label thisLabel = (Label) getActor();
                thisLabel.setText(String.valueOf(LootLockerService.getInstance().getHighScore()));

                return false;
            }
        });
    }

    private void createScoreGUI() {
        Label.LabelStyle labelStyle1 = GameFontGenerator.getInstance().generateLabelStyle(GameFontSizeEnum.SMALL, Color.WHITE);
        Label scoreLabel = new Label("SCORE", labelStyle1);
        Label scoreValueLabel = new Label("0", labelStyle1);
        Label enemiesLeftLabel = new Label("ENEMIES LEFT", labelStyle1);
        Label enemiesLeftLabelValue = new Label("0", labelStyle1);
        Label waveLabel = new Label("ENEMY WAVE", labelStyle1);
        Label waveLabelValue = new Label("0", labelStyle1);

        Table scoreTable = new Table();
        scoreTable.setFillParent(true);
        scoreTable.align(Align.topLeft);

        scoreTable.add(scoreLabel).align(Align.left).padRight(5);
        scoreTable.add(scoreValueLabel).align(Align.right);
        scoreTable.row();
        scoreTable.add(enemiesLeftLabel).align(Align.left).padRight(5);
        scoreTable.add(enemiesLeftLabelValue).align(Align.right);
        scoreTable.row();
        scoreTable.add(waveLabel).align(Align.left).padRight(5);
        scoreTable.add(waveLabelValue).align(Align.right);

        stage.addActor(scoreTable);

        createBackgroundStars();

        scoreValueLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Label thisLabel = (Label) getActor();
                thisLabel.setText((int) game.getScore());
                return false;
            }
        });
        enemiesLeftLabelValue.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Label thisLabel = (Label) getActor();
                thisLabel.setText((int) game.getEnemies().size());
                return false;
            }
        });
        waveLabelValue.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Label thisLabel = (Label) getActor();
                thisLabel.setText((int) enemyFactory.getWaveCount());
                return false;
            }
        });
    }

    private void createEnemies() {
        enemyFactory.createNextEnemyWave();
        //enemyFactory.autoGenerateWaves();
    }

    private void createBackgroundStars() {
        for (int i = 0; i < 10; i++) {
            int randomX = new Random().nextInt(GameConstants.WIDTH);
            int randomY = new Random().nextInt(GameConstants.HEIGHT);
            Star star = new Star();
            star.setPosition(randomX, randomY);
            stage.addActor(star);
            star.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    Star actor = (Star) getActor();
                    if (actor.getY() + actor.getHeight() > 0) {
                        actor.setY(actor.getY() - backgroundMoveSpeed * Gdx.graphics.getDeltaTime());
                    } else {
                        int randomX = new Random().nextInt(GameConstants.WIDTH);
                        int randomY = new Random().nextInt(100);
                        actor.setPosition(randomX, GameConstants.HEIGHT + randomY);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //GameSoundPlayer.playMusic(music);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            spaceship.moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            spaceship.moveRight(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            spaceship.moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            spaceship.moveDown(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            spaceship.shoot();
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
//            game.setScreen(new GameOverScreen(game));
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
//            List<AbstractEnemy> enemies = game.getEnemies();
//            for (AbstractEnemy enemy : enemies) {
//                enemy.remove();
//            }
//            game.getEnemies().clear();
//        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        scrollBackground();

        stage.act();
        stage.draw();

        createNextEnemyWaveIfNeeded();
    }

    private void createNextEnemyWaveIfNeeded() {
        if (game.getEnemies().size() == 0){
            enemyFactory.createNextEnemyWave();
        }
    }

    private void scrollBackground() {
        background1positionY -= backgroundMoveSpeed * Gdx.graphics.getDeltaTime();
        background2positionY = background1positionY - yMax;
        if (background1positionY < 0) {
            background1positionY = yMax;
            background2positionY = yMax - GameConstants.HEIGHT;
        }

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(backgroundTexture2, 0, background1positionY);
        game.getSpriteBatch().draw(backgroundTexture2, 0, background2positionY);
        game.getSpriteBatch().end();
    }

    private void shoot() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        GameSoundPlayer.stop(music);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture1.dispose();
        backgroundTexture2.dispose();
        spaceshipEnemyTexture.dispose();
        music.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}