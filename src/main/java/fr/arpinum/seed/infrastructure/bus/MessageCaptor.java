package fr.arpinum.seed.infrastructure.bus;


import com.google.common.reflect.TypeToken;

public interface MessageCaptor<TCommand extends Message<TResponse>, TResponse> {

    TResponse execute(TCommand command);

    default Class<TCommand> typeCommand() {
        return (Class<TCommand>) new TypeToken<TCommand>(getClass()) {
        }.getRawType();
    }


}
