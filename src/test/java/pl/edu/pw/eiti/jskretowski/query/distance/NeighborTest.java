package pl.edu.pw.eiti.jskretowski.query.distance;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import org.junit.Test;
import pl.edu.pw.eiti.jskretowski.engine.ConcurrentSpatialIndexedCollection;
import pl.edu.pw.eiti.jskretowski.query.option.Top;
import pl.edu.pw.eiti.jskretowski.query.simple.EqualTo;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;

import java.util.ArrayList;
import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;

public class NeighborTest {
    private ConcurrentSpatialIndexedCollection<PointWrapper, Double, Point> collection
            = new ConcurrentSpatialIndexedCollection<>();
    private GeometryFactory factory = new GeometryFactory();

    @Test
    public void testNeighbor() {
        Point testPoint = factory.createPoint(new Coordinate(4., 4.));
        PointWrapper[] closerPoints = {
                new PointWrapper(factory.createPoint(new Coordinate(3., 4.))),
                new PointWrapper(factory.createPoint(new Coordinate(4., 3.))),
                new PointWrapper(factory.createPoint(new Coordinate(5. ,4.))),
                new PointWrapper(factory.createPoint(new Coordinate(4., 5.)))
        };
        collection.addAll(Arrays.asList(closerPoints));
        StatefulResultSet rs = collection.retrieve(new Neighbor<>(getAttribute(), testPoint), queryOptions(new Top(2)));
        Assert.assertEquals(2, rs.size());
        collection.clear();
    }

    @Test
    public void largeTestNeighbor() {
        Point testPoint = factory.createPoint(new Coordinate(0, 0));
        for(int i = 0; i < 100000; ++i) {
            collection.add(new PointWrapper(factory.createPoint(new Coordinate(0, i))));
        }
        StatefulResultSet rs = collection.retrieve(new Neighbor<>(getAttribute(), testPoint), queryOptions(new Top(10000)));
        PointWrapper pw = collection.retrieve(new EqualTo<>(getAttribute(), factory.createPoint(new Coordinate(0, 9999))), noQueryOptions()).uniqueResult();
        Assert.assertEquals(10000, rs.size());
        Assert.assertTrue(rs.contains(pw));
    }

    class PointWrapper {
        final Point point;
        PointWrapper(Point point) {
            this.point = point;
        }
    }

    Attribute<PointWrapper, Point> getAttribute() {
        return new SimpleAttribute<PointWrapper, Point>() {
            @Override
            public Point getValue(PointWrapper o, QueryOptions queryOptions) {
                return o.point;
            }
        };
    }
}
