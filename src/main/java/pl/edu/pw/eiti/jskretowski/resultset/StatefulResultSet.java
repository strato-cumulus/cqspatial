package pl.edu.pw.eiti.jskretowski.resultset;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.common.NoSuchObjectException;
import com.googlecode.cqengine.resultset.common.NonUniqueObjectException;
import com.vividsolutions.jts.geom.Geometry;
import pl.edu.pw.eiti.jskretowski.query.StatefulQuery;

import java.sql.ResultSet;
import java.util.Iterator;

/**
 * This is a {@link ResultSet} equivalent for {@link StatefulQuery}, with matches() method removed (it would
 * have to be synonymous with contains()).
 */
public interface StatefulResultSet<O, P> extends Iterable<O> {
    /**
     * Returns true if this {@link ResultSet} contains the given object, false if it does not.
     * <p/>
     * Note that the cost of calling this method will most likely be cheaper than iterating all results to check if an
     * object is contained. If indexes are available to support the query, this method will query indexes to check if
     * the object is contained, instead of actually retrieving any data.
     * <p/>
     *
     * @param object The object to check for containment in this {@link ResultSet}
     * @return True if this {@link ResultSet} contains the given object, false if it does not
     */
    boolean contains(O object);

    /**
     * Returns the query for which this ResultSet provides results.
     * @return The query for which this ResultSet provides results.
     */
    StatefulQuery<O, P> getQuery();

    /**
     * Returns the query options associated with the query.
     * @return The query options associated with the query.
     */
    QueryOptions getQueryOptions();

    /**
     * Returns the first object returned by the iterator of this {@link ResultSet}, and throws an exception if
     * the iterator does not provide exactly one object.
     *
     * @return The first object returned by the iterator of this {@link ResultSet}
     * @throws NoSuchObjectException If the iterator indicates no object is available
     * @throws NonUniqueObjectException If the iterator indicates more than one object is available
     */
    default O uniqueResult() {
        Iterator<O> iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchObjectException("ResultSet is empty");
        }
        O result = iterator.next();
        if (iterator.hasNext()) {
            throw new NonUniqueObjectException("ResultSet contains more than one object");
        }
        return result;
    }

    /**
     * Returns an estimate of the cost of looking up objects matching the query
     * underlying this {@code ResultSet} in the index.
     * <p/>
     * The query engine will use this to select the index with the lowest cost,
     * when more than one index supporting the query exists for the same attribute.
     * <p/>
     * An example: a single-level hash index will typically have a lower retrieval cost than a tree-based index. Of
     * course a hash index only supports equality-based retrieval whereas a sorted tree-based index might common
     * equality/less than/greater than or range based retrieval. But for an equality-based query, supported by
     * both indexes, retrieval cost allows the query engine to <i>prefer</i> the hash index.
     *
     * @return An estimate of the cost of looking up a particular query in the index
     */
    int getRetrievalCost();

    /**
     * Returns an estimate of the cost of merging (or otherwise processing) objects matching the query.
     * <p/>
     * This will typically be based on the number of objects matching the query.
     * <ul>
     *     <li>
     *         If the query specifies a simple retrieval from an index, this might be the number of objects matching
     *         the query
     *     </li>
     *     <li>
     *         If the query specifies a union between multiple other sub-queries, this might be the sum of their merge
     *         costs
     *     </li>
     *     <li>
     *         If the query specifies an intersection, this might be the based on the merge cost of the sub-query with
     *         the lowest merge cost
     *     </li>
     * </ul>
     * The query engine will use this to optimize the order of intersections and unions, and to decide between merging
     * versus filtering strategies.
     *
     * @return An estimate of the cost of merging (or otherwise processing) objects matching a query
     */
    int getMergeCost();

    /**
     * Returns the number of objects which would be returned by this {@code ResultSet} if iterated.
     * <p/>
     * Note that the cost of calling this method depends on the query for which it was constructed.
     * <p/>
     * For simple queries where a single query is supplied and a matching index exists, or where several such simple
     * queries are supplied and are connected using a simple {@link com.googlecode.cqengine.query.logical.Or}
     * query, calculating the size via this method will be cheaper than iterating through the ResultSet and counting
     * the number of objects individually.
     * <p/>
     * For more complex queries, where intersections must be performed or where no suitable indexes exist, calling this
     * method can be non-trivial, but it will always be at least as cheap as iterating through the ResultSet and
     * counting the number of objects individually.
     *
     * @return The number of objects which would be returned by this {@code ResultSet} if iterated
     */
    int size();

    /**
     * Checks if this {@code ResultSet} if iterated would not return any objects (i.e. the query does not match any
     * objects).
     * <p/>
     * This method can be more efficient than calling {@code #size()} to check simply if no objects would be
     * returned.
     *
     * @return True if this {@code ResultSet} if iterated would not return any objects; false if the {@code ResultSet}
     * would return objects
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Checks if this {@code ResultSet} if iterated would return some objects (i.e. the query matches some
     * objects).
     * <p/>
     * This method can be more efficient than calling {@code #size()} to check simply if some objects would be
     * returned.
     *
     * @return True if this {@code ResultSet} if iterated would return some objects; false if the {@code ResultSet}
     * would not return any objects
     */
    default boolean isNotEmpty() {
        return iterator().hasNext();
    }

    /**
     * Releases any resources or closes the transaction which was opened for this ResultSet.
     * <p/>
     * Whether or not it is necessary to close the ResultSet depends on which implementation of
     * IndexedCollection is in use and the types of indexes added to it.
     */
    void close();
}
