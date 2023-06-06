package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Door extends Entity{
    Texture image2;
    boolean isOpened;

    public Door(String image, float x, float y, int speed, Array<Entity> entityArray, boolean collideable) {
        super(image, x, y, speed, entityArray, collideable);
        this.isOpened = false;
    }


    @Override
    void update() {
        if (!isOpened) super.update();
        if (MyGdxGame.keysNumber > 0 && this.overlaps(MyGdxGame.player)) {
            MyGdxGame.keysNumber -= 1;
            this.isOpened = true;
            this.image = this.image2;
            MyGdxGame.entityArray.removeIndex(MyGdxGame.entityArray.indexOf(this, false));
            this.set(0,0,0,0);
            new Door("opened_door.png", this.x, this.y-30, 0, MyGdxGame.entityArray, false);
            MyGdxGame.doorsNumber++;
        }
    }
}
