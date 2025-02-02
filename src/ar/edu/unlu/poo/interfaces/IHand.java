package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Value;

import java.util.List;

public interface IHand {
    boolean hasCardOfValue(Value value);
    List<ICard> getTransferenceCards();
    int getScore();
    String toString();
    int size();
}
