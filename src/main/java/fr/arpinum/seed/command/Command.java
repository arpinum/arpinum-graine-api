package fr.arpinum.seed.command;

import fr.arpinum.seed.infrastructure.bus.Message;

public interface Command<TResponse> extends Message<TResponse> {
}
