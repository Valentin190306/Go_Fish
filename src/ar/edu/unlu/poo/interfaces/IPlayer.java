package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Value;

import java.util.List;

public interface IPlayer {
    String getName();
    int getScore();
    boolean hasCardOfValue(Value value);
    List<ICard> getHand();
    List<ICard> getTransferenceCards();
    String handToString();
}
