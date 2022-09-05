package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.math.Vector2;

public class Tower extends Entity {
    public Tower(Map map, Vector2 position) {
        super(map, position.x, position.y, 10, 10);
        texture = map.game.Sprites.Tower();
    }

    public Tower(Map map, float center_x, float center_y) {
        super(map, center_x, center_y, 10, 10);
        texture = map.game.Sprites.Tower();
    }
}
