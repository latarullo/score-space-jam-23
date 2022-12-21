package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TypingLabel extends Label {
    private float stateTime = 0;
    private CharSequence fullText;
    private boolean typingComplete = false;
    private float startDelay = 0;

    public TypingLabel(CharSequence text, LabelStyle style, float startDelay) {
        super("", style);
        this.fullText = text;
        this.startDelay = startDelay;
    }

    public TypingLabel(CharSequence text, LabelStyle style) {
        super("", style);
        this.fullText = text;
    }

    @Override
    public void act(float delta) {
        if (typingComplete) {
            return;
        }

        super.act(delta);
        stateTime += delta;
        if (stateTime < startDelay){
            return;
        }
        int fullTextSize = this.fullText.length();
        float paceRate = (stateTime - startDelay) / 5;

        int charsToWrite;
        charsToWrite = (int) (fullTextSize * paceRate);
        if (charsToWrite > fullTextSize) {
            charsToWrite = fullTextSize;
            typingComplete = true;
        }

        this.setText(fullText.subSequence(0, charsToWrite).toString());
    }

    public boolean isTypingComplete() {
        return typingComplete;
    }
}