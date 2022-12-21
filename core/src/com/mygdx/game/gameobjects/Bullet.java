package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Bullet extends Actor implements Disposable {
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private int bulletWidth = 32;
    private int bulletHeight = 32;
    private boolean bulletUpOrientation = true;
    private float moveSpeed = 200;

    public Bullet() {
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/bullet.atlas"));
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("bullet"), Animation.PlayMode.LOOP);
        this.setWidth(bulletWidth/2);
        this.setHeight(bulletHeight/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimation = animation.getKeyFrame(stateTime, true);
        currentAnimation.flip(false, !bulletUpOrientation);
        batch.draw(currentAnimation, this.getX(), this.getY(), bulletWidth, bulletHeight);
        if (currentAnimation.isFlipY()){
            currentAnimation.flip(false, true);
        }
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
    }

    public boolean isBulletUpOrientation() {
        return bulletUpOrientation;
    }

    public void setBulletUpOrientation(boolean bulletUpOrientation) {
        this.bulletUpOrientation = bulletUpOrientation;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
