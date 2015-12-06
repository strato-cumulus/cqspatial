package pl.edu.pw.eiti.jskretowski.query;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.vividsolutions.jts.geom.Geometry;
import pl.edu.pw.eiti.jskretowski.query.simple.*;

/**
 * Created by strato on 02.11.2015.
 */
public class SpatialQueryFactory {
    SpatialQueryFactory() {
    }

    public static <O, A extends Geometry> Query<O> contains(Attribute<O, A> attribute, A attributeValue) {
        return new Contains<>(attribute, attributeValue);
    }

    public static <O, A extends Geometry> Query<O> covers(Attribute<O, A> attribute, A attributeValue) {
        return new Covers<>(attribute, attributeValue);
    }

    public static <O, A extends Geometry> Query<O> disjoint(Attribute<O, A> attribute, A attributeValue) {
        return new Disjoint<>(attribute, attributeValue);
    }

    public static <O, A extends Geometry> Query<O> equalTo(Attribute<O, A> attribute, A attributeValue) {
        return new EqualTo<>(attribute, attributeValue);
    }

    public static <O, A extends Geometry> Query<O> intersects(Attribute<O, A> attribute, A attributeValue) {
        return new Intersects<>(attribute, attributeValue);
    }

    public static <O, A extends Geometry> Query<O> overlaps(Attribute<O, A> attribute, A attributeValue) {
        return new Overlaps<>(attribute, attributeValue);
    }
}
