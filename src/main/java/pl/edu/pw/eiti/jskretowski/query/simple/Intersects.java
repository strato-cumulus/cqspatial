package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;
import com.vividsolutions.jts.geom.Geometry;

public class Intersects<O, A extends Geometry> extends SimpleQuery<O, A> {

    private final A value;

    public Intersects(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = value;
    }

    @Override
    public int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + attribute.hashCode();
        return result;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A attributeValue = attribute.getValue(object, queryOptions);
        return attributeValue.intersects(value);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for(A value : attribute.getValues(object, queryOptions)) {
            if(value.intersects(this.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
