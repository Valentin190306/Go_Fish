package ar.edu.unlu.poo.interfaces;

public interface IDeck {
    /**
     * Pila de cartas vacía?
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Obtener el tamaño de la pila de cartas en un momento dado
     * @return int
     */
    int size();
}
