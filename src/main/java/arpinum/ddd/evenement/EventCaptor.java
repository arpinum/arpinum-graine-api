package arpinum.ddd.evenement;


import arpinum.infrastructure.Nothing;
import arpinum.infrastructure.bus.Message;
import arpinum.infrastructure.bus.MessageCaptor;

@SuppressWarnings("UnusedDeclaration")
public interface EventCaptor<TEvenement extends Event<?>> extends MessageCaptor<TEvenement, Nothing> {

    @Override
    default Nothing execute(TEvenement command) {
        executeEvent(command);
        return Nothing.INSTANCE;
    }

    void executeEvent(TEvenement evenement);
}
