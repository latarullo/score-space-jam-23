package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameSoundPlayer;
import com.mygdx.game.MyGdxGame;

public abstract class AbstractEnemy extends Actor implements Disposable {
    protected TextureAtlas atlas;
    protected Animation<TextureRegion> animation;
    private float animationStateTime;
    private final Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
    protected final MyGdxGame game;
    protected float fireRate = 3;
    protected float shootStateTime = 0;
    private int enemyWidth = 64;
    private int enemyHeight = 64;
    protected float speed = 200;
    private int shipsCollisionMargin = 20;


    public AbstractEnemy(MyGdxGame game) {
        this.game = game;
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/enemies.atlas"));
        this.fireRate = (float) (this.fireRate + Math.random());
        this.setWidth(64);
        this.setHeight(64);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        animationStateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimation = animation.getKeyFrame(animationStateTime, true);
        batch.draw(currentAnimation, this.getX(), this.getY(), enemyWidth, enemyHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shootStateTime += delta;
        if (shootStateTime >= fireRate) {
            shoot();
            shootStateTime = 0;
        }


        boolean overlapsX = (this.getX() >= game.getSpaceship().getX() + shipsCollisionMargin && this.getX() <= game.getSpaceship().getX() + game.getSpaceship().getWidth() - shipsCollisionMargin)
                || (this.getX() + this.getWidth() >= game.getSpaceship().getX() + shipsCollisionMargin && this.getX() + this.getWidth() <= game.getSpaceship().getX() + game.getSpaceship().getWidth() - shipsCollisionMargin);

//        (this.getX() >= game.getSpaceship().getX() + errorMargin && this.getX() <= game.getSpaceship().getX() + game.getSpaceship().getWidth() - errorMargin )
//                || (this.getX() + this.getWidth() >= game.getSpaceship().getX() + errorMargin && this.getX() + this.getWidth() <= game.getSpaceship().getX() + game.getSpaceship().getWidth() - errorMargin);
//
        boolean overlapsY = (this.getY() >= game.getSpaceship().getY() + shipsCollisionMargin &&
                this.getY() <= game.getSpaceship().getY() + game.getSpaceship().getHeight() - shipsCollisionMargin) ||
                (this.getY() + this.getHeight() >= game.getSpaceship().getY() + shipsCollisionMargin &&
                        this.getY()  + this.getHeight() <= game.getSpaceship().getY() + game.getSpaceship().getHeight() - shipsCollisionMargin)
                ;

        if (overlapsX && overlapsY) {
            game.getSpaceship().wasHit();
        }
    }

    public void shoot() {
        GameSoundPlayer.playSound(shootSound);

        Bullet bullet = new Bullet();
        bullet.setPosition(this.getX() + this.getWidth() / 2 - bullet.getWidth(), this.getY() + this.getHeight() / 2);
        bullet.setOrigin(Align.center);
        bullet.setSize(16, 16);
        bullet.setBulletUpOrientation(false);
        this.getParent().addActor(bullet);

        bullet.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                getActor().setY(getActor().getY() - 200 * Gdx.graphics.getDeltaTime());
                checkCollisionWithSpaceship();
                return false;
            }

            private void checkCollisionWithSpaceship() {
                Bullet bulletImage = (Bullet) getActor();
                boolean overlapsX = bulletImage.getX() >= game.getSpaceship().getX() &&
                        bulletImage.getX() <= game.getSpaceship().getX() + game.getSpaceship().getWidth();

                boolean overlapsY = bulletImage.getY() >= game.getSpaceship().getY() &&
                        bulletImage.getY() <= game.getSpaceship().getY() + game.getSpaceship().getHeight();

                if (overlapsX && overlapsY) {
                    destroyBullet();
                    game.getSpaceship().wasHit();
                }

                if (bulletImage.getY() <= 0) {
                    destroyBullet();
                }
            }

            private void destroyBullet() {
                getActor().remove();
            }
        });
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.shootSound.dispose();
    }

    public void increaseSpeed(float v) {
        speed *= v;
    }

    public void increateFireRate(float v) {
        fireRate /= v;
    }
}
