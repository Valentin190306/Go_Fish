package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Hand;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.util.List;

public interface IPlayer extends Serializable {
    int getID();

    String getName();

    void setName(String name);

    PlayerState getPlayerState();

    void setPlayerState(PlayerState playerState);

    Hand getHand();

    String toString();
}
