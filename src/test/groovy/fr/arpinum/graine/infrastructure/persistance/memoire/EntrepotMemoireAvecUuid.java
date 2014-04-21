package fr.arpinum.graine.infrastructure.persistance.memoire;

import fr.arpinum.graine.modele.EntrepotAvecUuid;
import fr.arpinum.graine.modele.RacineAvecUuid;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public class EntrepotMemoireAvecUuid<TRacine extends RacineAvecUuid> extends EntrepotMemoire<UUID, TRacine> implements EntrepotAvecUuid<TRacine> {
}
