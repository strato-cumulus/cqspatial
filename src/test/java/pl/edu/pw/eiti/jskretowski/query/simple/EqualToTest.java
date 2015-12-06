package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static pl.edu.pw.eiti.jskretowski.query.SpatialQueryFactory.equalTo;

/**
 * Created by strato on 03.11.2015.
 */
public class EqualToTest extends QueryTestBase {
    private Polygon p5 = factory.createPolygon(p1.getCoordinates());

    @Test
    public void testEqualTo() {
        testCaseA();
        testCaseB();
        testCaseC();
        testCaseD();
    }

    private void testCaseA() {
        polygons.add(p5);
        Iterable<Polygon> result = polygons.retrieve(equalTo(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p5, result.iterator().next());
        polygons.clear();
    }

    private void testCaseB() {
        polygons.add(p3);
        Iterable<Polygon> result = polygons.retrieve(equalTo(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
    private void testCaseC() {
        polygons.addAll(Arrays.asList(p2, p3, p4, p5));
        Iterable<Polygon> result = polygons.retrieve(equalTo(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p5, result.iterator().next());
        polygons.clear();
    }
    private void testCaseD() {
        polygons.addAll(Arrays.asList(p2, p3, p4));
        Iterable<Polygon> result = polygons.retrieve(equalTo(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
}
