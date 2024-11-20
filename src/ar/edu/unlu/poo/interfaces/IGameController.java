package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.observer.Observer;

public interface IGameController extends Observer {
    boolean handlePlayerInput(String input); // Procesar entrada desde la vista
    void initializeGame(); // Inicializar y registrar el controlador
    void setView(IGameView view);
}

