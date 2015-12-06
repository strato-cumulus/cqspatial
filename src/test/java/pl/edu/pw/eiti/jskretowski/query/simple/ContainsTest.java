package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.vividsolutions.jts.geom.Polygon;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

import static pl.edu.pw.eiti.jskretowski.query.SpatialQueryFactory.contains;

/**
 * Test the following cases:
 * a. Two polygons, one contained within the other
 * b. Two polygons, neither of them contained within each other
 * c. Four polygons, one of which is contained within the tested one.
 * d. Four polygons, none of which is contained within each other.
 */
public class ContainsTest extends QueryTestBase {



    @Test
    public void testContains() {
        testCaseA();
        testCaseB();
        testCaseC();
        testCaseD();
    }

    private void testCaseA() {
        polygons.add(p2);
        Iterable<Polygon> result = polygons.retrieve(contains(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        Assert.assertEquals(p2, result.iterator().next());
        polygons.clear();
    }

    private void testCaseB() {
        polygons.add(p3);
        Iterable<Polygon> result = polygons.retrieve(contains(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }

    private void testCaseC() {
        polygons.addAll(Arrays.asList(p2, p3, p4));
        Iterable<Polygon> result = polygons.retrieve(contains(new SelfAttribute<>(Polygon.class), p1));
        Assert.assertEquals(1, Iterables.count(result));
        polygons.clear();
    }

    private void testCaseD() {
        polygons.addAll(Arrays.asList(p1, p2, p3));
        Iterable<Polygon> result = polygons.retrieve(contains(new SelfAttribute<>(Polygon.class), p4));
        Assert.assertEquals(0, Iterables.count(result));
        polygons.clear();
    }
}
