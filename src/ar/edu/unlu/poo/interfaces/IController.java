package ar.edu.unlu.poo.interfaces;

public interface IController {

    void playerIsReady();

    boolean handlePlayerInput(String input);

    void setView(IGameView view);
}

