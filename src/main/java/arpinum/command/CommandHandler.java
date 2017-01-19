package arpinum.command;


import arpinum.infrastructure.bus.MessageCaptor;

public interface CommandHandler<TCommand extends Command<TResponse>, TResponse> extends MessageCaptor<TCommand, TResponse> {
}
