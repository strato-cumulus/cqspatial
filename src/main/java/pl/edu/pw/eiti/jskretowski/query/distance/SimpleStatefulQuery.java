package pl.edu.pw.eiti.jskretowski.query.distance;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import pl.edu.pw.eiti.jskretowski.query.StatefulQuery;

public abstract class SimpleStatefulQuery<O, P, A> implements StatefulQuery<O, P> {
    protected final Attribute<O, A> attribute;

    private transient int cachedHashCode = 0;

    protected SimpleStatefulQuery(Attribute<O, A> attribute) {
        this.attribute = attribute;
    }

    @Override
    public abstract P fitness(O object, QueryOptions queryOptions);

    @Override
    public int hashCode() {
        int h = this.cachedHashCode;
        if(h == 0) {
            h = calcHashCode();
            if(h == 0) {
                h = -1084033327; // fallback value
            }
            this.cachedHashCode = h;
        }
        return h;
    }

    public Attribute<O, A> getAttribute() {
        return this.attribute;
    }

    abstract protected int calcHashCode();

    protected static String asLiteral(Object value) {
        return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
    }
}
