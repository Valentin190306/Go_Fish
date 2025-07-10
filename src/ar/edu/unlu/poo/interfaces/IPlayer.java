package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;

import java.util.List;
import java.util.Map;

public interface IPlayer {
    int getID();

    String getName();

    PlayerState getPlayerState();

    Card getFishedCard();

    boolean hasCardOfValue(Value value);

    List<Card> getAvailableCards();

    Map<Value, Integer> countAvailableCardsByValue();

    int getHandSize();

    List<Card> getLastTransferenceCards();

    List<List<Card>> getCompletedSets();

    int getScore();

    boolean hasCompletedSets();

    @Override
    boolean equals(Object obj);

    @Override
    String toString();
}
