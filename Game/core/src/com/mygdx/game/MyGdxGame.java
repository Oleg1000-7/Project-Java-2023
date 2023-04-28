package com.mygdx.game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.TimeUtils;
import jdk.jfr.consumer.RecordedThread;

public class MyGdxGame implements ApplicationListener {
	Texture dropImage, bucketImage, LightImage;

	Path path = Paths.get("C:/Users/User/Desktop/Project-Java-2023-main/Game/assets/walls.txt");
	Scanner scanner;

	{
		try {
			scanner = new Scanner(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//Sound dropSound;
	//Music rainMusic;
	SpriteBatch batch;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	Array<Entity> entityArray = new Array<>();
	long lastDropTime;
	Rectangle rct;
	boolean is_menu;


	class Entity extends Rectangle{
		Texture image;
		float x,y,with,height;
		int speed;

		public Entity(String image, float x, float y, int speed) {
			this.image = new Texture(Gdx.files.internal(image));
			this.x = x - this.image.getWidth()/2;
			this.y = y - this.image.getHeight()/2;
			this.with = this.image.getWidth();
			this.height = this.image.getHeight();
			this.speed = speed;
			entityArray.add(this);
		}

		void draw(Batch batch){
			batch.draw(this.image, this.x, this.y);
		}
	}

	class Player extends Entity{
		public Player(String image, float x, float y, int speed) {
			super(image, x, y, speed);
		}
	}

	Player player;
	Entity ent1;

	@Override
	public void create() {
		// загрузка изображений для капли и ведра, 64x64 пикселей каждый


		// загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
		//dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// сразу же воспроизводиться музыка для фона
		//rainMusic.setLooping(true);
		//rainMusic.play();

		// создается камера и SpriteBatch
		is_menu = true;
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		batch = new SpriteBatch();

		player = new Player("seregapirat.jpg", Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2, 250);

		int y = 0;
		while(scanner.hasNext()){
			char[] walls = scanner.nextLine().toCharArray();
			for (int x =  0; x < walls.length; ++x){
				if(walls[x]=='0') new Entity("wall.jpg", x * 100, y * 100, 0);
			}
			y++;
		}

		LightImage = new Texture(Gdx.files.internal("ground/foreground1.png"));
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = player.image;

		// создается Rectangle для представления ведра
		bucket = new Rectangle();
		// центрируем ведро по горизонтали
		bucket.x = player.x;
		// размещаем на 20 пикселей выше нижней границы экрана.
		bucket.y = player.y;

		bucket.width = 100;
		bucket.height = 100;

		// создает массив капель и возрождает первую
		raindrops = new Array<>();
		spawnRaindrop();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64)- player.x;
		raindrop.y = 480- player.y;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
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
			// очищаем экран темно-синим цветом.
			// Аргументы для glClearColor красный, зеленый
			// синий и альфа компонент в диапазоне [0,1]
			// цвета используемого для очистки экрана.

			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

			// сообщает камере, что нужно обновить матрицы
			camera.update();

			// сообщаем SpriteBatch о системе координат
			// визуализации указанной для камеры.
			batch.setProjectionMatrix(camera.combined);

			// начинаем новую серию, рисуем ведро и
			// все капли
			batch.begin();

			batch.draw(LightImage, player.x + player.with / 2 - LightImage.getWidth() / 2, player.y + player.height / 2 - LightImage.getHeight() / 2);

			for (Rectangle raindrop : raindrops) {
				batch.draw(dropImage, raindrop.x, raindrop.y);
			}

			for (Entity entity : entityArray) {
				if((entity.x - player.x) * (entity.x - player.x) + (entity.y - player.y) * (entity.y - player.y) < 250 * 250) entity.draw(batch);
			}

			batch.end();

			// обработка пользовательского ввода


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

			// убедитесь что ведро остается в пределах экрана
			if (bucket.x < 0) bucket.x = 0;
			if (bucket.x > 800 - 64) bucket.x = 800 - 64;

			// проверка, нужно ли создавать новую каплю
			//if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

			// движение капли, удаляем все капли выходящие за границы экрана
			// или те, что попали в ведро. Воспроизведение звукового эффекта
			// при попадании.
			Iterator<Rectangle> iter = raindrops.iterator();
			while (iter.hasNext()) {
				Rectangle raindrop = iter.next();
				raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
				if (raindrop.y + 64 < 0) iter.remove();
				if (raindrop.overlaps(bucket)) {
					//dropSound.play();
					iter.remove();
				}
			}
		}
	}

	@Override
	public void dispose() {
		// высвобождение всех нативных ресурсов
		dropImage.dispose();
		bucketImage.dispose();
		//dropSound.dispose();
		//rainMusic.dispose();
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
