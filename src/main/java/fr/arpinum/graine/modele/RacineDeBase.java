package fr.arpinum.graine.modele;

@SuppressWarnings("UnusedDeclaration")
public abstract class RacineDeBase<TId> extends EntiteDeBase<TId> implements Racine<TId> {

    protected RacineDeBase() {
    }

    protected RacineDeBase(TId tId) {
        super(tId);
    }
}
