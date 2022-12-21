package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Star extends Actor implements Disposable {
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public Star() {
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/star.atlas"));
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("Star"), Animation.PlayMode.LOOP);
        this.setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimation = animation.getKeyFrame(stateTime, true);
        batch.draw(currentAnimation, this.getX(), this.getY(), 16, 16);
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
    }
}
