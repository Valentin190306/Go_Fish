package ar.edu.unlu.poo.observer;

import ar.edu.unlu.poo.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(GameState gameState) {
        for (Observer observer : observers) {
            observer.update(gameState);
        }
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }
}
