package com.example.astar;

import lombok.Getter;
import lombok.Setter;

public class Node<T> {
    @Getter
    private int x;

    @Getter
    private int y;

    @Getter
    @Setter
    private T value;

    @Getter
    @Setter
    private Node<T> parent;

    @Getter
    @Setter
    private int heuristicCost = 0;

    @Getter
    @Setter
    private int finalCost = 0;

    public Node(int x, int y, T value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

}
