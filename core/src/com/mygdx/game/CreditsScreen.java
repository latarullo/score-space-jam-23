package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CreditsScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;

    private Texture background = new Texture(Gdx.files.internal("images/background.png"));
    private GameFontGenerator gameFontGenerator = GameFontGenerator.getInstance();
    private Sound typingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/keyboard-typing.wav"));

    private final String developedByString = "Developed by:\n   Latarullo";
    private final String artsByString = "Arts by:\n   HOKFMK";
    private final String composedByString = "Composed by:\n   Dolphinflavored";

    public CreditsScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        createBackButton();
        createCreditsHeader();
        createHighScoreTable();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        typingSound.loop(1);
        //GameSoundPlayer.playMusic(typingSound);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act();
        stage.draw();

        checkAndStopTypingShound();
    }

    private void checkAndStopTypingShound() {
        boolean shouldStopTypingSound = true;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Table) {
                Table table = (Table) actor;
                for (Actor tableChild : table.getChildren()) {
                    if (tableChild instanceof TypingLabel) {
                        TypingLabel typingLabel = (TypingLabel) tableChild;
                        if (!typingLabel.isTypingComplete()) {
                            shouldStopTypingSound = false;
                        }
                    }
                }
            }
        }
        if (shouldStopTypingSound) {
            GameSoundPlayer.stop(typingSound);
        }
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
        GameSoundPlayer.stop(typingSound);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void createHighScoreTable() {
        Table table = new Table();
        table.setPosition(300, 50);
        table.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                float y = getActor().getY();
                y = y + 50 * delta;
                getActor().setY(y);
                if (y >= 350) {
                    return true;
                }
                return false;
            }
        });

        Label.LabelStyle labelStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, Color.LIGHT_GRAY);

        TypingLabel gameDeveloperLabel = new TypingLabel(developedByString, labelStyle);
        TypingLabel gameArtistLabel = new TypingLabel(artsByString, labelStyle);
        TypingLabel gameComposerLabel = new TypingLabel(composedByString, labelStyle);

        table.pad(10, 0, 10, 0);
        table.add(gameDeveloperLabel).fill().uniformX().padBottom(30);
        table.row();
        table.add(gameArtistLabel).fill().uniformX().padBottom(30);;
        table.row();
        table.add(gameComposerLabel).fill();

        stage.addActor(table);
    }

    private void createCreditsHeader() {
        Label.LabelStyle labelHeaderStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, GameConstants.SCREEN_TITLE_COLOR);
        Label consumablesLabel = new Label("Credits", labelHeaderStyle);
        consumablesLabel.setPosition(GameConstants.WIDTH / 2 - consumablesLabel.getWidth() / 2, GameConstants.HEIGHT - consumablesLabel.getHeight() - 10);
        stage.addActor(consumablesLabel);
    }

    private void createBackButton() {
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("images/back-button.png"))));
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.setPosition(10, GameConstants.HEIGHT - 64);
        backButton.setSize(64, 64);
        stage.addActor(backButton);
    }
}