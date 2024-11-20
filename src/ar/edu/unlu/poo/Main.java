package ar.edu.unlu.poo;

import ar.edu.unlu.poo.controller.GameController;
import ar.edu.unlu.poo.interfaces.IGame;
import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.model.Game;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.view.ConsoleGameView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("J1"));
        players.add(new Player("J2"));

        IGame game = new Game(players);

        for (Player player : players) {
            IGameController controller = new GameController(game, player);
            ConsoleGameView view = new ConsoleGameView(controller);
        }

        game.dealInitialCards();
    }
}

