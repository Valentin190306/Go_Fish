package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IPlayer;

import java.io.Serializable;

public class Player implements IPlayer, Serializable {
    private final String name;
    private final Hand hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Hand getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}