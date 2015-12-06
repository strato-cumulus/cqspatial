package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Created by strato on 29.10.2015.
 */
public class Contains<O, A extends Geometry> extends SimpleQuery<O,A> {

    private final A value;

    public Contains(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = value;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        return value.contains(attribute.getValue(object, queryOptions));
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for(A attributeValue : attribute.getValues(object, queryOptions)) {
            if(value.contains(attributeValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int calcHashCode() {
        return 31 * this.value.hashCode();
    }
}
