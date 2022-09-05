package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Pathfinder {
    private final Map map;
    private int[] cells;
    private final int width;
    private final int height;

    public Pathfinder(Map map) {
        this.map = map;
        width = map.width / 10;
        height = map.height / 10;
    }

    public void draw(SpriteBatch batch) {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.22f);
        batch.begin();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int cost = cells[x * height + y];
                font.draw(batch, String.format("%s", cost), x * 10, y * 10 + 5);
            }
        }
        batch.end();
    }

    public void recalc() {
        this.cells = new int[width * height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                // Gdx.app.log("", String.format("x = %d, y = %d", x, y));
                cells[x * height + y] = -1;
            }
        }

        for (Entity t : map.towers) {
            Point pos = t.getPosition();
            cells[pos.x_div_10() * height + pos.y_div_10()] = 9999;
        }
    }

    public Array<Point> getPath(Point source, Point target) {
        int sourceX = source.x_div_10();
        int sourceY = source.y_div_10();

        int targetX = target.x_div_10();
        int targetY = target.y_div_10();

        assign_cost(targetX, targetY, 0);

        Array<Point> path = new Array<>();

        Point nearest = get_next_path(sourceX, sourceY);
        while(nearest != null) {
            if(nearest.x == targetX && nearest.y == targetY)
                break;
            path.add(nearest);

            nearest = get_next_path((int) nearest.x, (int) nearest.y);
        }

        return path;
    }

    private Point get_next_path(int x, int y) {
        int cost = 9999;
        Point p = null;

        // down
        if (y > 0) {
            if(cells[x * height + y - 1] < cost) {
                cost = cells[x * height + y];
                p = new Point(x, y - 1);
            }
        }
        // right
        if (x < width - 1) {
            if (cells[(x + 1) * height + y] < cost) {
                cost = cells[(x + 1) * height + y];
                p = new Point(x + 1, y);
            }
        }
        // up
        if (y < height - 1) {
            if(cells[x * height + y + 1] < cost) {
                cost = cells[x * height + y + 1];
                p = new Point(x, y + 1);
            }
        }
        // left
        if (x > 0) {
            if(cells[(x - 1) * height + y] < cost) {
                cost = cells[(x - 1) * height + y];
                p = new Point(x - 1, y);
            }
        }

        if(cost == 9999)
            return null;
        else
            return p;
    }

    private void assign_cost(int x, int y, int cost) {
        if  (cells[x * height + y] == 9999) return;

            if (cells[x * height + y] == - 1) {
            cells[x * height + y] = cost;

            if(cost != 9999) {
                // down
                if (y > 0) {
                    assign_cost(x, y - 1, cost + 1);
                }
                // left
                if (x > 0) {
                    assign_cost(x - 1, y, cost + 1);
                }
                // up
                if (y < height - 1) {
                    assign_cost(x, y + 1, cost + 1);
                }
                // right
                if (x < width - 1) {
                    assign_cost(x + 1, y, cost + 1);
                }
            }
        } else {
            if(cost < cells[x * height + y]) {
                cells[x * height + y] = cost;
            } else {
                return;
            }

            if(cost != 9999) {
                // down
                if (y > 0) {
                    assign_cost(x, y - 1, cost + 1);
                }
                // left
                if (x > 0) {
                    assign_cost(x - 1, y, cost + 1);
                }
                // up
                if (y < height - 1) {
                    assign_cost(x, y + 1, cost + 1);
                }
                // right
                if (x < width - 1) {
                    assign_cost(x + 1, y, cost + 1);
                }
            }
        }
    }
}
