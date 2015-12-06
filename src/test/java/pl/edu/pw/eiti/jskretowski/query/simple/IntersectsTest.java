package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static pl.edu.pw.eiti.jskretowski.query.SpatialQueryFactory.intersects;

/**
 * Created by strato on 03.11.2015.
 */
public class IntersectsTest extends QueryTestBase {

    @Test
    public void testEqualTo() {
        testCaseA();
        testCaseB();
        testCaseC();
        testCaseD();
    }

    private void testCaseA() {
        polygons.add(p3);
        Iterable<Polygon> result = polygons.retrieve(intersects(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p3, result.iterator().next());
        polygons.clear();
    }

    private void testCaseB() {
        polygons.add(p4);
        Iterable<Polygon> result = polygons.retrieve(intersects(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
    private void testCaseC() {
        polygons.addAll(Arrays.asList(p2, p3, p4));
        Iterable<Polygon> result = polygons.retrieve(intersects(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(2, Iterables.count(result));
        Assert.assertEquals(p2, result.iterator().next());
        polygons.clear();
    }
    private void testCaseD() {
        polygons.add(p4);
        Iterable<Polygon> result = polygons.retrieve(intersects(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
}
