package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Hand;
import ar.edu.unlu.poo.model.enums.Value;

import java.util.List;

public interface IPlayer {
    int getID();

    String getName();
    Hand getHand();
    String toString();
}
