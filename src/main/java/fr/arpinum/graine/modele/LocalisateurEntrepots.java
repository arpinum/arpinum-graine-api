package fr.arpinum.graine.modele;

public abstract class LocalisateurEntrepots {

    public static void initialise(LocalisateurEntrepots instance) {
        LocalisateurEntrepots.instance = instance;
    }

    private static LocalisateurEntrepots instance;
}
