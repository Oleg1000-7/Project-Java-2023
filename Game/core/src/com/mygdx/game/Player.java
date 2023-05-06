package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class Player extends Entity{
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
}