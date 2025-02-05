package ar.edu.unlu.poo.controller;

public class MatchBuilder {
    private static MatchBuilder instance = null;

    public MatchBuilder() {}

    public static MatchBuilder getInstance() {
        if (instance == null) {
            instance = new MatchBuilder();
            return instance;
        }
        else return instance;
    }


}
