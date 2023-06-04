package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

class Player extends Entity{
    long cooldown1, cooldown2;
    boolean is_invisible, is_running;
    int healthPoints;

    public Player(String image, float x, float y, float speed, Array<Entity> entityArray, boolean collideable, int healthPoints) {
        super(image, x, y, speed, entityArray, collideable);
        this.is_invisible = false;
        this.is_running = false;
        this.healthPoints = healthPoints;
    }

    int movement(Entity entity){
        if (this.intersects(entity)){
            double dx = Math.abs(entity.centerX - this.centerX);
            double dy = Math.abs(entity.centerY - this.centerY);

            if(dx>dy){
                if (entity.centerX>this.centerX) return 1;
                else return 0;
            }
            else {
                if (entity.centerY>this.centerY) return 2;
                else return 3;
            }

        }else return -1;
    }
    void skills(boolean [] keys){
        long time = MyGdxGame.curTime;
        if (keys[0] && time - cooldown1 > 8000 && !is_running){
            this.cooldown1 = time;
            this.speed*=2;
            MyGdxGame.radius_a = Gdx.graphics.getDisplayMode().width/2;
            this.is_running = true;
        }
        else if (time - cooldown1 > 2000 && is_running){
            this.speed/=2;
            this.cooldown1 = time;
            MyGdxGame.radius_a = 650;
            this.is_running = false;
        }

        if (keys[1] && time - cooldown2 > 10000 && !is_invisible){
            this.cooldown2 = System.currentTimeMillis();
            this.is_invisible = true;
        }
        else if (time - cooldown2 > 3000 && is_invisible){
            this.cooldown2 = time;
            this.is_invisible = false;
        }

    }
}