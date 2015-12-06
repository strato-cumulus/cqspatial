package pl.edu.pw.eiti.jskretowski.index;

import com.googlecode.cqengine.index.fallback.FallbackIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import pl.edu.pw.eiti.jskretowski.query.StatefulQuery;
import pl.edu.pw.eiti.jskretowski.query.option.Top;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;
import pl.edu.pw.eiti.jskretowski.util.CappedPriorityQueue;

import java.util.*;

/**
 * A fallback index for all queries stateful, i.e. those which need an auxiliary, finite-length state queue
 * due to their query result being dependent on all the other elements of the collection.
 */
public class StatefulFallbackIndex<O, P extends Comparable<P>> implements StatefulIndex<O, P> {
    // Use of this index shall be attempted before trying FallbackIndex, as the latter won't produce expected results
    // for state-dependent queries.
    private static final int INDEX_RETRIEVAL_COST = Integer.MAX_VALUE - 1;
    private static final int INDEX_MERGE_COST = Integer.MAX_VALUE - 1;

    private Set<O> collection = Collections.emptySet();

    /**
     * {@inheritDoc}
     * <p/>
     * This index is mutable.
     *
     * @return true
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>This index is reserved for stateful queries, letting other ones be passed to the {@link FallbackIndex}.</b>
     *
     * @return true if the object is an instance of {@link StatefulQuery}.
     */
    @Override
    public boolean supportsQuery(StatefulQuery<O, P> query) {
        return false;
    }

    @Override
    public boolean isQuantized() {
        return false;
    }

    @Override
    public StatefulResultSet<O, P> retrieve(final StatefulQuery<O, P> query, final QueryOptions queryOptions) {
        final Top top = queryOptions.get(Top.class);
        Queue<O> state;
        if(top != null) {
            state = CappedPriorityQueue.newInstance(top.getValue(),
                    (o1, o2)->query.fitness(o1, queryOptions).compareTo(query.fitness(o2, queryOptions)));
        }
        else {
            state = new PriorityQueue<>();
        }
        return new StatefulResultSet<O, P>() {
            boolean fetched = false;
            @Override
            public Iterator<O> iterator() {
                if(!fetched) {
                    fetchAll();
                }
                return state.iterator();
            }

            @Override
            public boolean contains(O o) {
                if(!fetched) {
                    fetchAll();
                }
                return state.contains(o);
            }

            @Override
            public StatefulQuery<O, P> getQuery() {
                return query;
            }

            @Override
            public QueryOptions getQueryOptions() {
                return queryOptions;
            }

            @Override
            public int getRetrievalCost() {
                return StatefulFallbackIndex.INDEX_RETRIEVAL_COST;
            }

            @Override
            public int getMergeCost() {
                return StatefulFallbackIndex.INDEX_MERGE_COST;
            }

            @Override
            public int size() {
                if(!fetched) {
                    fetchAll();
                }
                return state.size();
            }

            @Override
            public void close() {
                //nothing to close
            }

            // On first iterator()/contains() call, retrieve all elements
            private void fetchAll() {
                fetched = true;
                state.addAll(collection);
            }
        };
    }

    @Override
    public boolean addAll(Collection<O> collection, QueryOptions queryOptions) {
        return this.collection.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<O> collection, QueryOptions queryOptions) {
        return this.collection.removeAll(collection);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        this.collection = Collections.emptySet();
    }

    @Override
    public void init(Set<O> set, QueryOptions queryOptions) {
        this.collection = set;
    }
}
