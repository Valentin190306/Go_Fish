package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.PlayerState;

import java.io.Serializable;

public class Player implements IPlayer, Serializable {
    private static int IDCounter = 1;
    private final int ID;
    private String name;
    private final Hand hand;
    private PlayerState playerState = PlayerState.WAITING;

    public Player() {
        this.name = "Guest" + IDCounter;
        this.ID = IDCounter;
        IDCounter++;
        this.hand = new Hand();
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public Hand getHand() {
        return hand;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return ID == player.ID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}