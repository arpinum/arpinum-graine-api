package fr.arpinum.graine.modele;

public class Pair<T, U> {

    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair(first, second);
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public final T first;
    public final U second;
}
