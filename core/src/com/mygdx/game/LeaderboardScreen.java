package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.lootlocker.LootLockerService;
import com.mygdx.game.lootlocker.domain.HighScoreItem;
import com.mygdx.game.lootlocker.domain.HighScoreResponse;

import java.util.List;

public class LeaderboardScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;

    private Texture background = new Texture(Gdx.files.internal("images/background.png"));
    private GameFontGenerator gameFontGenerator = GameFontGenerator.getInstance();
    LootLockerService lootLockerService = LootLockerService.getInstance();

    public LeaderboardScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        lootLockerService.createSession();
        stage.addActor(createGUI());
        lootLockerService.deleteSession();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //GameSoundPlayer.playMusic(music);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
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
        //GameSoundPlayer.stop(music);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Table createGUI() {
        createBackButton();
        createScreenTitle();

        Table table = createHighScoreTable();

        return table;
    }

    private Table createHighScoreTable() {
        Table table = new Table();
        table.setFillParent(true);

        HighScoreResponse highScores = lootLockerService.getHighScores();
        List<HighScoreItem> items = highScores.getItems();
        for (HighScoreItem item : items) {
            Integer rank = item.getRank();
            Long score = item.getScore();
            String name = item.getPlayer().getName();

            Label.LabelStyle labelHeaderStyle;
            if (rank == 1){
                labelHeaderStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.SMALL, Color.RED);
            } else {
                labelHeaderStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.SMALL, Color.WHITE);
            }

            Label rankLabel = new Label("#"+ String.valueOf(rank), labelHeaderStyle);
            Label nameLabel = new Label(name, labelHeaderStyle);
            Label scoreLabel = new Label(String.valueOf(score), labelHeaderStyle);

            table.add(rankLabel).align(Align.right).padRight(10);
            table.add(nameLabel).width(400).align(Align.left);
            table.add(scoreLabel).align(Align.right);

            table.row();
        }
        return table;
    }

    private void createScreenTitle() {
        Label.LabelStyle labelHeaderStyle = gameFontGenerator.generateLabelStyle(GameFontSizeEnum.NORMAL, GameConstants.SCREEN_TITLE_COLOR);
        Label consumablesLabel = new Label("Leadership", labelHeaderStyle);
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
        backButton.setSize(64,64);
        stage.addActor(backButton);
    }
}