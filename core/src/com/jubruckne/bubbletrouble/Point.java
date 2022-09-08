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

    @Override
    public Point add(float x, float y) {
        super.add(x, y);
        return this;
    }

    @Override
    public String toString() {
        return Utils.format("(%.0f, %.0f)", this.x, this.y);
    }

    public String toString(String format) {
        return Utils.format("(%" + format +", %" + format + ")", this.x, this.y);
    }

    public int x_int() {
        return (int)x;
    }

    public int y_int() {
        return (int)y;
    }
}
