package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameSoundPlayer;

public class Explosion extends Actor implements Disposable {
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
    private float explosionWidth = 32;
    private float explosionHeight = 32;

    public Explosion() {
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/explosions.atlas"));
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("explosion"), Animation.PlayMode.LOOP);
        GameSoundPlayer.playSound(explosionSound);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimation = animation.getKeyFrame(stateTime, true);

        if (!animation.isAnimationFinished(stateTime)) {
            batch.draw(currentAnimation, this.getX(), this.getY(), explosionWidth, explosionHeight);
        } else {
            this.remove();
        }
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.explosionSound.dispose();
    }
}
