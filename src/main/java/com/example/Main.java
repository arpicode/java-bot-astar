package com.example;

import com.example.astar.AStar;

public class Main {
    public static void main(String[] args) {
        AStar aStar = new AStar(5, 5);

        aStar.setBlocked(0, 1);
        aStar.setBlocked(3, 1);
        aStar.setBlocked(3, 2);
        aStar.setBlocked(3, 3);

        // Set start and end nodes
        aStar.setStartNode(0, 0);
        aStar.setEndNode(4, 4);

        aStar.computePath();
        aStar.printPath();
        aStar.printGrid();
        aStar.printGridWithPath();
    }
}
