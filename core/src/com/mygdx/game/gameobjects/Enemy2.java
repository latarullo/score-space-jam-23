package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameConstants;
import com.mygdx.game.MyGdxGame;

public class Enemy2 extends AbstractEnemy {
    private boolean movingDown = true;

    public Enemy2(MyGdxGame game) {
        super(game);
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("enemy2"), Animation.PlayMode.LOOP);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float x = this.getX();
        float y = this.getY();

        if (movingDown){
            y -= speed * delta;
            if (y < 0){
                movingDown = false;
            }
        } else {
            y += speed * delta;
            if (y > GameConstants.HEIGHT - this.getHeight()){
                movingDown = true;
            }
        }

        this.setPosition(x, y);
    }

    @Override
    public void shoot() {
    }
}
