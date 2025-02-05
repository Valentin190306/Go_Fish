package ar.edu.unlu.poo.controller;

public static class MatchBuilder {
    private MatchBuilder instance = null;

    public MatchBuilder() {}

    public static MatchBuilder getInstance() {
        if (instance == null) {
            instance = new MatchBuilder();
            return instance;
        }
        else return instance;
    }

    public
}
