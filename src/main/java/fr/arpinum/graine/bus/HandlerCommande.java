package fr.arpinum.graine.bus;


public interface HandlerCommande<TCommande extends Commande<TReponse>, TReponse> {

    TReponse execute(TCommande commande);

    Class<TCommande> typeCommande();


}
