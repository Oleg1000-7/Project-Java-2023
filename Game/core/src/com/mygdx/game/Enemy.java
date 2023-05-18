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
        boolean[] directions = new boolean[]{true, true, true, true};
        for(Enemy enemy : MyGdxGame.enemies) {
            if(this.intersects(enemy)){
                double dx = Math.abs(enemy.centerX - this.centerX);
                double dy = Math.abs(enemy.centerY - this.centerY);
                if(dx>dy) {
                    if (enemy.centerX > this.centerX) directions[0] = false;
                    else if (enemy.centerX < this.centerX) directions[1] = false;
                }
                else {
                    if (enemy.centerY > this.centerY) directions[2] = false;
                    else if (enemy.centerY < this.centerY) directions[3] = false;
                }
            }
        }
        if(!this.intersects(MyGdxGame.player)) {
            if (this.x < screenCenterX && directions[0]) this.x += this.speed;
            else if (this.x > screenCenterX && directions[1]) this.x -= this.speed;

            if (this.y < screenCenterY && directions[2]) this.y += this.speed;
            else if (this.y > screenCenterY && directions[3]) this.y -= this.speed;
        }
        super.update();
    }
}