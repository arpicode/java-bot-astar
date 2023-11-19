package com.example;

import java.util.List;

import com.example.astar.AStar;
import com.example.astar.Node;
import com.example.models.Tile;

public class Main {
    public static void main(String[] args) {
        AStar aStar = new AStar(10, 10);

        aStar.setBlocked(1, 4);
        aStar.setBlocked(3, 1);
        aStar.setBlocked(3, 2);
        aStar.setBlocked(3, 3);

        // Set start and end nodes
        aStar.setStartNode(0, 0);
        aStar.setEndNode(4, 4);

        aStar.printGridWithPotentialTargets();
        List<Node<Tile>> shortestPath = aStar.computeShortestPath();
        aStar.printPath(shortestPath);
        aStar.printGridWithPath(shortestPath);

        System.out.println(shortestPath.get(1));
    }
}
