package com.example.astar;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import com.example.interfaces.INode;
import com.example.models.Tile;

public class AStar {

    private static final int MOVE_COST = 10;

    private INode[][] grid;
    private PriorityQueue<INode> open;
    private boolean[][] closed;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public AStar(int width, int height) {
        this.grid = new INode[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                INode node = new Tile(Tile.Type.GRASS, x, y);
                this.grid[y][x] = node;
            }
        }
    }

    public void addNode(INode node) {
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

    private void checkAndUpdateCost(INode current, INode target, int cost) {
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

    public void computePath() {
        open = new PriorityQueue<>(Comparator.comparingInt(node -> node.getFinalCost() + node.getHeuristicCost()));
        closed = new boolean[grid.length][grid[0].length];
        open.add(grid[startY][startX]);

        INode current;
        while (!open.isEmpty()) {
            current = open.poll();
            closed[current.getY()][current.getX()] = true;

            if (current.equals(grid[endY][endX])) {
                return;
            }

            INode target;
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
    }

    public void printPath() {
        if (closed[endY][endX]) {
            System.out.println("Path:");
            INode current = grid[endY][endX];
            while (current.getParent() != null) {
                System.out.println(current + " -> " + current.getParent());
                current = current.getParent();
            }
        } else
            System.out.println("No possible path");
    }

    public void printGrid() {
        System.out.println("Grid:");
        for (INode[] nodes : grid) {
            for (INode node : nodes) {
                if (node == null) {
                    System.out.print("X");
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
    }

    public void printGridWithPath() {
        System.out.println("Grid:");
        INode current = grid[endY][endX];
        Set<INode> path = new HashSet<>();
        while (current.getParent() != null) {
            path.add(current);
            current = current.getParent();
        }
        path.add(current); // Add the start node

        for (INode[] nodes : grid) {
            for (INode node : nodes) {
                if (node == null) {
                    System.out.print("X");
                } else if (path.contains(node)) {
                    System.out.print(".");
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
    }
}
