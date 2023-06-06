package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Rectangle;

public class Button extends Rectangle {
    Texture image, pressed_image;
    float x, y;
    double width, height;
    public Button(String image_path, String pressed_image_path,float x, float y){
        this.image = new Texture(Gdx.files.internal(image_path));
        this.pressed_image = new Texture(Gdx.files.internal(pressed_image_path));
        this.x = x;
        this.y = y;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
    }

    void draw(Batch batch, boolean is_pressed){
        if(is_pressed) batch.draw(this.pressed_image, this.x, this.y);
        else batch.draw(this.image, this.x, this.y);
    }
}
