package com.example.models;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

public class Tile {
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

    private Player player;

    private Bomb bomb;

    private Bonus bonus;

    @Getter
    @Setter
    private boolean unsafe;

    public Tile(Type type) {
        this.type = type;
        this.unsafe = false;
    }

    @Override
    public String toString() {
        return this.type.getShortName();
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    public Optional<Bomb> getBomb() {
        return Optional.ofNullable(bomb);
    }

    public Optional<Bonus> getBonus() {
        return Optional.ofNullable(bonus);
    }
}
