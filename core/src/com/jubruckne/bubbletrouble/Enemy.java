package com.jubruckne.bubbletrouble;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity {
    public Enemy(Map map, float x, float y) {
        super(map, x, y, 10, 10, Color.RED);
        texture = map.game.Sprites.Tower();
        body.setType(BodyDef.BodyType.DynamicBody);
        debug = true;
    }

    public Enemy(Map map, Point position) {
        this(map, position.x, position.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Point next = map.pathfinder.getNext(this.getPosition(), map.targets.first().getPosition());
        // Gdx.app.log("Draw", "current position:" + this.getPosition().toString());
        // Gdx.app.log("Draw", String.format("next    position: (%.0f, %.0f)", next.x * 10, next.y * 10));

        body.setLinearVelocity(
                map.pathfinder.getDirection(
                        getPosition().add(-5f, -5f),
                        map.targets.first().getPosition()
                ).scl(10)
        );

        if(hitpoints < max_hitpoints) {
            batch.end();
            batch.begin();

            map.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            map.shapeRenderer.arc(getPosition().x, getPosition().y, 5.1f, 0.0f, this.hitpoints / this.max_hitpoints * 360, 20);
            map.shapeRenderer.end();

            batch.end();
            batch.begin();
        }

        super.draw(batch);

        batch.end();
        batch.begin();
        map.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        map.shapeRenderer.setColor(Color.ORANGE);

        Array<Point> path = map.pathfinder.getPath(this.getPosition(), map.targets.first().getPosition());

        for (int p = 0; p < path.size; p++) {
            map.shapeRenderer.circle(
                    path.get(p).x * 10 + 5,
                    path.get(p).y * 10 + 5,
                    1.5f);
        }

        map.shapeRenderer.end();
        batch.end();
        batch.begin();
    }

    public void kill() {
        this.world.destroyBody(this.body);
        this.body = null;
    }
}
