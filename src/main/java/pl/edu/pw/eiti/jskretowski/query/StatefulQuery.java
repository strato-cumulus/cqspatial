package pl.edu.pw.eiti.jskretowski.query;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A query supported by stateful indexes, returning a comparable value using a query-specific hardcoded fitness function
 * for any of the points contained in the queried set. A StatefulQuery is expected to have an object or a set of objects
 * towards which fitness is calculated.
 */
public interface StatefulQuery<O, P> {
    P fitness(O object, QueryOptions queryOptions);
}