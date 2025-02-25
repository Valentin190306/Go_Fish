package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Hand;
import ar.edu.unlu.poo.model.enums.PlayerState;

import java.io.Serializable;

public interface IPlayer extends Serializable {

    int getID();

    String getName();

    PlayerState getPlayerState();

    Hand getHand();

    String toString();
}
