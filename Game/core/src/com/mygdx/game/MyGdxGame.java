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
	static Array<Entity> entityArray;
	static ArrayList<Enemy> enemies;
	boolean [] move;
	boolean is_menu;
	static public Player player;
	float currentPlayerSpeed;
	static int radius_a, keysNumber;
	@Override
	public void create() {
		random = new Random();
		entityArray = new Array<>();
		enemies = new ArrayList<>();
		move = new boolean[]{true, true, true, true};
		is_menu = true;
		radius_a = 650;
		keysNumber = 0;


		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("player1.png", "player1.png", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 500, entityArray, true);

		int y = -7;
		while(scanner.hasNext()){
			char[] walls = scanner.nextLine().toCharArray();
			for (int x =  0; x < walls.length; ++x){
				int spawnX = x*100;
				int spawnY = -y*100;
				if(walls[x]=='0') new Entity("wall.png", "point.jpg", spawnX, spawnY, 0, entityArray, true);
				if(walls[x]=='o') new Entity("seregapirat.jpg", "enemy.png", spawnX, spawnY, 0, entityArray, false);
				if(walls[x]=='k'){
					new Entity("seregapirat.jpg", "enemy.png", spawnX, spawnY, 0, entityArray, false);
					new Item("digits.png","point.jpg", spawnX, spawnY, 0, entityArray, true);
					}
				if(walls[x]=='d') new Door("wall_.jpg", "point.jpg", spawnX, spawnY, 0, entityArray, true);
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
				if(entity != entityArray.get(0)){
					entity.draw(batch, Math.pow(entity.x - player.x, 2) + Math.pow(entity.y - player.y, 2) < 280 * 280);
					entity.update();

					int pos = player.movement(entity);
					if (pos != -1) move[pos] = false;
				}
			}
			player.draw(batch, true);
			player.update();

			batch.end();

			boolean [] keyList = new boolean[]{
					Gdx.input.isKeyPressed(Keys.A),
					Gdx.input.isKeyPressed(Keys.D),
					Gdx.input.isKeyPressed(Keys.W),
					Gdx.input.isKeyPressed(Keys.S)
			};
			for (Entity entity : entityArray) {
				if (entity != entityArray.get(0)) {
					entity.wasd(keyList, move, currentPlayerSpeed);
				}
			}
			boolean [] skillKeys = new boolean[]{false, false};
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) skillKeys[0] = true;
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) skillKeys[1] = true;

			player.skills(skillKeys, System.currentTimeMillis());

			int x = random.nextInt(0, Gdx.graphics.getDisplayMode().width + 400) - 200;
			int y = random.nextInt(0, Gdx.graphics.getDisplayMode().height + 400) - 200;
			if ((x <= -100 || y <= -100 || x >= Gdx.graphics.getDisplayMode().width || y >= Gdx.graphics.getDisplayMode().height) && enemies.size() < 5 && 0==1){
				enemies.add(new Enemy("assets/enemy.jpg", "assets/enemy_.jpg", x, y, 5, entityArray, true));
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
