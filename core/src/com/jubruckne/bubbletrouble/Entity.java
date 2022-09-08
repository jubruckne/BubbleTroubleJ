package com.jubruckne.bubbletrouble;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Entity {
    protected float width;
    protected float height;
    protected float hitpoints = 100;
    protected float max_hitpoints = 100;

    protected final Map map;
    protected final World world;
    protected Body body;
    protected TextureRegion texture;
    protected Color color;
    protected boolean debug = false;

    protected boolean selected = false;
    protected boolean highlight = false;

    public Entity(final Map map, float x, float y, float width, float height) {
        this(map, x, y, width, height, Color.WHITE);
    }

    public Entity(final Map map, float x, float y, float width, float height, Color color) {
        this.map = map;
        this.world = map.world;
        this.width = width;
        this.height = height;
        this.color = color;

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setRadius(height / 2.5f);
        // PolygonShape shape = new PolygonShape();
        // shape.setAsBox(width / 2 - 1, height / 2 - 1);

        FixtureDef fix = new FixtureDef();
        fix.shape = circle;
        fix.density = 1f;
        body.createFixture(circle, 1f);

        //shape.dispose();
        circle.dispose();
    }

    public float apply_damage(float dmg) {
        hitpoints -= dmg * 0.1f;
        return hitpoints;
    }

    public float distance_to(Point point) {
        return this.getPosition().dst(point);
    }

    public float distance_to(Entity entity) {
        return this.getPosition().dst(entity.getPosition());
    }

    public Point getPosition() {
        return new Point(
                this.body.getPosition().x, body.getPosition().y
        );
    }

    public float getX() {
        return getPosition().x;
    }

    public float getY() {
        return getPosition().y;
    }

    public Rectangle get_bounds() {
        return new Rectangle(
                (int)body.getPosition().x - width / 2,
                (int)body.getPosition().y - height / 2,
                (int) width,
                (int) height
        );
    }

    public void draw(SpriteBatch batch) {
        // Gdx.app.log("Enemy", this.toString());
        if (highlight)
            batch.setColor(Color.YELLOW);
        else
            batch.setColor(this.color);

        batch.draw(texture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);

        batch.setColor(Color.WHITE);
        map.font.getData().setScale(0.25f);
        map.font.draw(batch, toString(), getX(), getY() + height + 5);
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

    @Override
    public String toString() {
        return Utils.format("%s %f", getClass().getSimpleName(), getPosition());
    }
}
