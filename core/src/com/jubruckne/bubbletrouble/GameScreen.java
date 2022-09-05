package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final Game game;

    private SpriteBatch batch;
    private SpriteBatch batch_ui;

    private BitmapFont font;

    private Map map;

    private final OrthographicCamera camera;
    private final OrthographicCamera camera_ui;

    private final ShapeRenderer shapeRenderer;
    private final ShapeRenderer shapeRenderer_ui;

    private Button GoButton;

    public GameScreen(final Game game) {
        this.game = game;

        font = new BitmapFont();

        map = new Map(game);

        // World Space
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, map.width, map.height);
        batch.setProjectionMatrix(camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Window Space
        batch_ui = new SpriteBatch();
        camera_ui = new OrthographicCamera();
        camera_ui.setToOrtho(false);
        batch_ui.setProjectionMatrix(camera_ui.combined);

        shapeRenderer_ui = new ShapeRenderer();
        shapeRenderer_ui.setAutoShapeType(true);
        shapeRenderer_ui.setProjectionMatrix(camera_ui.combined);

        GoButton = new Button(
                map,
                "Play",
                Gdx.graphics.getWidth() - 96 - 16,
                Gdx.graphics.getHeight() - 48 - 16,
                96,
                48);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        map.draw_background(batch_ui);
        map.draw(batch, get_mouse_pos_world(), shapeRenderer);

        // first draw the background (everything in screen coordinates)
        batch_ui.begin();
        font.draw(batch_ui, String.format("Screen: (%s)", get_mouse_pos_local()), 10, 589);
        font.draw(batch_ui, String.format("World:  (%s)", get_mouse_pos_world()), 10, 570);
        font.draw(batch_ui, String.format("Game:   (%s)", get_mouse_pos_game()), 10, 551);
        batch_ui.end();

        GoButton.draw(batch_ui);
    }

    public Point get_mouse_pos_game() {
        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Point(
                Math.floor(mouse.x / 10) * 10,
                Math.floor(mouse.y / 10) * 10
        );
    }

    public Point get_mouse_pos_world() {
        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Point(mouse.x, mouse.y);
    }

    public Point get_mouse_pos_local() {
        Vector3 mouse = camera_ui.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Point(mouse.x, mouse.y);
    }

    public boolean touchDown(int screenX, int screenY, int button) {
        if (button == Input.Buttons.LEFT) {
            map.towers.add(new Tower(map, get_mouse_pos_game()));
        }

        return true;
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        camera_ui.update();
        batch_ui.setProjectionMatrix(camera_ui.combined);
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
        batch.dispose();
        font.dispose();
        map.dispose();
    }

    public boolean mouseMoved(int screenX, int screenY) {
        GoButton.highlight(GoButton.get_bounds().contains(get_mouse_pos_local()));

        for (Entity e : this.map.towers) {
            e.highlight(
                    e.get_bounds().contains(get_mouse_pos_world())
            );
        }
        for (Entity e : this.map.enemies) {
            e.highlight(
                    e.get_bounds().contains(get_mouse_pos_world())
            );
        }

        return true;
    }
}
