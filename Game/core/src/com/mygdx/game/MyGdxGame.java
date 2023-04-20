package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import jdk.jfr.consumer.RecordedThread;

public class MyGdxGame implements ApplicationListener {
	Texture dropImage;
	Texture bucketImage;
	//Sound dropSound;
	//Music rainMusic;
	SpriteBatch batch;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	Array<Entity> entityArray = new Array<>();
	long lastDropTime;
	Rectangle rct;


	class Entity extends Rectangle{
		Texture playerImage;
		float x,y,with,height;
		int speed;
		public Entity(String playerImage, float x, float y, float width, float height, int speed) {
			super(x, y, width, height);
			this.playerImage = new Texture(Gdx.files.internal(playerImage));
			this.speed = speed;
			entityArray.add(this);
		}
	}

	class Player extends Entity{
		public Player(String playerImage, float x, float y, float width, float height, int speed) {
			super(playerImage, x, y, width, height, speed);
		}
	}

	Player player = new Player("seregapirat.jpg", 0, 0, 100, 100, 250);

	@Override
	public void create() {
		// загрузка изображений для капли и ведра, 64x64 пикселей каждый
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = player.playerImage;

				// загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
				//dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
				//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

				// сразу же воспроизводиться музыка для фона
				//rainMusic.setLooping(true);
				//rainMusic.play();

		// создается камера и SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

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




		batch.draw(bucketImage, 0, 0);
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		for(Rectangle ent: entityArray){
			batch.draw(player.playerImage, player.x, player.y);
		}
		batch.end();

		// обработка пользовательского ввода

		if(Gdx.input.isKeyPressed(Keys.A)){
			player.x -= player.speed * Gdx.graphics.getDeltaTime();

		}

		if(Gdx.input.isKeyPressed(Keys.D)) {
			player.x += player.speed * Gdx.graphics.getDeltaTime();

		}

		if(Gdx.input.isKeyPressed(Keys.S)) {
			player.y -= player.speed * Gdx.graphics.getDeltaTime();

		}

		if(Gdx.input.isKeyPressed(Keys.W)) {
			player.y += player.speed * Gdx.graphics.getDeltaTime();

		}

		// убедитесь что ведро остается в пределах экрана
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// проверка, нужно ли создавать новую каплю
		//if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		// движение капли, удаляем все капли выходящие за границы экрана
		// или те, что попали в ведро. Воспроизведение звукового эффекта
		// при попадании.
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)) {
				//dropSound.play();
				iter.remove();
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
