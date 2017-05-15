package arpinum.infrastructure.bus.event;

import arpinum.command.CommandMiddleware;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.EventBusMiddleware;
import arpinum.infrastructure.bus.Bus;

public interface CommandSynchronizedEventBus extends Bus, EventBus, CommandMiddleware, EventBusMiddleware {
}
