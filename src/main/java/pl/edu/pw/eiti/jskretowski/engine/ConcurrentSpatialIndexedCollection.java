package pl.edu.pw.eiti.jskretowski.engine;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.index.support.DefaultConcurrentSetFactory;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.query.option.QueryOptions;
import pl.edu.pw.eiti.jskretowski.query.distance.SimpleStatefulQuery;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;

import static com.googlecode.cqengine.query.QueryFactory.*;

import java.util.Set;

public class ConcurrentSpatialIndexedCollection<O, P extends Comparable<P>, A> extends ConcurrentIndexedCollection<O> {

    protected final SpatialCollectionQueryEngine<O, P> spatialIndexEngine;

    public ConcurrentSpatialIndexedCollection() {
        this(new DefaultConcurrentSetFactory<O>());
    }

    public ConcurrentSpatialIndexedCollection(Factory<Set<O>> backingSetFactory) {
        super(backingSetFactory);
        SpatialCollectionQueryEngine<O, P> engine = new SpatialCollectionQueryEngine<>();
        spatialIndexEngine = new SpatialCollectionQueryEngine<>();
        spatialIndexEngine.init(super.collection, noQueryOptions());
    }

    public StatefulResultSet<O, P> retrieve(SimpleStatefulQuery<O, P, A> query, QueryOptions queryOptions) {
        return spatialIndexEngine.retrieve(query, queryOptions);
    }
}
