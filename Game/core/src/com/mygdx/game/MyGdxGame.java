package com.mygdx.game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
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

	Path path = Paths.get("assets/walls.txt");

	Scanner scanner;

	{
		try {
			scanner = new Scanner(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	SpriteBatch batch;
	Random random;
	OrthographicCamera camera;
	Array<Entity> entityArray;
	ArrayList<Enemy> enemies;
	boolean [] move;
	boolean is_menu;
	Player player;
	float currentPlayerSpeed;
	@Override
	public void create() {
		random = new Random();
		entityArray = new Array<>();
		enemies = new ArrayList<>();
		move = new boolean[]{true, true, true, true};
		is_menu = true;


		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("wall.jpg", "wall.jpg", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 250, entityArray);

		int y = 0;
		while(scanner.hasNext()){
			char[] walls = scanner.nextLine().toCharArray();
			for (int x =  0; x < walls.length; ++x){
				if(walls[x]=='0') new Entity("wall.jpg", "point.jpg", x * 100, -y * 100, 0, entityArray);
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

			batch.draw(
					LightImage,
					(float) (player.x + (player.width - LightImage.getWidth()) / 2),
					(float) (player.y + (player.height - LightImage.getHeight()) / 2)
			);

			currentPlayerSpeed = player.speed * Gdx.graphics.getDeltaTime();

			move = new boolean[]{true, true, true, true};

			for (Entity entity : entityArray) {
				entity.draw(batch, (entity.x - player.x) * (entity.x - player.x) + (entity.y - player.y) * (entity.y - player.y) < 250 * 250);
				entity.update();
				if(entity != entityArray.get(0)){
					int pos = player.movement(entity);
					if (pos != -1) move[pos] = false;
				}
			}

			batch.end();

			for (Entity entity : entityArray) {
				if (entity != entityArray.get(0)) {
					if (Gdx.input.isKeyPressed(Keys.A) && move[0]) {
						entity.x += currentPlayerSpeed;
					}

					if (Gdx.input.isKeyPressed(Keys.D) && move[1]) {
						entity.x -= currentPlayerSpeed;
					}

					if (Gdx.input.isKeyPressed(Keys.W) && move[2]) {
						entity.y -= currentPlayerSpeed;
					}

					if (Gdx.input.isKeyPressed(Keys.S) && move[3]) {
						entity.y += currentPlayerSpeed;
					}
				}
			}

			int x = random.nextInt(0, Gdx.graphics.getDisplayMode().width + 400) - 200;
			int y = random.nextInt(0, Gdx.graphics.getDisplayMode().height + 400) - 200;
			if (x <= -100 || y <= -100 || x >= Gdx.graphics.getDisplayMode().width || y >= Gdx.graphics.getDisplayMode().height && entityArray.size < 5){
				enemies.add(new Enemy("assets/enemy.jpg", "assets/enemy_.jpg", x, y, 0, entityArray));
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
