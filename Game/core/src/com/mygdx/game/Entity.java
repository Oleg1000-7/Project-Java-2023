package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.awt.*;
import com.badlogic.gdx.utils.Array;

public class Entity extends Rectangle {
    Texture image, oimage;
    float x,y;
    double width,height;
    int speed;

    public Entity(String image, String oimage, int x, int y, int speed, Array<Entity> entityArray) {
        this.image = new Texture(Gdx.files.internal(image));
        this.oimage = new Texture(Gdx.files.internal(oimage));
        this.x = x;
        this.y = y;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.speed = speed;
        entityArray.add(this);
        this.setRect(this.x, this.y, this.width, this.height);
    }

    void draw(Batch batch, boolean is_drawn){
        if(is_drawn || oimage == image) {
            batch.draw(this.image, this.x, this.y);
        } else batch.draw(this.oimage, this.x, this.y);
    }
    void update(){
        this.setRect(this.x, this.y, this.width, this.height);
    }
}
