package com.example.interfaces;

public interface INode {
    int getX();

    int getY();

    INode getParent();

    void setParent(INode parent);

    int getHeuristicCost();

    void setHeuristicCost(int heuristicCost);

    int getFinalCost();

    void setFinalCost(int finalCost);
}
