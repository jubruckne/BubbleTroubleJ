package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

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
        if (cells == null) return;

        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.22f);
        batch.begin();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int cost = cells[x * height + y];
                font.draw(batch, String.format("%s", cost), x * 10, y * 10 + 5);
            }
        }
        batch.end();
    }

    private void reset() {
        this.cells = new int[width * height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x * height + y] = -1;
            }
        }

        for (Entity t : map.towers) {
            Point pos = t.getCenter();
            cells[pos.x_div_10() * height + pos.y_div_10()] = 9999;
            // Gdx.app.log("Pathfinder", "Tower at: " + pos.x_div_10() + ", " + pos.y_div_10());
        }
    }

    public Vector2 getDirection(Point source, Point target) {
        Point next = getNext(source, target);
        if (next == null)
            return new Vector2(0, 0);
        else {
            return new Vector2(next.x - source.x_div_10(), next.y - source.y_div_10());
        }
    }

    public Point getNext(Point source, Point target) {
        reset();

        int sourceX = source.x_div_10();
        int sourceY = source.y_div_10();

        int targetX = target.x_div_10();
        int targetY = target.y_div_10();

        if(cells[sourceX * height + sourceY] == 9999) {
            Gdx.app.log("Pathfinder", "Starting from block!!!");
        }

        assign_cost(targetX, targetY, 0);

        Point nearest = get_next_path(sourceX, sourceY);
        Gdx.app.log(
                "Pathfinder",
                String.format("Find route from (%s, %s) to (%s, %s)", sourceX, sourceY, targetX, targetY));

        if (nearest.x == 12 && nearest.y == 9) {
            Gdx.app.log(
                    "Pathfinder",
                    String.format("Move next to (%s, %s), which has cost %s",
                            nearest.x,
                            nearest.y,
                            cells[(int) (nearest.x * height + nearest.y)]));
        }

        return nearest;
    }

    public Array<Point> getPath(Point source, Point target) {
        reset();

        int sourceX = source.x_div_10();
        int sourceY = source.y_div_10();

        int targetX = target.x_div_10();
        int targetY = target.y_div_10();

        assign_cost(targetX, targetY, 0);

        Array<Point> path = new Array<>();

        Point nearest = get_next_path(sourceX, sourceY);
        while (nearest != null) {
            if (nearest.x == targetX && nearest.y == targetY)
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
            if (cells[x * height + y - 1] < cost) {
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
            if (cells[x * height + y + 1] < cost) {
                cost = cells[x * height + y + 1];
                p = new Point(x, y + 1);
            }
        }
        // left
        if (x > 0) {
            if (cells[(x - 1) * height + y] < cost) {
                cost = cells[(x - 1) * height + y];
                p = new Point(x - 1, y);
            }
        }

        if (cost == 9999)
            return null;
        else
            return p;
    }

    private void assign_cost(int x, int y, int cost) {
        if (cells[x * height + y] == 9999) return;

        if (cells[x * height + y] == -1) {
            cells[x * height + y] = cost;

            if (cost != 9999) {
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
            if(cells[x * height + y] == 9999) {
                return;
            } else if (cost == 9999) {
                return;
            } else if (cost < cells[x * height + y]) {
                cells[x * height + y] = cost;
            } else {
                return;
            }

            if (cost != 9999) {
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

    public void print() {
/*        Gdx.app.log("Pathfinder", "");

        for (int y = 0; y < height; y++) {
            String line = "";
            for (int x = 0; x < width; x++){
                if(cells[x * height + y] == 9999) {
                    line = line + "#";
                } else {
                    line = line + "_";
                }
            }
            Gdx.app.log("Pathfinder", line);
        }
  */
    }
}
