package votreapplication.modele;

import javax.inject.Inject;

public abstract class LocalisateurEntrepots {

    public static void initialise(LocalisateurEntrepots instance) {
        LocalisateurEntrepots.instance = instance;
    }

    @Inject
    private static LocalisateurEntrepots instance;
}
