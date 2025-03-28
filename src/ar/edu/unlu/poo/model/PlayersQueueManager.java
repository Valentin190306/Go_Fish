package ar.edu.unlu.poo.model;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.util.ArrayList;

public class PlayersQueueManager extends ObservableRemoto {
    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

}
