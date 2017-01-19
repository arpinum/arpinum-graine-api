package arpinum.query;


import arpinum.infrastructure.bus.MessageCaptor;

public interface QueryHandler<TRecherche extends Query<TReponse>, TReponse> extends MessageCaptor<TRecherche, TReponse> {
}
