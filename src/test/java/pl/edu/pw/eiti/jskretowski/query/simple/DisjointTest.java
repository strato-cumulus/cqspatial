package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static pl.edu.pw.eiti.jskretowski.query.SpatialQueryFactory.disjoint;

/**
 * Test set consists of:
 * a. one match in a single-object collection
 * b. no matches in a single-object collection
 * c. one match in a multi-object collection
 * d. no matches in multi-object collection
 */
public class DisjointTest extends QueryTestBase {

    @Test
    public void testDisjoint() {
        testCaseA();
        testCaseB();
        testCaseC();
        testCaseD();
    }

    private void testCaseA() {
        polygons.add(p4);
        Iterable<Polygon> result = polygons.retrieve(disjoint(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p4, result.iterator().next());
        polygons.clear();
    }

    private void testCaseB() {
        polygons.add(p3);
        Iterable<Polygon> result = polygons.retrieve(disjoint(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
    private void testCaseC() {
        polygons.addAll(Arrays.asList(p2, p3, p4));
        Iterable<Polygon> result = polygons.retrieve(disjoint(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p4, result.iterator().next());
        polygons.clear();
    }
    private void testCaseD() {
        polygons.addAll(Arrays.asList(p2, p3));
        Iterable<Polygon> result = polygons.retrieve(disjoint(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
}
