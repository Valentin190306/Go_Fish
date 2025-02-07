package ar.edu.unlu.poo.interfaces;

public interface IDeck {
    /**
     * Pregunta si la pila de cartas esta vacía
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Obtener el tamaño de la pila de cartas en un momento dado
     * @return int
     */
    int size();
}
