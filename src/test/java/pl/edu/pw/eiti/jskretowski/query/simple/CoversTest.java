package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static pl.edu.pw.eiti.jskretowski.query.SpatialQueryFactory.covers;

/**
 * Test cases:
 * a. One covered, non-contained polygon in the collection.
 * b. One non-covered polygon in the collection.
 * c. Three polygons, one of which covered, not contained, in the collection.
 * d. Three polygons, none of which covered, in the collection.
 */
public class CoversTest extends QueryTestBase {

    @Test
    public void testCovers() {
        testCaseA();
        testCaseB();
        testCaseC();
        testCaseD();
    }

    private void testCaseA() {
        polygons.add(p2);
        Iterable<Polygon> result = polygons.retrieve(covers(new SelfAttribute<>(Polygon.class), p3));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p2, result.iterator().next());
        polygons.clear();
    }

    private void testCaseB() {
        polygons.add(p1);
        Iterable<Polygon> result = polygons.retrieve(covers(new SelfAttribute<>(Polygon.class), p3));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }

    private void testCaseC() {
        polygons.addAll(Arrays.asList(p1, p2, p4));
        Iterable<Polygon> result = polygons.retrieve(covers(new SelfAttribute<>(Polygon.class), p3));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p2, result.iterator().next());
        polygons.clear();
    }

    private void testCaseD() {
        polygons.addAll(Arrays.asList(p1, p2, p3));
        Iterable<Polygon> result = polygons.retrieve(covers(new SelfAttribute<>(Polygon.class), p4));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
}
