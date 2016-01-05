package fr.arpinum.seed.model;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public abstract class BaseEntityWithUuid extends BaseEntity<UUID> {

    protected BaseEntityWithUuid() {
        super(UUID.randomUUID());
    }
}
