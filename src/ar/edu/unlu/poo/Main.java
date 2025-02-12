package ar.edu.unlu.poo;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.poo.interfaces.IGame;
import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.model.Game;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.view.ConsoleGameView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws RemoteException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("J1"));
        players.add(new Player("J2"));
        players.add(new Player("J3"));
        players.add(new Player("J4"));
/*
        IGame game = new Game(players);

        for (Player player : players) {
            IController controller = new Controller(game, player);
            ConsoleGameView view = new ConsoleGameView(controller);
        }

        game.dealInitialCards();
*/
    }
}

