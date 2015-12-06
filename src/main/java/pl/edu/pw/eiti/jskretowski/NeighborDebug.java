package pl.edu.pw.eiti.jskretowski;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import pl.edu.pw.eiti.jskretowski.engine.ConcurrentSpatialIndexedCollection;
import pl.edu.pw.eiti.jskretowski.query.distance.Neighbor;
import pl.edu.pw.eiti.jskretowski.query.option.Top;
import pl.edu.pw.eiti.jskretowski.query.simple.EqualTo;
import pl.edu.pw.eiti.jskretowski.resultset.StatefulResultSet;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.queryOptions;

/**
 * Created by strato on 29.11.2015.
 */
public class NeighborDebug {
    GeometryFactory factory = new GeometryFactory();

    public static void main(String[] args) {
        NeighborDebug neighborDebug = new NeighborDebug();
        neighborDebug.smallTestNeighbor();
        neighborDebug.largeTestNeighbor();
    }

    public void smallTestNeighbor() {
        Point testPoint = factory.createPoint(new Coordinate(4., 4.));
        PointWrapper[] closerPoints = {
                new PointWrapper(factory.createPoint(new Coordinate(3., 4.))),
                new PointWrapper(factory.createPoint(new Coordinate(4., 3.))),
                new PointWrapper(factory.createPoint(new Coordinate(5., 4.))),
                new PointWrapper(factory.createPoint(new Coordinate(4., 5.)))
        };
        ConcurrentSpatialIndexedCollection<PointWrapper, Double, Point> collection
                = new ConcurrentSpatialIndexedCollection<>();
        collection.addAll(Arrays.asList(closerPoints));
        StatefulResultSet rs = collection.retrieve(new Neighbor<>(getAttribute(), testPoint), queryOptions(new Top(2)));
        Assert.assertEquals(2, rs.size());
    }

    public void largeTestNeighbor() {
        ConcurrentSpatialIndexedCollection<PointWrapper, Double, Point> collection
                = new ConcurrentSpatialIndexedCollection<>();
        Point testPoint = factory.createPoint(new Coordinate(0, 0));
        for(int i = 0; i < 100000; ++i) {
            collection.add(new PointWrapper(factory.createPoint(new Coordinate(0, i))));
        }
        StatefulResultSet rs = collection.retrieve(new Neighbor<>(getAttribute(), testPoint), queryOptions(new Top(10000)));
        ResultSet pointRs = collection.retrieve(new EqualTo<>(getAttribute(), factory.createPoint(new Coordinate(0, 9999))));
        Assert.assertEquals(10000, rs.size());
        Assert.assertTrue(rs.contains(pointRs.uniqueResult()));
    }

    static class PointWrapper {
        final Point point;
        PointWrapper(Point point) {
            this.point = point;
        }
    }

    static Attribute<PointWrapper, Point> getAttribute() {
        return new SimpleAttribute<PointWrapper, Point>() {
            @Override
            public Point getValue(PointWrapper o, QueryOptions queryOptions) {
                return o.point;
            }
        };
    }
}
