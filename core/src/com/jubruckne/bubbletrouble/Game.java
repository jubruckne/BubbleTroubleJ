package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.*;
import com.badlogic.gdx.physics.box2d.World;

public class Game extends com.badlogic.gdx.Game implements ApplicationListener, InputProcessor {
    public SpriteSheet Sprites;
    public World world;

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

    public void dispose() {
        Sprites.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
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
