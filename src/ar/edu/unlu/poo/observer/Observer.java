package ar.edu.unlu.poo.observer;

import ar.edu.unlu.poo.model.GameState;

public interface Observer {
    void update(GameState gameState);
}
