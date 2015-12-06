package fr.arpinum.graine.recherche;

import fr.arpinum.graine.infrastructure.bus.MessageCaptor;

public interface ResearchHandler<TRecherche extends Research<TReponse>, TReponse> extends MessageCaptor<TRecherche, TReponse> {
}
