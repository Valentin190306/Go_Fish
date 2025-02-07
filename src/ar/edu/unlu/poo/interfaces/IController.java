package ar.edu.unlu.poo.interfaces;

public interface IController {
    boolean handlePlayerInput(String input);
    void setView(IGameView view);
}

