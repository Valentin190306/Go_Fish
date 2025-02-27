package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.view.LobbyPanel;
import ar.edu.unlu.poo.view.MenuPanel;
import ar.edu.unlu.poo.view.ScoresPanel;

public interface IGameWindow {
    MenuPanel getMenuCard();

    LobbyPanel getLobbyCard();

    ScoresPanel getScoresCard();

    void start();
}
