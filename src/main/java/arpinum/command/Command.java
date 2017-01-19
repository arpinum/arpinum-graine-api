package arpinum.command;


import arpinum.infrastructure.bus.Message;

public interface Command<TReponse> extends Message<TReponse> {
}
