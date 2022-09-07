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
    private static final float STEP_TIME = 1f/60f;
    private float step_accumulator = 0;
    public ShapeRenderer shapeRenderer;

    public World world;
    public Game game;
    private int path_idx = 0;
    private int frame = 0;

    public final int width;
    public final int height;

    public Array<Tower> towers;
    public Array<Enemy> enemies;
    public Array<Target> targets;
    public Array<Source> sources;

    private Sprite background;

    public Pathfinder pathfinder;

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
        sources = new Array<>();

        sources.add(
                new Source(this, 0, 0)
        );

        targets.add(
                new Target(this, 290, 190)
        );

        towers.add(
                new Tower(this, 10, 10)
        );
        towers.add(
                new Tower(this, 20, 10)
        );
        towers.add(
                new Tower(this, 30, 10)
        );



    }

    public Enemy get_nearest_enemy(Point position) {
        Enemy nearest = null;
        float nearest_dist = 9999;

        for (Enemy e: enemies) {
            float dist = e.distance_to(position);
            if(dist < nearest_dist) {
                nearest = e;
                nearest_dist = dist;
            }
        }

        return nearest;
    }

    public void draw_background(SpriteBatch batch) {
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        step_accumulator += Math.min(delta, 0.25f);

        while (step_accumulator >= STEP_TIME) {
            step_accumulator -= STEP_TIME;
            world.step(STEP_TIME, 6, 2);
        }
    }

    public void draw(SpriteBatch batch, Vector2 mouse_pos, ShapeRenderer shapeRenderer) {
        frame++;

        this.shapeRenderer = shapeRenderer;

        stepWorld();

        batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.18f, 0.18f, 0.18f, 1f);
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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);

        Array<Point> path = this.pathfinder.getPath(sources.first().getPosition(), targets.first().getPosition());

        if (path_idx >= path.size + 10) path_idx = -10;
        if (frame % 5 == 0) path_idx++;

        for (int p = 0; p < path.size; p++) {
            shapeRenderer.circle(path.get(p).x * 10 + 5, path.get(p).y * 10 + 5, Math.max(0.75f, (8f - Math.abs(path_idx - p)) * 0.11f + 0.75f));
            // Gdx.app.log("path_idx", String.valueOf(path_idx) + "_" + String.valueOf(p));
        }
        shapeRenderer.end();
        batch.end();

        batch.begin();
        for (Target t: targets) {
            t.draw(batch);
        }
        for (Source s: sources) {
            s.draw(batch);
        }
        for (Enemy e: enemies) {
            e.draw(batch);
        }
        for (Tower t: towers) {
            t.draw(batch);
        }
        batch.end();

        // pathfinder.draw(batch);

    }

    public void dispose() {
        this.world.dispose();
    }

    public void start_wave() {
        enemies.add(new Enemy(this, sources.first().getPosition()));
    }

    public void kill_enemy(Enemy e) {
        this.enemies.removeValue(e, true);
        e.kill();
    }
}
