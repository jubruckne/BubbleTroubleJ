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

    public final int width;
    public final int height;

    public Array<Tower> towers;
    public Array<Enemy> enemies;
    public Array<Target> targets;

    private Sprite background;

    private AStar a_star_pathfinder;

    public Map(final Game game) {
        this.game = game;
        this.width = 300;
        this.height = 200;

        a_star_pathfinder = new AStar(width / 10, height / 10);

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
                new Target(this, 180, 120)
        );
    }

    public AStar pathfinder() {
        for(Tower t: towers) {
            Point pos = t.getPosition();
            Gdx.app.log("Tower", pos.toString());
            AStar.Node node = a_star_pathfinder.map.getNodeAt(
                    pos.x_div_10(),
                    pos.y_div_10()
            );
            node.isWall = true;
        }

        a_star_pathfinder.run();

        return this.a_star_pathfinder;
    }

    public void draw_background(SpriteBatch batch) {
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    public void draw(SpriteBatch batch, Vector2 mouse_pos, ShapeRenderer shapeRenderer) {
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

        Array<Point> path = this.pathfinder().getPath(enemies.first().getPosition(), targets.first().getPosition());
        for (Point p: path) {
            shapeRenderer.circle(p.x * 10 + 5, p.y * 10 + 5, 1f);
        }
        shapeRenderer.end();
        batch.end();

    }

    public void dispose() {
        this.world.dispose();
    }
}
