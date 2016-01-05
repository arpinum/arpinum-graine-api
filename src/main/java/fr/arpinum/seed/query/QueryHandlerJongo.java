package fr.arpinum.seed.query;

import org.jongo.*;

import javax.inject.*;

@SuppressWarnings("UnusedDeclaration")
public abstract class QueryHandlerJongo<TQuery extends Query<TResponse>, TResponse> implements QueryHandler<TQuery, TResponse> {

    public Jongo getJongo() {
        return jongo;
    }

    @Inject
    void setJongo(Jongo jongo) {
        this.jongo = jongo;
    }

    @Override
    public final TResponse execute(TQuery query) {
        return execute(query, jongo);
    }

    protected abstract TResponse execute(TQuery tQuery, Jongo jongo);

    protected Find applyLimitAndSkip(Query<?> query, Find find) {
        return find.skip(query.skip()).limit(query.limit());
    }

    private Jongo jongo;
}
