package fr.arpinum.seed.infrastructure.persistence.mongo;

import com.google.common.reflect.*;
import fr.arpinum.seed.model.Aggregate;
import fr.arpinum.seed.model.Repository;
import fr.arpinum.seed.model.QueryOptions;
import fr.arpinum.seed.model.SortOptions;
import org.mongolink.*;
import org.mongolink.domain.criteria.*;

import java.util.function.*;

public abstract class MongolinkRepository<TId, TRoot extends Aggregate<TId>> implements Repository<TId, TRoot> {

    protected MongolinkRepository(MongoSession session) {
        this.session = session;
    }


    @Override
    public TRoot get(TId tId) {
        return getSession().get(tId, entityType());
    }

    protected Class<TRoot> entityType() {
        return (Class<TRoot>) typeToken.getRawType();
    }

    protected MongoSession getSession() {
        return session;
    }

    @Override
    public void add(TRoot root) {
        getSession().save(root);
    }

    @Override
    public void delete(TRoot root) {
        getSession().delete(root);
    }

    protected void applyOptions(QueryOptions options, Criteria criteria) {
        options.sortOptions.ifPresent(applySort(criteria));
        options.limit.ifPresent(applyLimit(criteria));
        options.skip.ifPresent(applySkip(criteria));
    }

    private Consumer<Integer> applySkip(Criteria criteria) {
        return s -> criteria.skip(s);
    }

    private Consumer<Integer> applyLimit(Criteria criteria) {
        return l -> criteria.limit(l);
    }

    private Consumer<SortOptions> applySort(Criteria criteria) {
        return so -> criteria.sort(so.field, so.order == SortOptions.Order.ASC ? Order.ASCENDING : Order.DESCENDING);
    }

    private final MongoSession session;
    private final TypeToken<TRoot> typeToken = new TypeToken<TRoot>(getClass()) {
    };
}
