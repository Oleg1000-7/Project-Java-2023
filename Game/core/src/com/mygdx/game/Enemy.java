package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class Enemy extends Entity{
    public Enemy(String image, String oimage, int x, int y, int speed, Array<Entity> entityArray) {
        super(image, oimage, x, y, speed, entityArray);
    }
}