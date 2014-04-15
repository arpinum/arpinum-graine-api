package fr.arpinum.graine.modele;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public abstract class EntiteDeBaseAvecUuid extends EntiteDeBase<UUID> {

    protected EntiteDeBaseAvecUuid() {
        super(UUID.randomUUID());
    }
}
