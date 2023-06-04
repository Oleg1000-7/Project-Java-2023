package com.mygdx.game;

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

	Scanner scanner;

	SpriteBatch batch;
	Random random;
	OrthographicCamera camera;
	static Array<Entity> entityArray;
	static ArrayList<Enemy> enemies;
	boolean [] move;
	boolean is_menu;
	static public Player player;
	float currentPlayerSpeed;
	static int radius_a, keysNumber, move_time, animation_time, doorsNumber;
	static long curTime;
	static Button start_button, exit_button, pause_button;

	@Override
	public void create() {
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
							"######.............#..........###........###########################\n"+
							"######.#####################..#..........###########################\n"+
							"######..........................#........###########################\n"+
							"########################################d###########################\n"+
							"########################################.###########################\n"+
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

		random = new Random();
		entityArray = new Array<>();
		enemies = new ArrayList<>();
		move = new boolean[]{true, true, true, true};
		is_menu = true;
		radius_a = 500;
		keysNumber = 0;
		animation_time = 16;
		curTime = 0;


		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("player1.png", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 250, entityArray, true, 100);

		start_button = new Button("start.png", "start.png", Gdx.graphics.getDisplayMode().width/2 - Gdx.graphics.getDisplayMode().width/10, Gdx.graphics.getDisplayMode().height - Gdx.graphics.getDisplayMode().height/5);
		exit_button = new Button("exit.png", "exit.png", Gdx.graphics.getDisplayMode().width/2 + Gdx.graphics.getDisplayMode().width/10, Gdx.graphics.getDisplayMode().height - Gdx.graphics.getDisplayMode().height/5);
		pause_button = new Button("pause.png", "pause.png", Gdx.graphics.getDisplayMode().width - Gdx.graphics.getDisplayMode().width/20, Gdx.graphics.getDisplayMode().height - Gdx.graphics.getDisplayMode().height/5);

		int y = -36;
		while(scanner.hasNext()){
			char[] map = scanner.nextLine().toCharArray();
			for (int x = 0; x < map.length; ++x){
				int spawnX = x*100;
				int spawnY = -y*100;
				if(map[x]=='#') new Entity("wall.png", spawnX, spawnY, 0, entityArray, true);
				if(map[x]=='.') new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
				if(map[x]=='k'){
					new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
					new Item("key.png", spawnX, spawnY, 0, entityArray, true);
				}
				if(map[x]=='d'){
					new Entity("floor.png", spawnX, spawnY, 0, entityArray, false);
					new Door("door.png", spawnX, spawnY, 0, entityArray, true);
				}
			}
			y++;
		}
	}

	@Override
	public void render() {
		if (is_menu) {
			if (Gdx.input.isTouched()) {
				if (Gdx.input.getX() > exit_button.x && Gdx.input.getX() < exit_button.x + exit_button.width && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() > exit_button.y && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() < exit_button.y + exit_button.height) Gdx.app.exit();
				if (Gdx.input.getX() > start_button.x && Gdx.input.getX() < start_button.x + start_button.width && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() > start_button.y && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() < start_button.y + start_button.height) is_menu = false;
			}
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			start_button.draw(batch, false);
			exit_button.draw(batch, false);
			camera.update();
			batch.end();
		} else if(doorsNumber < 5 && player.healthPoints > 0){
			curTime = System.currentTimeMillis();
			if (Gdx.input.isTouched() && Gdx.input.getX() > pause_button.x && Gdx.input.getX() < pause_button.x + pause_button.width && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() > pause_button.y && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() < pause_button.y + pause_button.height) is_menu = true;
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();

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
			if (!player.is_running) animation_time = 16;
			else animation_time = 8;
			if (
					Gdx.input.isKeyPressed(Keys.A) ||
							Gdx.input.isKeyPressed(Keys.D) ||
							Gdx.input.isKeyPressed(Keys.W) ||
							Gdx.input.isKeyPressed(Keys.S)){
				if (move_time >= animation_time * 2) move_time = 0;
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
			pause_button.draw(batch, false);


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

			player.skills(skillKeys);

			int x = random.nextInt(Gdx.graphics.getDisplayMode().width + 400) - 200;
			int y = random.nextInt(Gdx.graphics.getDisplayMode().height + 400) - 200;
			if ((x <= -100 || y <= -100 || x >= Gdx.graphics.getDisplayMode().width || y >= Gdx.graphics.getDisplayMode().height) && enemies.size() < 3){
				enemies.add(new Enemy("angry_enemy.png", x, y, 4, entityArray, true, 10));
			}
		}
		else if(player.healthPoints <= 0){
			player.image = new Texture(Gdx.files.internal("player3.jpg"));
		if (Gdx.input.isTouched()) {
			if (Gdx.input.getX() > exit_button.x && Gdx.input.getX() < exit_button.x + exit_button.width && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() > exit_button.y && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() < exit_button.y + exit_button.height) Gdx.app.exit();
			if (Gdx.input.getX() > start_button.x && Gdx.input.getX() < start_button.x + start_button.width && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() > start_button.y && Gdx.graphics.getDisplayMode().height - Gdx.input.getY() < start_button.y + start_button.height) { create(); is_menu = false;}
		}
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		player.draw(batch, true);
		start_button.draw(batch, false);
		exit_button.draw(batch, false);
		camera.update();
		batch.end();
		}
		else{create();}
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
