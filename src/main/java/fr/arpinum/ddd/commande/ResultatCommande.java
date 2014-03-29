package fr.arpinum.ddd.commande;

public class ResultatCommande<TReponse> {

    public static <TReponse> ResultatCommande<TReponse> succes(TReponse reponse) {
        return new ResultatCommande<>(reponse, true);
    }

    public static <TReponse> ResultatCommande<TReponse> erreur(Throwable erreur) {
        return new ResultatCommande<>(erreur);
    }


    private ResultatCommande(TReponse reponse, boolean succes) {
        this.reponse = reponse;
        this.succes = succes;
        erreur = null;
    }

    public ResultatCommande(Throwable erreur) {
        this.erreur = erreur;
        succes = false;
        reponse = null;
    }

    public TReponse donnees() {
        return reponse;
    }

    public boolean isSucces() {
        return succes;
    }

    public boolean isErreur() {
        return !succes;
    }

    public Throwable erreur() {
        return erreur;
    }

    private final TReponse reponse;
    private final boolean succes;
    private final Throwable erreur;
}
