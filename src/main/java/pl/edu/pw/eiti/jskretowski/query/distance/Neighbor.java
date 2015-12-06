package pl.edu.pw.eiti.jskretowski.query.distance;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.vividsolutions.jts.geom.Geometry;

import java.util.function.Function;

/**
 * A query implementing search for nearest N neighbors. When called, returns the distance from the
 */
public final class Neighbor<O, A extends Geometry> extends SimpleStatefulQuery<O, Double, A> {
    private final Attribute<O, A> attribute;
    private final A value;

    public Neighbor(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Returns the opposite of distance between value and tested point. This ensures the values are
     * ordered closest-last.
     * @param object an object tested against this query's value
     * @param queryOptions query options (e.g. {@link pl.edu.pw.eiti.jskretowski.query.option.Top}).
     * @return
     */
    @Override
    public Double fitness(O object, QueryOptions queryOptions) {
        Iterable<A> values = attribute.getValues(object, queryOptions);
        return -value.distance(values.iterator().next());
    }

    @Override
    protected int calcHashCode() {
        return 0;
    }
}
