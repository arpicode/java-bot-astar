package com.example.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.example.models.Tile;

public class AStar {

    private static final int MOVE_COST = 10;

    private Node<Tile>[][] grid;
    private PriorityQueue<Node<Tile>> open;
    private boolean[][] closed;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public AStar(int width, int height) {
        this.grid = new Node[height][width];
        // WIP: initialize grid based on game context
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Node<Tile> node = new Node<>(x, y, new Tile(Tile.Type.GRASS));
                this.grid[y][x] = node;
            }
        }
    }

    public void addNode(Node<Tile> node) {
        this.grid[node.getY()][node.getX()] = node;
    }

    public void setBlocked(int x, int y) {
        this.grid[y][x] = null;
    }

    public void setStartNode(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    public void setEndNode(int x, int y) {
        this.endX = x;
        this.endY = y;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].setHeuristicCost(Math.abs(j - endX) + Math.abs(i - endY));
                }
            }
        }
    }

    private void checkAndUpdateCost(Node<Tile> current, Node<Tile> target, int cost) {
        if (target == null || closed[target.getY()][target.getX()])
            return;

        int targetFinalCost = target.getHeuristicCost() + cost;
        boolean isOpen = open.contains(target);

        if (!isOpen || targetFinalCost < target.getFinalCost()) {
            target.setFinalCost(targetFinalCost);
            target.setParent(current);
            if (!isOpen)
                open.add(target);
        }
    }

    private PriorityQueue<Node<Tile>> computeTargets() {
        PriorityQueue<Node<Tile>> targets = new PriorityQueue<>(Comparator.comparingInt(Node<Tile>::getHeuristicCost));

        for (int y = 0; y < grid.length; ++y) {
            for (int x = 0; x < grid[0].length; ++x) {
                if (grid[y][x] == null) {
                    addAdjacentNodesToTargets(targets, x, y);
                }
            }
        }

        return targets;
    }

    private void addAdjacentNodesToTargets(PriorityQueue<Node<Tile>> targets, int x, int y) {
        addNodeIfNotNull(targets, x, y - 1); // Up
        addNodeIfNotNull(targets, x - 1, y); // Left
        addNodeIfNotNull(targets, x + 1, y); // Right
        addNodeIfNotNull(targets, x, y + 1); // Down
    }

    private void addNodeIfNotNull(PriorityQueue<Node<Tile>> targets, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x] != null && !targets.contains(grid[y][x])) {
            targets.add(grid[y][x]);
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length;
    }

    public List<Node<Tile>> computeShortestPath() {
        PriorityQueue<Node<Tile>> targets = computeTargets();
        List<Node<Tile>> shortestPath = new ArrayList<>();
        int shortestPathLength = Integer.MAX_VALUE;

        for (Node<Tile> target : targets) {
            setEndNode(target.getX(), target.getY());
            List<Node<Tile>> path = computePath();

            if (path.size() < shortestPathLength) {
                shortestPath = path;
                shortestPathLength = path.size();
            }
        }

        return shortestPath;
    }

    public List<Node<Tile>> computePath() {
        open = new PriorityQueue<>(Comparator.comparingInt(node -> node.getFinalCost() + node.getHeuristicCost()));
        closed = new boolean[grid.length][grid[0].length];
        open.add(grid[startY][startX]);

        Node<Tile> current;
        while (!open.isEmpty()) {
            current = open.poll();
            closed[current.getY()][current.getX()] = true;

            if (current.equals(grid[endY][endX])) {
                // We've reached the end node, construct and return the path
                List<Node<Tile>> path = new ArrayList<>();

                while (current != null) {
                    path.add(current);
                    current = current.getParent();
                }
                Collections.reverse(path); // Reverse the path to start from the beginning

                return path;
            }

            Node<Tile> target;
            if (current.getY() - 1 >= 0) {
                target = grid[current.getY() - 1][current.getX()];
                checkAndUpdateCost(current, target, current.getFinalCost() + MOVE_COST);
            }

            if (current.getX() - 1 >= 0) {
                target = grid[current.getY()][current.getX() - 1];
                checkAndUpdateCost(current, target, current.getFinalCost() + MOVE_COST);
            }

            if (current.getX() + 1 < grid[0].length) {
                target = grid[current.getY()][current.getX() + 1];
                checkAndUpdateCost(current, target, current.getFinalCost() + MOVE_COST);
            }

            if (current.getY() + 1 < grid.length) {
                target = grid[current.getY() + 1][current.getX()];
                checkAndUpdateCost(current, target, current.getFinalCost() + MOVE_COST);
            }
        }

        // No path found
        return List.of();
    }

    public void printPath(List<Node<Tile>> path) {
        System.out.println("Path:");
        StringBuilder pathString = new StringBuilder();

        for (Node<Tile> node : path) {
            pathString.append(node).append(" -> ");
        }

        // remove the last arrow
        pathString.delete(pathString.length() - 4, pathString.length());
        System.out.println(pathString);
    }

    public void printGridWithPotentialTargets() {
        System.out.println("Grid:");
        PriorityQueue<Node<Tile>> targets = computeTargets();

        for (Node<Tile>[] nodes : grid) {
            for (Node<Tile> node : nodes) {
                if (node == null) {
                    System.out.print(" X ");
                } else if (targets.contains(node)) {
                    System.out.print(" * ");
                } else {
                    System.out.print(" O ");
                }
            }

            System.out.println();
        }
    }

    public void printGridWithPath(List<Node<Tile>> path) {
        System.out.println("Grid:");
        Set<Node<Tile>> pathSet = new HashSet<>(path);

        for (Node<Tile>[] nodes : grid) {
            for (Node<Tile> node : nodes) {
                if (node == null) {
                    System.out.print(" X ");
                } else if (pathSet.contains(node)) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" O ");
                }
            }

            System.out.println();
        }
    }
}
