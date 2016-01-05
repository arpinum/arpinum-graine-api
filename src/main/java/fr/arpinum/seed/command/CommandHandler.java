package fr.arpinum.seed.command;

import fr.arpinum.seed.infrastructure.bus.MessageCaptor;

public interface CommandHandler<TCommand extends Command<TResponse>, TResponse> extends MessageCaptor<TCommand, TResponse> {
}
