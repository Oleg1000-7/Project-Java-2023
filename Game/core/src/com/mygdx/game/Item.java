package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Item extends Entity{


    public Item(String image, float x, float y, float speed, Array<Entity> entityArray, boolean collideable) {
        super(image, x, y, speed, entityArray, collideable);
        this.oimage = new Texture(Gdx.files.internal("key.png"));
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
