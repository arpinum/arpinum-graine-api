package fr.arpinum.ddd.commande;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.Set;

public class BusCommandeLocal implements BusCommande {


    public BusCommandeLocal(Set<SynchronisationBus> synchronisations, Set<HandlerCommande> handlers) {
        for (HandlerCommande handler : handlers) {
            this.handlers.put(handler.typeCommande(), handler);
        }
        this.synchronisations.addAll(synchronisations);
    }


    @Override
    public <TReponse> ListenableFuture<ResultatCommande<TReponse>> poste(Commande<TReponse> commande) {
        final HandlerCommande handlerCommande = handlers.get(commande.getClass());
        handlerCommande.execute(commande);
        return null;
    }

    private final Set<SynchronisationBus> synchronisations = Sets.newHashSet();
    private final Map<Class<?>, HandlerCommande> handlers = Maps.newConcurrentMap();


}
