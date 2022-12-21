package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.lootlocker.LootLockerService;

public class GameOverScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;

    private GameFontGenerator gameFontGenerator = GameFontGenerator.getInstance();
    private Texture cursorTexture;
    private LootLockerService lootLockerService = LootLockerService.getInstance();

    public GameOverScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        createCursorTexture();
        createBackButton();
        createGameOverGUI();
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
    }

    @Override
    public void dispose() {
        stage.dispose();
        cursorTexture.dispose();
    }

    private void createCursorTexture() {
        Pixmap pixmapGreen = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapGreen.setColor(Color.YELLOW);
        pixmapGreen.fill();
        cursorTexture = new Texture(pixmapGreen);
    }

    private void createGameOverGUI() {
        createScreenTitle();
        createScreenDetails();
    }

    private void createScreenTitle() {
        Label.LabelStyle labelHeaderStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, GameConstants.SCREEN_TITLE_COLOR);
        Label screenTitleLabel = new Label("GAME OVER", labelHeaderStyle);
        screenTitleLabel.setPosition(GameConstants.WIDTH / 2 - screenTitleLabel.getWidth() / 2, GameConstants.HEIGHT - screenTitleLabel.getHeight() - 10);
        stage.addActor(screenTitleLabel);
    }

    private void createScreenDetails() {
        Label.LabelStyle labelFieldsStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, Color.LIGHT_GRAY);
        Label usernameLabel = new Label("Name", labelFieldsStyle);
        Label scoreLabel = new Label("Score", labelFieldsStyle);

        Label.LabelStyle scoreValueStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, Color.YELLOW);
        Label scoreLabelValue = new Label(String.valueOf(game.getScore()), scoreValueStyle);

        TextField.TextFieldStyle nameInputStyle = createTextFieldStyle();

        final TextField usernameText = new TextField(lootLockerService.getPlayerName(), nameInputStyle);
        usernameText.setMaxLength(30);

        TextButton.TextButtonStyle textButtonStyle = gameFontGenerator.generateTextButtonStyle(GameFontSizeEnum.NORMAL);

        TextButton submitScoreButton = new TextButton("Submit Score", textButtonStyle);
        submitScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String typedUsername = usernameText.getText();
                if (typedUsername == null || typedUsername.trim().isEmpty()){
                    typedUsername = "Anonymous";
                    usernameText.setText(typedUsername);
                }

                Long score = game.getScore();

                lootLockerService.createSession();
                if (lootLockerService.getPlayerName() == null ||
                        !lootLockerService.getPlayerName().equals(typedUsername)) {
                    lootLockerService.changePlayerName(typedUsername);
                }
                lootLockerService.sendHighScore(score);
                lootLockerService.deleteSession();

                TextButton actor = (TextButton) event.getListenerActor();
                actor.setText("Score submitted");
                actor.setDisabled(true);
            }
        });

        TextButton playAgainButton = new TextButton("Play Again?", textButtonStyle);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(scoreLabel).align(Align.left);
        table.add(scoreLabelValue).align(Align.right);
        table.row();
        table.add(usernameLabel).colspan(2).align(Align.left);
        table.row();
        table.add(usernameText).colspan(2).align(Align.left).fill();
        table.row();
        table.add(new Label("", labelFieldsStyle));
        table.row();
        table.add(submitScoreButton).colspan(2);
        table.row();
        table.add(new Label("", labelFieldsStyle));
        table.row();
        table.add(playAgainButton).width(500).colspan(2);

        stage.setKeyboardFocus(usernameText);
        stage.addActor(table);
    }

    private TextField.TextFieldStyle createTextFieldStyle() {
        TextField.TextFieldStyle nameInputStyle = gameFontGenerator.generateTextFieldStyle(GameFontSizeEnum.NORMAL, Color.YELLOW);

        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        nameInputStyle.cursor = cursorDrawable;
        nameInputStyle.cursor.setMinWidth(2);

        return nameInputStyle;
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