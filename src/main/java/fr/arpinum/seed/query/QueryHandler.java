package fr.arpinum.seed.query;

import fr.arpinum.seed.infrastructure.bus.MessageCaptor;

public interface QueryHandler<TQuery extends Query<TResponse>, TResponse> extends MessageCaptor<TQuery, TResponse> {
}
