package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Drop game;

    OrthographicCamera camera;
    Texture dropImage;
    Texture balaImage;
    Texture bucketImage;
    Texture liveImage;
    Sound hitSound;
    Sound shootSound;
    Sound bulletSound;
    Music rainMusic;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    Array<Rectangle> balas;
    long lastDropTime;
    long lastBulletTime;
    int vidas;
    int dropsDestroyed;
    int bucketVerticalSpeed;

    public GameScreen(final Drop game) {
        this.game = game;

        //cargamos los recursos de la gota y el cubo, cada uno de 64 pixels de lado
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        //cargamos  la bala, de 16 pixels de lado
        balaImage = new Texture(Gdx.files.internal("bala.png"));
        //Cargamos la imagen para representar las vidas
        liveImage = new Texture(Gdx.files.internal("live.png"));

        //cargamos los efectos sonoros y la música
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("bullet.mp3"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        //comienza la reproducción de la música al momento
        rainMusic.setLooping(true);

        //creamos la camara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //creamos el cubo y lo posicionamos. Su velocidad vertical inicial será nula
        bucket = new Rectangle();
        bucket.x = 368; //800/2 - 64/2
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 48;
        bucketVerticalSpeed = 0;

        //creamos un array para las gotas las gotas
        raindrops = new Array< >();
        spawnRaindrop();

        //creamos otro array para las balas
        balas = new Array<>();

        //Inicializamos el número de vidas;
        vidas = 3;

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.y = MathUtils.random(0, 416/*480-64*/);
        raindrop.x = 800;
        raindrop.height = 24;
        raindrop.width = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }


    private void spawnBullet() {
        bulletSound.play();
        Rectangle bala = new Rectangle();
        bala.x = bucket.x + 64;
        bala.y = bucket.y + 24;
        bala.height = 16;
        bala.width = 16;
        balas.add(bala);
        lastBulletTime = TimeUtils.nanoTime();
    }


    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        dibujarVidas();
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Puntos: "+dropsDestroyed, 450, 450);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        for (Rectangle bala : balas){
            game.batch.draw(balaImage, bala.x, bala.y);
        }
        game.batch.end();

        //añadimos funcionalidad al teclado para mover el cubo hacia los lados
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 300 * Gdx.graphics.getDeltaTime();

        //Añadimos la posibilidad de impulsar el cubo hacia arriba
        //if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) bucket.y -= 300 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) bucketVerticalSpeed = 400;//bucket.y += 300 * Gdx.graphics.getDeltaTime();

        //Añadimos la posibilidad de disparar con el cubo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && TimeUtils.nanoTime() - lastBulletTime > 100000000) spawnBullet();

        //evitamos que el cubo se salga de la pantalla por los lados
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 736 /*800 - 64*/) bucket.x = 736;
        if (bucket.y > 416 /*480-64*/) bucket.y = 416;
        if (bucket.y < 0) bucket.y = 0;

        //generamos una gota si ha pasado el tiempo suficiente desde la última que salió
        if (TimeUtils.nanoTime()-lastDropTime > 1000000000) spawnRaindrop();

        //aceleramos el cubo hacia abajo como si estuviera en caída libre y lo movemos en consecuencia
        if (bucket.y > 0) bucketVerticalSpeed -= 900 * Gdx.graphics.getDeltaTime();


        bucket.y += bucketVerticalSpeed * Gdx.graphics.getDeltaTime();

        //movemos las gotas hacia la izquierda para que luchen contra el cubo
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
            Rectangle raindrop = iter.next();
            raindrop.x -= 100*Gdx.graphics.getDeltaTime();
            //Si una gota sale de la pantalla la eliminamos
            if (raindrop.x < 0) iter.remove();
            // Si una gota toca al jugador eliminamos una vida y reproducimos un sonido
            if (raindrop.overlaps(bucket)) { //TODO En lugar de recoger la gota y sumarla habría que restar una vida al jugador
                vidas--;
                hitSound.play();
                iter.remove();
            }

            //Si no quedan vidas termina el juego
            if (vidas == 0){
                game.setScreen(new GameOverScreen(game, dropsDestroyed));
                dispose();
            }

            //Comprobamos si las gotas chocan con alguna bala
            for (Rectangle bala : balas) {
                if (raindrop.overlaps(bala)) {
                    shootSound.play();
                    dropsDestroyed++;
                    iter.remove();
                    balas.removeValue(bala, false);

                }
            }
        }
        //movemos las balas
        for (Iterator<Rectangle> iterador = balas.iterator(); iterador.hasNext();) {
            Rectangle bala = iterador.next();
            bala.x += 300 * Gdx.graphics.getDeltaTime();
            //Si la bala se sale del cuadro la eliminamos
            if (bala.x > 784 /*800 - 16*/) iterador.remove();

        }

    }

    private void dibujarVidas() {
        for (int i = 0; i<vidas; i++){
            game.batch.draw(liveImage, (32 * i) + 5 , 420);
        }
    }

    @Override
    public void show() {
        //empiza la reproducción de la "música" en cuanto se muestra la pantalla
        rainMusic.play();

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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        liveImage.dispose();
        shootSound.dispose();
        hitSound.dispose();
        bulletSound.dispose();
        rainMusic.dispose();

    }

}
