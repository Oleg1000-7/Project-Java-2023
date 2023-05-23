package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

public class Item extends Entity{
    public Item(String image, String oimage, int x, int y, int speed, Array<Entity> entityArray) {
        super(image, oimage, x, y, speed, entityArray);
    }

    @Override
    void update() {
        super.update();
    }
}
