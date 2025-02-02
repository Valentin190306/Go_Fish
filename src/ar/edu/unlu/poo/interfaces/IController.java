package ar.edu.unlu.poo.interfaces;

public interface IController {
    boolean handlePlayerInput(String input); // Procesar entrada desde la vista
    void setView(IGameView view);
}

