package com.jubruckne.bubbletrouble;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Entity {
    protected float width;
    protected float height;

    private final Map map;
    private final World world;
    protected final Body body;
    protected TextureRegion texture;

    protected boolean selected = false;
    protected boolean highlight = false;

    public Entity(final Map map, float center_x, float center_y, float width, float height) {
        this.map = map;
        this.world = map.world;
        this.width = width;
        this.height = height;

        BodyDef def = new BodyDef();
        def.position.set(center_x, center_y);
        def.type = BodyDef.BodyType.KinematicBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.density = 1f;
        body.createFixture(shape, 1f);

        shape.dispose();
    }

    public Point getPosition() {
        return new Point(this.body.getPosition());
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Rectangle get_bounds() {
        return new Rectangle((int)body.getPosition().x, (int)body.getPosition().y, (int) width, (int) height);
    }

    public void draw(SpriteBatch batch) {
        if(highlight) { batch.setColor(Color.YELLOW); };
        batch.draw(texture, body.getPosition().x, body.getPosition().y, width, height);
        batch.setColor(Color.WHITE);
    }

    public void highlight(final boolean highlight) {
        this.highlight = highlight;
    }

    public void highlight() {
        this.highlight = true;
    }

    public boolean isHighlighted() {
        return highlight;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected = true;
    }

    public void select(final boolean selected) {
        this.selected = selected;
    }
}
