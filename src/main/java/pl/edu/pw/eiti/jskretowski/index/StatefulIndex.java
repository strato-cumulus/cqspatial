package pl.edu.pw.eiti.jskretowski.index;

import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import pl.edu.pw.eiti.jskretowski.query.StatefulQuery;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;

/**
 * A stateful index differ from a usual index in the way it calls the {@link StatefulQuery} for a quality measure
 * for a given element, instead of a boolean matches/does not match.
 * Separation from the {@link Index} interface occurred, because the index disallows {@link Query} from evaluation.
 */
public interface StatefulIndex<O, P> extends ModificationListener<O> {
    boolean isMutable();

    boolean supportsQuery(StatefulQuery<O, P> query);

    boolean isQuantized();

    StatefulResultSet<O, P> retrieve(StatefulQuery<O, P> query, QueryOptions queryOptions);
}
