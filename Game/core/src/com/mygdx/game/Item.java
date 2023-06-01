package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

public class Item extends Entity{


    public Item(String image, float x, float y, float speed, Array<Entity> entityArray, boolean collideable) {
        super(image, x, y, speed, entityArray, collideable);
    }

    @Override
    void update() {
        super.update();
        if(this.intersects(MyGdxGame.player)){
            MyGdxGame.entityArray.removeIndex(MyGdxGame.entityArray.indexOf(this, false));
            ifTaken();
        }

    }
    void ifTaken(){
        MyGdxGame.keysNumber++;
    }
}
