package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Point extends Vector2 {
    public Point(int x, int y) {
        super(x, y);
    }

    public Point(float x, float y) {
        super(x, y);
    }

    public Point(double x, double y) {
        super((float) x, (float) y);
    }

    public Point(Vector2 point) {
        super(point.x, point.y);
    }

    public Point(Vector3 point) {
        super(point.x, point.y);
    }

    public Point(Entity point) {
        super(point.getPosition());
    }

    public Vector3 toVector3() {
        return new Vector3(this.x, this.y, 0);
    }

    public Rectangle toRectangle() {
        return new Rectangle(this.x, this.y, 0, 0);
    }

    public Rectangle toRectangle(int width, int height) {
        return new Rectangle(this.x, this.y, width, height);
    }

    public Rectangle toRectangle(float width, float height) {
        return new Rectangle(this.x, this.y, width, height);
    }

    public int x_div_10() {
        return (int) Math.floor(this.x / 10);
    }

    public int y_div_10() {
        return (int) Math.floor(this.y / 10);
    }

    @Override
    public String toString() {
        return String.format("(%.0f, %.0f)", this.x, this.y);
    }

    public String toString(String format) {
        return String.format("(%" + format +", %" + format + ")", this.x, this.y);
    }
}
