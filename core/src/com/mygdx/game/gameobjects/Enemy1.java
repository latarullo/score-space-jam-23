package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameConstants;
import com.mygdx.game.MyGdxGame;

public class Enemy1 extends AbstractEnemy implements Disposable {
    private boolean movingLeft = true;

    public Enemy1(MyGdxGame game) {
        super(game);
        this.animation = new Animation<TextureRegion>(1f / 5, atlas.findRegions("enemy1"), Animation.PlayMode.LOOP);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float x = this.getX();
        float y = this.getY();

        if (movingLeft){
            x -= speed * delta;
            if (x < 0){
                movingLeft = false;
                if (y > 200) {
                    y -= 20 + speed * delta;
                }
            }
        } else {
            x += speed * delta;
            if (x > GameConstants.WIDTH - this.getWidth()){
                movingLeft = true;
                if (y > 200) {
                    y -= 20 + speed * delta;
                }
            }
        }

        this.setPosition(x, y);
    }
}
