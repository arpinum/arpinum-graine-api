package fr.arpinum.ddd.commande;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class BusCommandeLocalTest {

    @Test
    public void peutExécuterUneCommande() {
        final FausseCommandeHandler handler = new FausseCommandeHandler();
        BusCommandeLocal bus = new BusCommandeLocal(Sets.newHashSet(mock(SynchronisationBus.class)), Sets.newHashSet(handler));
        FausseCommande commande = new FausseCommande();

        bus.poste(commande);

        assertThat(handler.commandeReçue).isEqualTo(commande);
    }

    private class FausseCommande implements Commande<String> {

    }

    private class FausseCommandeHandler implements HandlerCommande<FausseCommande, String> {

        @Override
        public String execute(FausseCommande commande) {
            commandeReçue = commande;
            return "";
        }

        @Override
        public Class<FausseCommande> typeCommande() {
            return FausseCommande.class;
        }

        private FausseCommande commandeReçue;
    }
}
