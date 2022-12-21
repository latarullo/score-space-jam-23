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
import com.mygdx.game.GameConstants;
import com.mygdx.game.GameOverScreen;
import com.mygdx.game.GameSoundPlayer;
import com.mygdx.game.MyGdxGame;

public class Spaceship extends Actor implements Disposable {
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private final Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
    private final MyGdxGame game;
    public boolean isMoving = false;
    private int spaceshipWidth = 64;
    private int spaceshipHeight = 64;
    private float moveSpeed = 200;

    public Spaceship(MyGdxGame game) {
        this.game = game;
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/spaceship.atlas"));
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("spaceship"), Animation.PlayMode.LOOP);
        this.setWidth(spaceshipWidth);
        this.setHeight(spaceshipHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentAnimation = null;
        if (isMoving) {
            currentAnimation = animation.getKeyFrame(stateTime, true);
        } else {
            currentAnimation = animation.getKeyFrame(0);
        }

        batch.draw(currentAnimation, this.getX(), this.getY(), spaceshipWidth, spaceshipHeight);

        if (isMoving && animation.isAnimationFinished(stateTime)) {
            isMoving = false;
        }
    }

    public void shoot() {
        GameSoundPlayer.playSound(shootSound);
        final Bullet bullet = new Bullet();
        bullet.setPosition(this.getX() + spaceshipWidth / 2 - bullet.getWidth(), this.getY() + spaceshipHeight / 2);
        this.getParent().addActor(bullet);
        bullet.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                getActor().setY(getActor().getY() + bullet.getMoveSpeed() * Gdx.graphics.getDeltaTime());
                checkCollision();
                return false;
            }

            private void checkCollision() {
                Bullet bulletObject = (Bullet) getActor();
                AbstractEnemy enemyToRemove = null;
                for (AbstractEnemy enemyShip : game.getEnemies()) {
                    boolean overlapsX = bulletObject.getX() >= enemyShip.getX() &&
                            bulletObject.getX() <= enemyShip.getX() + enemyShip.getWidth();

                    boolean overlapsY = bulletObject.getY() >= enemyShip.getY() &&
                            bulletObject.getY() <= enemyShip.getY() + enemyShip.getHeight();

                    if (overlapsX && overlapsY) {
                        game.setScore(game.getScore() + 1);

                        createExplosion(enemyShip);

                        enemyShip.remove();
                        enemyToRemove = enemyShip;
                        destroyBullet();
                        break;
                    }
                }
                game.getEnemies().remove(enemyToRemove);

                if (bulletObject.getY() > Gdx.graphics.getHeight()) {
                    destroyBullet();
                }
            }

            private void createExplosion(final AbstractEnemy enemyShip) {
                Explosion explosion = new Explosion();
                float enemyX = enemyShip.getX();
                float enemyWidth = enemyShip.getWidth();
                float enemyY = enemyShip.getY();
                float enemyHeight = enemyShip.getHeight();
                explosion.setOrigin(Align.center);
                explosion.setPosition(enemyX + enemyWidth / 2, enemyY + enemyHeight / 2);

                getActor().getParent().addActor(explosion);
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

    public void moveLeft(float deltaTime) {
        float x = getX() - moveSpeed * deltaTime;
        if (x < 0 - spaceshipWidth / 2) {
            x = 0 - spaceshipWidth / 2;
        }
        this.setX(x);
        this.isMoving = true;
    }

    public void moveRight(float deltaTime) {
        float x = getX() + moveSpeed * deltaTime;
        if (x > GameConstants.WIDTH - spaceshipWidth / 2) {
            x = GameConstants.WIDTH - spaceshipWidth / 2;
        }
        this.setX(x);
        this.isMoving = true;
    }

    public void moveDown(float deltaTime) {
        float y = getY() - moveSpeed * deltaTime;
        if (y < 0) {
            y = 0;
        }
        this.setY(y);
        this.isMoving = true;
    }

    public void moveUp(float deltaTime) {
        float y = getY() + moveSpeed * deltaTime;
        if (y > GameConstants.HEIGHT - spaceshipWidth) {
            y = GameConstants.HEIGHT - spaceshipWidth;
        }
        this.setY(y);
        this.isMoving = true;
    }

    public void wasHit() {
        game.setScreen(new GameOverScreen(game));
    }
}
