package pl.edu.pw.eiti.jskretowski.query.simple;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.vividsolutions.jts.geom.*;

/**
 * Test set description:
 * - p1 contains and covers p2, intersects p3
 * - p2 is contained by and covered by p1, covered by and intersecting p3
 * - p3 covers and intersects p2, intersects p1
 * - p4 is an outlier.
 */
public abstract class QueryTestBase {
    protected IndexedCollection<Polygon> polygons = new ConcurrentIndexedCollection<>();
    protected GeometryFactory factory = new GeometryFactory();

    Polygon p1 = createPolygonFromPoints(
            0d, 1d,
            1d, 3d,
            4d, 2d,
            1d, 0d
    );
    Polygon p2 = createPolygonFromPoints(
            1d, 1d,
            1d, 2d,
            2d, 2d
    );
    Polygon p3 = createPolygonFromPoints(
            0d, 3d,
            4d, 1d,
            1d, -1d
    );
    Polygon p4 = createPolygonFromPoints(
            3d, 3d,
            3d, 4d,
            4d, 3d
    );

    private Polygon createPolygonFromPoints(double... points) {
        if(points.length % 2 != 0) {
            throw new IllegalArgumentException("An uneven number of coordinates does not define a shape");
        }
        // An array for all pairs of coordinates, plus the first one repeated
        Coordinate[] polygonCoordinates = new Coordinate[points.length/2 + 1];
        for (int i = 0; i < points.length / 2; i++) {
            polygonCoordinates[i] = new Coordinate(
                points[2*i], points[2*i + 1]
            );
        }
        polygonCoordinates[polygonCoordinates.length - 1] = new Coordinate(
                points[0], points[1]
        );
        return new Polygon(factory.createLinearRing(polygonCoordinates), new LinearRing[]{}, factory);
    }
}
