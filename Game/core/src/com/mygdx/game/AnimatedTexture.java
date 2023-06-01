package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class AnimatedTexture {
    ArrayList<Texture> textures;
    float currentTime = 0;
    float SPRITE_TIME = 0.1f;
    int currentFrame = 0;

    public AnimatedTexture(ArrayList<Texture> textures) {
        this.textures = textures;
        this.currentFrame = 0;
    }

    void update(float deltaTime){
        currentTime += deltaTime;
        if (currentTime >= SPRITE_TIME){
            currentFrame = (currentFrame + 1) % textures.size();
            currentTime -= SPRITE_TIME;
        }
    }

    //void draw(Batch batch, float deltaTime){
    //    update(deltaTime);
    //    batch.draw(textures.get(currentFrame), ...);
    //}
}
