package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    final Drop game;
    OrthographicCamera camera;
    Sound gameOverSound;
    int puntuacion;
    public GameOverScreen(final Drop game, int puntuacion) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //Cargar el recurso de sonido
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));

        //Establecer la puntuación
        this.puntuacion = puntuacion;

    }

    @Override
    public void show() {
        gameOverSound.play();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "Has destruido "+puntuacion+" enemigos.", 50, 350);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Pulsa ENTER para jugar", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new GameScreen(game));
            dispose();
        }

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
        gameOverSound.dispose();

    }
}
