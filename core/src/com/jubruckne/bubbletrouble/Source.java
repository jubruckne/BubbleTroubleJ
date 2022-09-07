package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Source extends Entity {
    private boolean growing = true;
    private float size = (float) (Math.random() * 3);

    public Source(Map map, float x, float y) {
        super(map, x, y, 10, 10, Color.RED);
        texture = map.game.Sprites.Tower();
        body.setActive(false);
    }

    public Source(Map map, Point position) {
       this(map, position.x, position.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(growing) size += 0.05f; else size -= 0.05f;
        if (size > width / 3) growing = !growing;
        if (size < 0) growing = !growing;

        if(highlight)
            batch.setColor(Color.YELLOW);
        else
            batch.setColor(color);

        batch.draw(
                texture,
                body.getPosition().x - width / 2 - size / 2,
                body.getPosition().y - height / 2 - size / 2,
                width + size,
                height + size
        );
        batch.setColor(Color.WHITE);
    }
}
