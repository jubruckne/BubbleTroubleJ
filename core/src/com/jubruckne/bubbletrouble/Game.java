package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.*;

public class Game extends com.badlogic.gdx.Game implements ApplicationListener, InputProcessor {
    public SpriteSheet Sprites;
    private boolean paused = false;

    @Override
    public void create() {
        Sprites = new SpriteSheet(this);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(this);

        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render();
    }

    public boolean isPaused() {
        return paused;
    }

    public void dispose() {
        Sprites.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        } else if(keycode == Input.Keys.P) {
            pause();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return ((GameScreen)screen).touchDown(screenX, screenY, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public void pause() {
        this.paused = true;
        super.pause();
    }

    @Override
    public void resume() {
        this.paused = false;
        super.resume();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return ((GameScreen)screen).mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
