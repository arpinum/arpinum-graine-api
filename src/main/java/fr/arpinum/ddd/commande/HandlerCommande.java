package fr.arpinum.ddd.commande;


public interface HandlerCommande<TCommande extends Commande<TReponse>, TReponse> {

    TReponse execute(TCommande commande);

     Class<TCommande> typeCommande();


}
