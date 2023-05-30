package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Door extends Entity{
    Texture image2;
    boolean isOpened;

    public Door(String image, String oimage, float x, float y, int speed, Array<Entity> entityArray, boolean collideable) {
        super(image, oimage, x, y, speed, entityArray, collideable);
        this.isOpened = false;
    }


    @Override
    void update() {
        if (!isOpened) super.update();
        if (MyGdxGame.keysNumber > 0 && this.intersects(MyGdxGame.player)) {
            MyGdxGame.keysNumber -= 1;
            this.isOpened = true;
            this.image = this.image2;
            MyGdxGame.entityArray.removeIndex(MyGdxGame.entityArray.indexOf(this, false));
            this.setRect(0,0,0,0);
            new Door("bucket.jpg", "point.jpg", this.x, this.y, 0, MyGdxGame.entityArray, false);

        }
    }
}
