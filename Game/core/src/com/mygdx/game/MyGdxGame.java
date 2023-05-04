package com.mygdx.game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame implements ApplicationListener {
	Texture LightImage;

	Path path = Paths.get("C:/Users/CUBER/AndroidStudioProjects/Project-Java-2023/Game/assets/walls.txt");
	Scanner scanner;
	{
		try {
			scanner = new Scanner(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	SpriteBatch batch;
	OrthographicCamera camera;
	Array<Entity> entityArray = new Array<>();
	boolean is_menu;
	Player player;

	@Override
	public void create() {
		is_menu = true;
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("seregapirat.jpg", "seregapirat.jpg", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 250, entityArray);

		int y = 0;
		while(scanner.hasNext()){
			char[] walls = scanner.nextLine().toCharArray();
			for (int x =  0; x < walls.length; ++x){
				if(walls[x]=='0') new Entity("wall.jpg", "point.png", x * 100, y * 100, 0, entityArray);
			}
			y++;
		}

		LightImage = new Texture(Gdx.files.internal("ground/foreground1.png"));
	}

	@Override
	public void render() {
		if (is_menu) {
			if (Gdx.input.isKeyPressed(Keys.Q)) Gdx.app.exit();
			if (Gdx.input.isKeyPressed(Keys.ENTER)) is_menu = false;
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			camera.update();
		} else {
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) is_menu = true;
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();

			batch.draw(LightImage, player.x + (player.width - LightImage.getWidth()) / 2, player.y + (player.height - LightImage.getHeight()) / 2);

			for (Entity entity : entityArray) {
				entity.draw(batch, (entity.x - player.x) * (entity.x - player.x) + (entity.y - player.y) * (entity.y - player.y) < 250 * 250);
			}

			batch.end();

			for (Entity entity : entityArray) {
				if (entity != entityArray.get(0)) {
					if (Gdx.input.isKeyPressed(Keys.A)) {
						entity.x += player.speed * Gdx.graphics.getDeltaTime();
					}

					if (Gdx.input.isKeyPressed(Keys.D)) {
						entity.x -= player.speed * Gdx.graphics.getDeltaTime();
					}

					if (Gdx.input.isKeyPressed(Keys.S)) {
						entity.y += player.speed * Gdx.graphics.getDeltaTime();
					}

					if (Gdx.input.isKeyPressed(Keys.W)) {
						entity.y -= player.speed * Gdx.graphics.getDeltaTime();
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
