package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;

    private Sound music = Gdx.audio.newSound(Gdx.files.internal("sounds/menu-music.wav"));
    private Texture menuBackgroundTexture = new Texture(Gdx.files.internal("images/main-menu-background.png"));
    private GameFontGenerator gameFontGenerator = GameFontGenerator.getInstance();

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        createBackground();

        Table menuTable = new Table();
        menuTable.setPosition(300, 300);

        TextButton.TextButtonStyle textButtonStyle = gameFontGenerator.generateTextButtonStyle(GameFontSizeEnum.NORMAL);

        TextButton newGameButton = new TextButton("New Game", textButtonStyle);
        TextButton settingsButton = new TextButton("Settings", textButtonStyle);
        TextButton creditsButton = new TextButton("Credits", textButtonStyle);
        TextButton leadershipButton = new TextButton("Leadership", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);

        menuTable.pad(10, 0, 10, 0);
        menuTable.add(newGameButton);
        menuTable.row();
//        menuTable.add(settingsButton);
//        menuTable.row();
        menuTable.add(leadershipButton);
        menuTable.row();
        menuTable.add(creditsButton);
        menuTable.row();
        menuTable.add(exitButton);

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.newGame();
                game.setScreen(new GameScreen(game));
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //game.setScreen(new GameScreen(game));
            }
        });

        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new CreditsScreen(game));
            }
        });

        leadershipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(menuTable);

        GameSoundPlayer.playMusic(music);
    }

    private void createBackground() {
        Image backgroundImage = new Image(menuBackgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act();
        stage.draw();
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
        menuBackgroundTexture.dispose();
    }
}