package fr.arpinum.graine.modele;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public abstract class RacineDeBaseAvecUuid extends RacineDeBase<UUID> implements RacineAvecUuid {

    protected RacineDeBaseAvecUuid() {
        super(UUID.randomUUID());
    }
}
