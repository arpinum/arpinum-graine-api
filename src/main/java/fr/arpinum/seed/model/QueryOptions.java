package fr.arpinum.seed.model;

import java.util.*;

public class QueryOptions {

    public Optional<SortOptions> sortOptions = Optional.empty();

    public Optional<Integer> limit = Optional.empty();

    public Optional<Integer> skip = Optional.empty();


    public QueryOptions withSortOptions(SortOptions options) {
        this.sortOptions = Optional.of(options);
        return this;
    }

    public QueryOptions withLimit(int limit) {
        this.limit = Optional.of(limit);
        return this;
    }

    public QueryOptions withSkip(int skip) {
        this.skip = Optional.of(skip);
        return this;
    }
}
