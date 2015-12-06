package fr.arpinum.graine.recherche;

import org.jongo.*;

import javax.inject.*;

@SuppressWarnings("UnusedDeclaration")
public abstract class ResearchHandlerJongo<TRecherche extends Research<TReponse>, TReponse> implements ResearchHandler<TRecherche, TReponse> {

    public Jongo getJongo() {
        return jongo;
    }

    @Inject
    void setJongo(Jongo jongo) {
        this.jongo = jongo;
    }

    @Override
    public final TReponse execute(TRecherche recherche) {
        return execute(recherche, jongo);
    }

    protected abstract TReponse execute(TRecherche tRecherche, Jongo jongo);

    protected Find applyLimitAndSkip(Research<?> research, Find find) {
        return find.skip(research.skip()).limit(research.limit());
    }

    private Jongo jongo;
}
