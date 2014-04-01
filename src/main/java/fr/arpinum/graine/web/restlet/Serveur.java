package fr.arpinum.graine.web.restlet;

import org.restlet.Component;
import org.restlet.data.Protocol;

@SuppressWarnings("UnusedDeclaration")
public class Serveur {

    public Serveur(BaseApplication application) {
        this.application = application;
    }

    public void start(int port) throws Exception {
        final Component component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach(application);
        component.start();
    }

    private final BaseApplication application;
}
