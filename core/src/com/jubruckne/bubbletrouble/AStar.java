package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

public class AStar {

    public class AStarMap {
        private Node[][] map;

        private final int width;
        private final int height;

        public AStarMap(int width, int height) {
            this.width = width;
            this.height = height;

            map = new Node[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    map[y][x] = new Node(this, x, y);
                }
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Node getNodeAt(int x, int y) {
            return map[y][x];
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    stringBuilder.append(map[y][x].isWall ? "#" : " ");
                }
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }

    public class Node {
        public final int x;
        public final int y;
        public boolean isWall;
        private final int index;
        private final Array<Connection<Node>> connections;

        public Node(AStarMap map, int x, int y) {
            this.x = x;
            this.y = y;
            this.index = x * map.getHeight() + y;
            this.isWall = false;
            this.connections = new Array<Connection<Node>>();
        }

        public int getIndex() {
            return index;
        }

        public Array<Connection<Node>> getConnections() {
            return connections;
        }

        @Override
        public String toString() {
            return "Node: (" + x + ", " + y + ")";
        }

    }

    public final AStarMap map;
    private PathFinder<Node> pathfinder;
    private final Heuristic<Node> heuristic;
    private GraphPath<Connection<Node>> connectionPath;

    public AStar(int width, int height) {
        this.map = new AStarMap(width, height);
        this.pathfinder = new IndexedAStarPathFinder<>(createGraph(map));
        this.connectionPath = new DefaultGraphPath<>();
        this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate(Node node, Node endNode) {
                // Manhattan distance
                return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
            }
        };
    }

    public void run() {
        this.pathfinder = new IndexedAStarPathFinder<>(createGraph(map));
        this.connectionPath = new DefaultGraphPath<>();
    }

    public Array<Point> getPath(Point source, Point target) {
        Array<Point> path = new Array<>();

        int targetX = target.x_div_10();
        int targetY = target.y_div_10();

        Node next_node = findNextNode(source.x_div_10(), source.y_div_10(), targetX, targetY);

        while(next_node != null) {
            while(next_node.isWall) {
                next_node = findNextNode(next_node.x, next_node.y, targetX, targetY);
            }

            path.add(new Point(next_node.x, next_node.y));
            next_node = findNextNode(next_node.x, next_node.y, targetX, targetY);
        }

        return path;
    }


    public Node findNextNode(int sourceX, int sourceY, int targetX, int targetY) {
        if (map == null
                || sourceX < 0 || sourceX >= map.getWidth()
                || sourceY < 0 || sourceY >= map.getHeight()
                || targetX < 0 || targetX >= map.getWidth()
                || targetY < 0 || targetY >= map.getHeight()) {
            return null;
        }

        Node sourceNode = map.getNodeAt(sourceX, sourceY);
        Node targetNode = map.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

        return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
    }

    public Node findNextNode(Point source, Point target) {
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        return findNextNode(sourceX, sourceY, targetX, targetY);
    }

    private static final int[][] NEIGHBORHOOD = new int[][]{
            new int[]{-1, 0},
            new int[]{0, -1},
            new int[]{0, 1},
            new int[]{1, 0}
    };

    private static MyGraph createGraph(AStarMap map) {
        final int height = map.getHeight();
        final int width = map.getWidth();

        MyGraph graph = new MyGraph(map);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node node = map.getNodeAt(x, y);
                if (node.isWall) {
                    continue;
                }
                // Add a connection for each valid neighbor
                for (int offset = 0; offset < NEIGHBORHOOD.length; offset++) {
                    int neighborX = node.x + NEIGHBORHOOD[offset][0];
                    int neighborY = node.y + NEIGHBORHOOD[offset][1];
                    if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
                        Node neighbor = map.getNodeAt(neighborX, neighborY);
                        if (!neighbor.isWall) {
                            // Add connection to walkable neighbor
                            node.getConnections().add(new DefaultConnection<Node>(node, neighbor));
                        }
                    }
                }
                node.getConnections().shuffle();
            }
        }
        return graph;
    }

    private static class MyGraph implements IndexedGraph<Node> {
        AStarMap map;

        public MyGraph(AStarMap map) {
            this.map = map;
        }

        @Override
        public int getIndex(Node node) {
            return node.getIndex();
        }

        @Override
        public Array<Connection<Node>> getConnections(Node fromNode) {
            return fromNode.getConnections();
        }

        @Override
        public int getNodeCount() {
            return map.getHeight() * map.getHeight();
        }
    }
}
