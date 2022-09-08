package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Pathfinder {
    private final Map map;
    private int[] cells;
    private final int width;
    private final int height;
    private final int scale;

    public Pathfinder(Map map) {
        this(map, map.width, map.height);
    }

    public Pathfinder(Map map, int width, int height) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.scale = map.width / width;
    }

    public void draw(SpriteBatch batch) {
        if (cells == null) return;

        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.22f);
        batch.begin();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int cost = cells[x * height + y];
                font.draw(batch, Utils.format("%i", cost), x * scale, y * scale + 5);
            }
        }
        batch.end();
    }

    private Point toSim(Point p) {
        return new Point(Math.floor((p.x) / scale), Math.floor((p.y) / scale));
    }

    private void reset() {
        this.cells = new int[width * height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x * height + y] = -1;
            }
        }

        for (Entity t : map.towers) {
            Point pos = toSim(t.getPosition());
            cells[pos.x_int() * height + pos.y_int()] = 9999;
            //Gdx.app.log("Pathfinder", "Tower at: " + pos.toString());
        }
    }

    public Vector2 getDirection(Point world_source, Point world_target) {
        Point next = getNext(toSim(world_source), toSim(world_target));

        Gdx.app.log(
                "Pathfinder",
                Utils.format("Find route from %f to %f:",
                        world_source,
                        world_target
                        ));

        if (next == null)
            return new Vector2(0, 0);
        else {
            Vector2 v = new Vector2(next.x - toSim(world_source).x_int(), next.y - toSim(world_source).y_int());

            Gdx.app.log(
                    "Pathfinder",
                    Utils.format("Go into direction %f.",
                            v));

             if(v.y == 0) {
                 Gdx.app.log("Pathfinder:", world_source.toString("f"));
                 //v.y = (float) (5 - (Math.floor(world_source.y / scale) * scale));
                 v.y =
                         (float) (( Math.floor(world_source.y / scale) * scale - world_source.y) * 0.1f);
             }

            return v;
        }
    }

    private Point getNext(Point sim_source, Point sim_target) {
        reset();

        int sourceX = sim_source.x_int();
        if (sourceX < 0) sourceX = 0;

        int sourceY = sim_source.y_int();
        if (sourceY < 0) sourceY = 0;

        int targetX = sim_target.x_int();
        int targetY = sim_target.y_int();

        if(cells[sourceX * height + sourceY] == 9999) {
            Gdx.app.log("Pathfinder", "Starting from block!!!");
        }

        assign_cost(targetX, targetY, 0);

        Point nearest = get_next_path(sourceX, sourceY);
        //Gdx.app.log(
        //        "Pathfinder",
        //        Utils.format("Find route from (%i, %i) to (%i, %i)", sourceX, sourceY, targetX, targetY));

        return nearest;
    }

    public Array<Point> getPath(Point world_source, Point world_target) {
        reset();

        int sourceX = toSim(world_source).x_int();
        if (sourceX < 0) sourceX = 0;
        int sourceY = toSim(world_source).y_int();
        if (sourceY < 0) sourceY = 0;

        int targetX = toSim(world_target).x_int();
        int targetY = toSim(world_target).y_int();

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
