package fr.arpinum.graine.recherche;

import com.google.common.util.concurrent.ListenableFuture;
import fr.arpinum.graine.bus.Bus;
import fr.arpinum.graine.bus.Commande;
import fr.arpinum.graine.bus.ResultatCommande;

public class BusRecherche implements Bus {

    @Override
    public <TReponse> ListenableFuture<ResultatCommande<TReponse>> poste(Commande<TReponse> commande) {
        return null;
    }
}
