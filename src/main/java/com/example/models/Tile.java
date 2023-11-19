package com.example.models;

import com.example.interfaces.INode;

import lombok.Getter;
import lombok.Setter;

public class Tile implements INode {
    public enum Type {
        GRASS("grass", "G"),
        WOOD("wood", "W");

        @Getter
        private String name;

        @Getter
        private String shortName;

        Type(String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
        }
    }

    @Getter
    @Setter
    private Type type;

    @Getter
    @Setter
    private boolean unsafe;

    private int x;
    private int y;
    private INode parent;
    private int heuristicCost = 0;
    private int finalCost = 0;

    public Tile(Type type, int x, int y) {
        this.type = type;
        this.unsafe = false;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public void setParent(INode parent) {
        this.parent = parent;
    }

    @Override
    public int getHeuristicCost() {
        return heuristicCost;
    }

    @Override
    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    @Override
    public int getFinalCost() {
        return finalCost;
    }

    @Override
    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    @Override
    public String toString() {
        return this.type.getShortName() + "[" + this.x + ", " + this.y + "]";
    }
}
