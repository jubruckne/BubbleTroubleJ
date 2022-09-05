package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy extends Entity {
    public Enemy(Map map, float x, float y) {
        super(map, x, y, 10, 10);
        texture = map.game.Sprites.Tower();
    }

    public Enemy(Map map, Point position) {
        super(map, position.x, position.y, 10, 10);
        texture = map.game.Sprites.Tower();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(highlight)
            batch.setColor(Color.YELLOW);
        else
            batch.setColor(Color.RED);

        batch.draw(texture, body.getPosition().x, body.getPosition().y, width, height);
        batch.setColor(Color.WHITE);
    }
}
