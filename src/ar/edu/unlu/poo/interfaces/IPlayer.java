package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Rank;

import java.util.List;

public interface IPlayer {
    String getName();
    int getScore();
    boolean hasCardOfRank(Rank rank);
    List<ICard> getHand();
    List<ICard> getTransferenceCards();
    String handToString();
}
