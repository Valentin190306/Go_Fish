package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.view.viewPanels.LobbyPanel;
import ar.edu.unlu.poo.view.viewPanels.MenuPanel;
import ar.edu.unlu.poo.view.viewPanels.ScoresPanel;

public interface IGameWindow {
    void handleException(Exception e);

    MenuPanel getMenuCard();

    LobbyPanel getLobbyCard();

    ScoresPanel getScoresCard();

    void start();

    void showGameCard();
}
