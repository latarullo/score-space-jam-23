package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameFontGenerator {
    private static final GameFontGenerator instance = new GameFontGenerator();

    public static GameFontGenerator getInstance() {
        return instance;
    }

    private FileHandle fontFile;
    private BitmapFont smallFont;
    private BitmapFont font;
    private BitmapFont largeFont;

    private GameFontGenerator() {
        fontFile = Gdx.files.internal("fonts/wheaton capitals.ttf");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterSmall = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterSmall.size = 20;
        smallFont = generator.generateFont(parameterSmall);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterLarge = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterLarge.size = 100;
        largeFont = generator.generateFont(parameterLarge);

        generator.dispose();
    }

    public Label.LabelStyle generateLabelStyle(int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = generator.generateFont(createFontParameter(size, color));
        generator.dispose();

        return labelStyle;
    }

    public TextButton.TextButtonStyle generateTextButtonStyle(GameFontSizeEnum gameFontSizeEnum) {
        BitmapFont bitmapFont = getFont();

        if (gameFontSizeEnum == GameFontSizeEnum.SMALL) {
            bitmapFont = getSmallFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.NORMAL) {
            bitmapFont = getFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.LARGE) {
            bitmapFont = getLargeFont();
        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = GameConstants.BUTTON_COLOR;
        textButtonStyle.overFontColor = GameConstants.BUTTON_HOVER_COLOR;
        textButtonStyle.downFontColor = GameConstants.BUTTON_CLICK_COLOR;
        textButtonStyle.disabledFontColor = GameConstants.BUTTON_DISABLED_COLOR;

        return textButtonStyle;
    }

    public Label.LabelStyle generateLabelStyle(GameFontSizeEnum gameFontSizeEnum, Color color) {
        BitmapFont bitmapFont = getFont();

        if (gameFontSizeEnum == GameFontSizeEnum.SMALL) {
            bitmapFont = getSmallFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.NORMAL) {
            bitmapFont = getFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.LARGE) {
            bitmapFont = getLargeFont();
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = color;

        return labelStyle;
    }

    public TextButton.TextButtonStyle generateTextButtonStyle(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(createFontParameter(size, GameConstants.BUTTON_COLOR));
        textButtonStyle.fontColor = GameConstants.BUTTON_COLOR;
        textButtonStyle.overFontColor = GameConstants.BUTTON_HOVER_COLOR;
        textButtonStyle.downFontColor = GameConstants.BUTTON_CLICK_COLOR;
        textButtonStyle.disabledFontColor = GameConstants.BUTTON_DISABLED_COLOR;
        generator.dispose();

        return textButtonStyle;
    }

    private FreeTypeFontGenerator.FreeTypeFontParameter createFontParameter(int size, Color color) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;

        return parameter;
    }

    public TextField.TextFieldStyle generateTextFieldStyle(GameFontSizeEnum gameFontSizeEnum, Color color) {
        BitmapFont bitmapFont = getFont();

        if (gameFontSizeEnum == GameFontSizeEnum.SMALL) {
            bitmapFont = getSmallFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.NORMAL) {
            bitmapFont = getFont();
        } else if (gameFontSizeEnum == GameFontSizeEnum.LARGE) {
            bitmapFont = getLargeFont();
        }

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = bitmapFont;
        textFieldStyle.fontColor = color;

        return textFieldStyle;
    }

    public TextField.TextFieldStyle generateTextFieldStyle(int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = generator.generateFont(createFontParameter(size, color));
        generator.dispose();

        return textFieldStyle;
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }

    public BitmapFont getFont() {
        return font;
    }

    public BitmapFont getLargeFont() {
        return largeFont;
    }
}
