package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class Player extends Entity{
    long cooldown1;
    public Player(String image, String oimage, int x, int y, int speed, Array<Entity> entityArray) {
        super(image, oimage, x, y, speed, entityArray);
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
    void skills(boolean [] keys, long time){
        if (keys[0] && time - cooldown1 > 8000){
            this.cooldown1 = System.currentTimeMillis();
            this.speed*=1.5;
        }
        else if (time - cooldown1 > 8000 && cooldown1>0){
            this.speed/=1.5;
            this.cooldown1 = -1;
        }
    }
}