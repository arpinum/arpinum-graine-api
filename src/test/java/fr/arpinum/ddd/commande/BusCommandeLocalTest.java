package fr.arpinum.ddd.commande;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BusCommandeLocalTest {

    @Test
    public void peutExécuterUneCommande() {
        final FausseCommandeHandler handler = unHandler();
        BusCommandeLocal bus = busAvec(handler);
        FausseCommande commande = new FausseCommande("nom");

        bus.poste(commande);

        assertThat(handler.commandeReçue).isEqualTo(commande);
    }

    @Test
    public void estAsynchrone() {
        final ExecutorService executor = mock(ExecutorService.class);
        final BusCommandeLocal bus = bus();
        bus.setExecutor(executor);

        bus.poste(new FausseCommande("nom"));

        verify(executor).execute(anyObject());

    }

    @Test
    public void peutEncapsulerAvecUneSynchronisation() {
        final SynchronisationBus synchro = mock(SynchronisationBus.class);
        BusCommandeLocal bus = busAvec(synchro);
        final FausseCommande commande = new FausseCommande("nom");

        bus.poste(commande);

        verify(synchro).avantExecution(commande);
        verify(synchro).apresExecution();
    }

    @Test
    public void appelleToutDeMêmeLaSynchroSurErreur() {
        final FausseCommandeHandler handler = new FausseCommandeHandler();
        handler.renvoieException();
        final SynchronisationBus synchronisationBus = mock(SynchronisationBus.class);
        final BusCommandeLocal bus = busAvec(handler, synchronisationBus);

        bus.poste(new FausseCommande("nom"));

        verify(synchronisationBus).finalement();

    }

    @Test
    public void peutRetournerLeRésultatDeLaCommande() {
        final FausseCommandeHandler handler = new FausseCommandeHandler();
        final BusCommandeLocal bus = busAvec(handler);

        final ListenableFuture<ResultatCommande<String>> promesse = bus.poste(new FausseCommande("nom"));

        assertThat(promesse).isNotNull();
        final ResultatCommande<String> réponse = Futures.getUnchecked(promesse);
        assertThat(réponse.isSucces()).isTrue();
        assertThat(réponse.donnees()).isEqualTo("42");
    }

    @Test
    public void peutRetournerUnRésultatSurErreur() {
        final FausseCommandeHandler handler = new FausseCommandeHandler();
        handler.renvoieException();
        final BusCommandeLocal bus = busAvec(handler);

        final ListenableFuture<ResultatCommande<String>> promesse = bus.poste(new FausseCommande("nom"));

        assertThat(promesse).isNotNull();
        final ResultatCommande<String> réponse = Futures.getUnchecked(promesse);
        assertThat(réponse).isNotNull();
        assertThat(réponse.isErreur()).isTrue();
        assertThat(réponse.erreur()).isInstanceOf(RuntimeException.class);
        assertThat(réponse.erreur().getMessage()).isEqualTo("Ceci est une erreur");
    }


    private BusCommandeLocal bus() {
        return new BusCommandeLocal(Sets.newHashSet(mock(SynchronisationBus.class)), Sets.newHashSet(new FausseCommandeHandler()));
    }

    private BusCommandeLocal busAvec(FausseCommandeHandler handler, SynchronisationBus synchronisationBus) {
        final BusCommandeLocal bus = new BusCommandeLocal(Sets.newHashSet(synchronisationBus), Sets.newHashSet(handler));
        bus.setExecutor(executeur());
        return bus;
    }

    private ListeningExecutorService executeur() {
        return MoreExecutors.sameThreadExecutor();
    }

    private BusCommandeLocal busAvec(SynchronisationBus synchro) {
        return busAvec(unHandler(), synchro);
    }

    private BusCommandeLocal busAvec(FausseCommandeHandler handler) {
        return busAvec(handler, mock(SynchronisationBus.class));
    }

    private FausseCommandeHandler unHandler() {
        return new FausseCommandeHandler();
    }

    private class FausseCommande implements Commande<String> {

        private FausseCommande(String nom) {
            this.nom = nom;
        }

        @NotEmpty
        public String nom;
    }

    private class FausseCommandeHandler implements HandlerCommande<FausseCommande, String> {

        @Override
        public String execute(FausseCommande commande) {
            commandeReçue = commande;
            if (exception) {
                throw new RuntimeException("Ceci est une erreur");
            }
            return "42";
        }

        @Override
        public Class<FausseCommande> typeCommande() {
            return FausseCommande.class;
        }

        public void renvoieException() {
            this.exception = true;
        }

        private FausseCommande commandeReçue;
        private boolean exception;
    }
}
