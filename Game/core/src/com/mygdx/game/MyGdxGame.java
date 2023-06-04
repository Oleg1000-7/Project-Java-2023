package com.mygdx.game;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import javax.naming.Context;

public class MyGdxGame implements ApplicationListener {

	//File file = new File("C:/Users/user/IdeaProjects/test/assets/walls.txt");

	Scanner scanner;

	{
			scanner = new Scanner(
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"######........k...............................................d.....\n"+
					"##################.#################################################\n"+
					"######.............#..........###.............................######\n"+
					"######.#####################..#...............................######\n"+
					"######..........................#.............................######\n"+
					"#################################.............................######\n"+
					"#################################.............................######\n"+
					"######....................d...................................######\n"+
					"######..##..##..##...#....##############.############.######.#######\n"+
					"######..##..##..##........#..##..........###..............##..######\n"+
					"##############..############.#..#...##...###..####..####..###.######\n"+
					"##############..###########....###..##...###..####..####..##..######\n"+
					"##############..##################..##...###..####..####..##.#######\n"+
					"######...........................#..##....................##..######\n"+
					"######d#######..################.#.##########################.######\n"+
					"######.#######..#................#............................######\n"+
					"######.#######..#..############..#..##..##..##..##..##..##..########\n"+
					"######.#..#.....#................##################.################\n"+
					"######.#........#..............................####k################\n"+
					"######.#######################################.#####################\n"+
					"######.....................########...........................######\n"+
					"######.........###.........##################.##..##..##..##..######\n"+
					"######.........###.........#.......##########.##..##..##..##..######\n"+
					"######.....................#.......##########.................######\n"+
					"############d############..#####..###########.##..##..##..##..######\n"+
					"######..k.....#...................####.k....#.##..##..##..##..######\n"+
					"#####.........##################..####..##..#.................######\n"+
					"#####.........#...................####..##..#####.##################\n"+
					"######........#........k..........####........................######\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################\n"+
					"####################################################################");
	}

	SpriteBatch batch;
	Random random;
	OrthographicCamera camera;
	static Array<Entity> entityArray;
	static ArrayList<Enemy> enemies;
	boolean [] move;
	boolean is_menu, is_enemy;
	static public Player player;
	float currentPlayerSpeed;
	static int radius_a, keysNumber, move_time, animation_time;

	@Override
	public void create() {
		random = new Random();
		entityArray = new Array<>();
		enemies = new ArrayList<>();
		move = new boolean[]{true, true, true, true};
		is_menu = true;
		radius_a = 500;
		keysNumber = 0;
		animation_time = 16;


		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("player1.png", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 250, entityArray, true, 100);

		int y = -36;
		while(scanner.hasNext()){
			char[] walls = scanner.nextLine().toCharArray();
			for (int x = 0; x < walls.length; x++){
				int spawnX = x*100;
				int spawnY = -y*100;
				if(walls[x]=='#') new Entity("wall.png", spawnX, spawnY, 0, entityArray, true);
				if(walls[x]=='.') new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
				if(walls[x]=='k'){
					new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
					new Item("key.png", spawnX, spawnY, 0, entityArray, true);
				}
				if(walls[x]=='d'){
					new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
					new Door("door.png", spawnX, spawnY, 0, entityArray, true);
				}
			}
			y++;
		}
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (is_menu) {
			if (Gdx.input.isKeyPressed(Keys.Q)) Gdx.app.exit();
			if (Gdx.input.isKeyPressed(Keys.ENTER)) is_menu = false;
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			camera.update();
		} else {
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) is_menu = true;
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();

			currentPlayerSpeed = player.speed * Gdx.graphics.getDeltaTime();

			move = new boolean[]{true, true, true, true};

			for (Entity entity : entityArray) {
				is_enemy = true;
				for (Enemy x: enemies){
					if (entity == x && !(Math.pow(entity.x - player.x, 2) + Math.pow(entity.y - player.y, 2) < 280 * 280)){
						is_enemy = false;
						break;
					}
				}
				if(entity != entityArray.get(0) && is_enemy){
					entity.draw(batch, Math.pow(entity.x - player.x, 2) + Math.pow(entity.y - player.y, 2) < 280 * 280);
					entity.update();

					int pos = player.movement(entity);

					if (pos != -1) move[pos] = false;
				}
			}
			if (!player.is_running) animation_time = 16;
			else animation_time = 8;
			if (
					Gdx.input.isKeyPressed(Keys.A) ||
							Gdx.input.isKeyPressed(Keys.D) ||
							Gdx.input.isKeyPressed(Keys.W) ||
							Gdx.input.isKeyPressed(Keys.S)){
				if (move_time == animation_time * 2) move_time = 0;
				if (move_time > animation_time){
					player.image = new Texture(Gdx.files.internal("player1.png"));
				} else{
					player.image = new Texture(Gdx.files.internal("player2.png"));
				}
				move_time++;
			}
			if(!player.is_invisible){
				player.draw(batch, true);
				player.update();
			}

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

			int x = random.nextInt(Gdx.graphics.getDisplayMode().width + 400) - 200;
			int y = random.nextInt(Gdx.graphics.getDisplayMode().height + 400) - 200;
			if ((x <= -100 || y <= -100 || x >= Gdx.graphics.getDisplayMode().width || y >= Gdx.graphics.getDisplayMode().height) && enemies.size() < 5){
				enemies.add(new Enemy("angry_enemy.png", x, y, 4, entityArray, true, 10));
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
