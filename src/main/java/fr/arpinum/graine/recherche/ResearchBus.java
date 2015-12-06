package fr.arpinum.graine.recherche;

import fr.arpinum.graine.infrastructure.bus.*;

import javax.inject.*;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class ResearchBus extends AsynchronousBus {

    @Inject
    public ResearchBus(Set<ResearchSynchronization> synchronizations, Set<ResearchHandler> handlers) {
        super(synchronizations, handlers);
    }

}
