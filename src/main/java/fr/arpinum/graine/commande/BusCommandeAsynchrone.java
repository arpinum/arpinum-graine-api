package fr.arpinum.graine.commande;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import fr.arpinum.graine.bus.*;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusCommandeAsynchrone implements BusCommande {


    @Inject
    public BusCommandeAsynchrone(Set<SynchronisationBus> synchronisations, Set<HandlerCommande> handlers) {
        for (HandlerCommande handler : handlers) {
            this.handlers.put(handler.typeCommande(), handler);
        }
        this.synchronisations.addAll(synchronisations);
    }


    @Override
    public <TReponse> ListenableFuture<ResultatCommande<TReponse>> poste(Commande<TReponse> commande) {
        final HandlerCommande handlerCommande = handlers.get(commande.getClass());
        return executorService.submit(execute(commande, handlerCommande));
    }

    private <TReponse> Callable<ResultatCommande<TReponse>> execute(Commande<TReponse> commande, HandlerCommande<Commande<TReponse>, TReponse> handlerCommande) {
        return () -> {
            try {
                synchronisations.forEach((synchro) -> synchro.avantExecution(commande));
                final TReponse reponse = handlerCommande.execute(commande);
                synchronisations.forEach(SynchronisationBus::apresExecution);
                return ResultatCommande.succes(reponse);
            } catch (Throwable e) {
                synchronisations.forEach(SynchronisationBus::surErreur);
                return ResultatCommande.erreur(e);
            } finally {
                synchronisations.forEach(SynchronisationBus::finalement);
            }
        };
    }

    public void setExecutor(ExecutorService executor) {
        this.executorService = MoreExecutors.listeningDecorator(executor);
    }

    private final Set<SynchronisationBus> synchronisations = Sets.newHashSet();
    private final Map<Class<?>, HandlerCommande> handlers = Maps.newConcurrentMap();
    private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
}
