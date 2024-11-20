package ar.edu.unlu.poo.model;

// Enum para los valores de las cartas
public enum Rank {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("comodin"),
    QUEEN("reina"),
    KING("rey"),
    ACE("as");

    private final String value;

    Rank(String value) { this.value = value; }

    public String getValue() { return value; }
}

