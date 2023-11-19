package com.example.interfaces;

import com.example.models.Tile;

public interface INode {
    int getX();

    int getY();

    INode getParent();

    void setParent(INode parent);

    int getHeuristicCost();

    void setHeuristicCost(int heuristicCost);

    int getFinalCost();

    void setFinalCost(int finalCost);

    Tile.Type getType();

    void setType(Tile.Type type);

    boolean isUnsafe();

    void setUnsafe(boolean unsafe);

    boolean hasBomb();
}
