package fr.arpinum.seed.model.event;


import fr.arpinum.seed.infrastructure.bus.MessageCaptor;

@SuppressWarnings("UnusedDeclaration")
public interface EventCaptor<TEvent extends Event> extends MessageCaptor<TEvent, Void> {

    @Override
    default Void execute(TEvent event) {
        executeEvent(event);
        return null;
    }

    void executeEvent(TEvent event);
}
