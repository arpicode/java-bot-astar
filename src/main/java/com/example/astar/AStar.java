package com.example.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
        // TODO: initialize grid based on game context
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

    private PriorityQueue<INode> computeTargets() {
        PriorityQueue<INode> targets = new PriorityQueue<>(Comparator.comparingInt(INode::getHeuristicCost));

        for (int y = 0; y < grid.length; ++y) {
            for (int x = 0; x < grid[0].length; ++x) {
                // look for blocked nodes
                if (grid[y][x] == null) {
                    // check if there is a non-blocked node adjacent to the blocked node
                    if (y - 1 >= 0 && grid[y - 1][x] != null && !targets.contains(grid[y - 1][x])) {
                        targets.add(grid[y - 1][x]);
                    }
                    if (x - 1 >= 0 && grid[y][x - 1] != null && !targets.contains(grid[y][x - 1])) {
                        targets.add(grid[y][x - 1]);
                    }
                    if (x + 1 < grid[0].length && grid[y][x + 1] != null && !targets.contains(grid[y][x + 1])) {
                        targets.add(grid[y][x + 1]);
                    }
                    if (y + 1 < grid.length && grid[y + 1][x] != null && !targets.contains(grid[y + 1][x])) {
                        targets.add(grid[y + 1][x]);
                    }
                }
            }
        }

        return targets;
    }

    public List<INode> computeShortestPath() {
        PriorityQueue<INode> targets = computeTargets();
        List<INode> shortestPath = new ArrayList<>();
        int shortestPathLength = Integer.MAX_VALUE;

        for (INode target : targets) {
            setEndNode(target.getX(), target.getY());
            List<INode> path = computePath();

            if (path.size() < shortestPathLength) {
                shortestPath = path;
                shortestPathLength = path.size();
            }
        }

        return shortestPath;
    }

    public List<INode> computePath() {
        open = new PriorityQueue<>(Comparator.comparingInt(node -> node.getFinalCost() + node.getHeuristicCost()));
        closed = new boolean[grid.length][grid[0].length];
        open.add(grid[startY][startX]);

        INode current;
        while (!open.isEmpty()) {
            current = open.poll();
            closed[current.getY()][current.getX()] = true;

            if (current.equals(grid[endY][endX])) {
                // We've reached the end node, construct and return the path
                List<INode> path = new ArrayList<>();

                while (current != null) {
                    path.add(current);
                    current = current.getParent();
                }
                Collections.reverse(path); // Reverse the path to start from the beginning

                return path;
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

        // No path found
        return List.of();
    }

    public void printPath(List<INode> path) {
        System.out.println("Path:");
        StringBuilder pathString = new StringBuilder();

        for (INode node : path) {
            pathString.append(node).append(" -> ");
        }

        // remove the last arrow
        pathString.delete(pathString.length() - 4, pathString.length());
        System.out.println(pathString);
    }

    public void printGridWithPotentialTargets() {
        System.out.println("Grid:");
        PriorityQueue<INode> targets = computeTargets();

        for (INode[] nodes : grid) {
            for (INode node : nodes) {
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

    public void printGridWithPath(List<INode> path) {
        System.out.println("Grid:");
        Set<INode> pathSet = new HashSet<>(path);

        for (INode[] nodes : grid) {
            for (INode node : nodes) {
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
