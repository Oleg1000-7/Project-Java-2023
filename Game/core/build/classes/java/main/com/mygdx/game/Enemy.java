package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.collision.Ray;

class Enemy extends Entity{

    int damage;
    long attackCooldown;

    public Enemy(String image, float x, float y, float speed, Array<Entity> entityArray, boolean collideable, int damage) {
        super(image, x, y, speed, entityArray, collideable);
        this.damage = damage;
        this.attackCooldown = 0;
    }

    @Override
    void update() {
        int screenCenterX = Gdx.graphics.getDisplayMode().width/2;
        int screenCenterY = Gdx.graphics.getDisplayMode().height/2;
        boolean[] directions = new boolean[]{true, true, true, true};
        for(Enemy enemy : MyGdxGame.enemies) {
            if(this.overlaps(enemy)){
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
        double cos = Math.abs(this.centerX-screenCenterX);
        double sin = Math.abs(this.centerY-screenCenterY);
        double gip =  Math.hypot(cos, sin);
        if(!this.overlaps(MyGdxGame.player) && gip < MyGdxGame.radius_a && !MyGdxGame.player.is_invisible) {
            cos /= gip;
            sin /= gip;

            if (this.x < screenCenterX && directions[0]) this.x += this.speed*cos;
            else if (this.x > screenCenterX && directions[1]) this.x -= this.speed*cos;

            if (this.y < screenCenterY && directions[2]) this.y += this.speed*sin;
            else if (this.y > screenCenterY && directions[3]) this.y -= this.speed*sin;
        }
        if(this.overlaps(MyGdxGame.player)){
            if(MyGdxGame.curTime - this.attackCooldown > 1000) {
                MyGdxGame.player.healthPoints -= this.damage;
                this.attackCooldown = MyGdxGame.curTime;
            }
        }
        super.update();
        if(gip > 2000){
            MyGdxGame.enemies.remove(this);
            MyGdxGame.entityArray.removeIndex(MyGdxGame.entityArray.indexOf(this, false));
            this.set(99999, 99999, 0, 0);
        }
    }
}