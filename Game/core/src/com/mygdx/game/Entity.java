package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.awt.*;
import com.badlogic.gdx.utils.Array;

public class Entity extends Rectangle {
    Texture image, oimage;
    float x,y;
    double width,height,owidth,oheight,centerX,centerY;
    int speed;
    boolean collideable;
    public Entity(String image, String oimage, float x, float y, int speed, Array<Entity> entityArray, boolean collideable) {
        this.image = new Texture(Gdx.files.internal(image));
        this.oimage = new Texture(Gdx.files.internal(oimage));
        this.x = x;
        this.y = y;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.owidth = this.oimage.getWidth();
        this.oheight = this.oimage.getHeight();
        this.speed = speed;
        entityArray.add(this);
        this.collideable = collideable;
        if(collideable) {
            this.setRect(this.x, this.y, this.width, this.height);
            this.centerX = this.getCenterX();
            this.centerY = this.getCenterY();
        }else{
            this.centerX = this.x + 50;
            this.centerY = this.y + 50;
        }
    }

    void draw(Batch batch, boolean is_drawn){
        if(is_drawn || oimage == image) {
            batch.draw(this.image, this.x, this.y);
        } else batch.draw(this.oimage,
                (float) (this.centerX - this.owidth/2),
                (float) (this.centerY - this.oheight/2));
    }
    void update(){
        if(collideable){
            this.setRect(this.x, this.y, this.width, this.height);
            this.centerX = this.getCenterX();
            this.centerY = this.getCenterY();
        }
        else{
            this.centerX = this.x + 50;
            this.centerY = this.y + 50;
        }
    }
    void wasd(boolean [] keys, boolean [] move, float speed){
        if(!move[0]) this.x -= 1;
        if(!move[1]) this.x += 1;
        if(!move[2]) this.y += 1;
        if(!move[3]) this.y -= 1;

        if (keys[0] && move[0]) this.x += speed;
        if (keys[1] && move[1]) this.x -= speed;
        if (keys[2] && move[2]) this.y -= speed;
        if (keys[3] && move[3]) this.y += speed;
    }
}
