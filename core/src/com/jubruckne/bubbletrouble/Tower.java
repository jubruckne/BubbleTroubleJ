package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tower extends Entity {
    public Tower(Map map, Vector2 position) {
        super(map, position.x, position.y, 10, 10);
        texture = map.game.Sprites.Tower();
    }

    public Tower(Map map, float x, float y) {
        super(map, x, y, 10, 10);
        texture = map.game.Sprites.Tower();
    }

    @Override
    public String toString() {
        return String.format("Tower pos=(%s, %s)", this.getX(), this.getY());
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
        Enemy e = map.get_nearest_enemy(getPosition());

        if(e != null) {
            if (e.distance_to(getPosition()) < 50) {
                if(e.apply_damage(0.025f) <= 0.0f) {
                    map.kill_enemy(e);
                } else {
                    // Gdx.app.log("Distance", "=" + e.distance_to(getPosition()));
                    batch.end();
                    batch.begin();

                    map.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    map.shapeRenderer.setColor(0.9f, 0.9f, 0.1f, 1f);

                    map.shapeRenderer.line(getCenter(), e.getCenter());

                    map.shapeRenderer.setColor(Color.WHITE);
                    map.shapeRenderer.end();

                    batch.end();
                    batch.begin();
                }
            }
        }
    }
}
