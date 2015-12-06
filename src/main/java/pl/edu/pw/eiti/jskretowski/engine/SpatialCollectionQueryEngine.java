package pl.edu.pw.eiti.jskretowski.engine;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.engine.CollectionQueryEngine;
import com.googlecode.cqengine.query.option.QueryOptions;
import pl.edu.pw.eiti.jskretowski.index.StatefulFallbackIndex;
import pl.edu.pw.eiti.jskretowski.index.StatefulIndex;
import pl.edu.pw.eiti.jskretowski.query.StatefulQuery;
import pl.edu.pw.eiti.jskretowski.query.distance.SimpleStatefulQuery;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static com.googlecode.cqengine.query.QueryFactory.*;
/**
 * An extended {@link CollectionQueryEngine}, allowing spatial queries and indexes to be used.
 * Most of methods from CollectionQueryEngine have been overloaded to accept {@link StatefulQuery}.
 */
public class SpatialCollectionQueryEngine<O, P extends Comparable<P>> {
    private volatile Set<O> collection;

    private final ConcurrentMap<Attribute<O, ?>, Set<StatefulIndex<O, P>>> attributeStatefulIndexes = new ConcurrentHashMap<>();
    // Falback stateful index when there are no indexes suitable for use with the query
    private final StatefulFallbackIndex<O, P> statefulFallbackIndex = new StatefulFallbackIndex<>();

    public void init(Set<O> collection, final QueryOptions queryOptions) {
        this.collection = collection;
        forEachIndex((StatefulIndex<O, P> index) -> {
            index.init(collection, queryOptions);
        });
    }

    private void forEachIndex(Consumer<StatefulIndex<O,P>> consumer) {
        consumer.accept(statefulFallbackIndex);
        for(Set<StatefulIndex<O, P>> indexes: attributeStatefulIndexes.values()) {
            for(StatefulIndex<O, P> index : indexes) {
                consumer.accept(index);
            }
        }
    }

    public <A> StatefulResultSet<O, P> retrieve(SimpleStatefulQuery<O, P, A> query, QueryOptions queryOptions) {
        return getResultSetWithLowestRetrievalCost(query, queryOptions);
    }

    public void addIndex(StatefulIndex<O, P> index) {
        addIndex(index, noQueryOptions());
    }

    public void addIndex(StatefulIndex<O, P> index, QueryOptions queryOptions) {

    }

    public Iterable<StatefulIndex<O, P>> getStatefulIndexes() {
        List<StatefulIndex<O, P>> statefulIndexes = new ArrayList<>();
        return statefulIndexes;
    }

    public Iterable<StatefulIndex<O, P>> getIndexesOnAttribute(Attribute<O, ?> attribute) {
        Set<StatefulIndex<O, P>> statefulIndexes = attributeStatefulIndexes.get(attribute);
        return statefulIndexes;
    }

    <A> StatefulResultSet<O, P> getResultSetWithLowestRetrievalCost(SimpleStatefulQuery<O, P, A> query, QueryOptions queryOptions) {
        Iterable<StatefulIndex<O, P>> indexesOnAttribute = getIndexesOnAttribute(query.getAttribute());
        if(indexesOnAttribute == null) {
            return statefulFallbackIndex.retrieve(query, queryOptions);
        }

        StatefulResultSet<O, P> lowestCostResultSet = null;
        for(StatefulIndex<O, P> index: indexesOnAttribute) {
            if(index.supportsQuery(query)) {
                StatefulResultSet<O, P> resultSet = index.retrieve(query, queryOptions);
                if(lowestCostResultSet == null || lowestCostResultSet.getRetrievalCost() > resultSet.getRetrievalCost()) {
                    lowestCostResultSet = resultSet;
                }
            }
        }
        if(lowestCostResultSet == null) {
            return statefulFallbackIndex.retrieve(query, queryOptions);
        }
        return lowestCostResultSet;
    }
}
