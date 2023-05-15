package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;


class Enemy extends Entity{
    public Enemy(String image, String oimage, int x, int y, int speed, Array<Entity> entityArray) {
        super(image, oimage, x, y, speed, entityArray);
    }

    @Override
    void update() {
        int screenCenterX = Gdx.graphics.getDisplayMode().width/2;
        int screenCenterY = Gdx.graphics.getDisplayMode().height/2;

        if(this.x < screenCenterX) this.x+=this.speed;
        else if(this.x > screenCenterX) this.x-=this.speed;
        if(this.y < screenCenterY) this.y+=this.speed;
        else if(this.y > screenCenterY) this.y-=this.speed;

        super.update();
    }
}