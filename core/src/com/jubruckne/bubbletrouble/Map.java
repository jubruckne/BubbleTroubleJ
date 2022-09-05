package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {
    public World world;
    public Game game;
    private int path_idx = 0;
    private int frame = 0;

    public final int width;
    public final int height;

    public Array<Tower> towers;
    public Array<Enemy> enemies;
    public Array<Target> targets;

    private Sprite background;

    private Pathfinder pathfinder;

    public Map(final Game game) {
        this.game = game;
        this.width = 300;
        this.height = 200;

        pathfinder = new Pathfinder(this);

        world = new World(new Vector2(0, 0), false);

        background = new Sprite(new Texture("farback.png"));
        background.setPosition(0, 0);
        background.setAlpha(0.75f);

        towers = new Array<>();
        enemies = new Array<>();
        targets = new Array<>();

        enemies.add(
                new Enemy(this, 0, 100)
        );

        targets.add(
                new Target(this, 290, 120)
        );
    }

    public void draw_background(SpriteBatch batch) {
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    public void draw(SpriteBatch batch, Vector2 mouse_pos, ShapeRenderer shapeRenderer) {
        frame++;

        batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        for (int x = 0; x < 100; x++) {
            shapeRenderer.line(x * 10, 0, x * 10, height);
            shapeRenderer.line(0, x * 10, width, x * 10);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        shapeRenderer.rect(
                (float) (Math.floor(mouse_pos.x / 10) * 10),
                (float) (Math.floor(mouse_pos.y / 10) * 10),
                10,
                10
        );
        shapeRenderer.end();
        batch.end();

        batch.begin();
        for (Entity t : towers) {
            t.draw(batch);
        }
        for (Entity e : enemies) {
            e.draw(batch);
        }
        for (Entity t : targets) {
            t.draw(batch);
        }
        batch.end();

        batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);

        pathfinder.recalc();

        Array<Point> path = this.pathfinder.getPath(enemies.first().getPosition(), targets.first().getPosition());
        // pathfinder.draw(batch);

        if (path_idx >= path.size + 10) path_idx = -10;
        if (frame % 6 == 0) path_idx++;

        for (int p = 0; p < path.size; p++) {
            shapeRenderer.circle(path.get(p).x * 10 + 5, path.get(p).y * 10 + 5, Math.max(0.75f, (8f - Math.abs(path_idx - p)) * 0.10f + 0.75f));
            // Gdx.app.log("path_idx", String.valueOf(path_idx) + "_" + String.valueOf(p));
        }
        shapeRenderer.end();
        batch.end();

    }

    public void dispose() {
        this.world.dispose();
    }
}
