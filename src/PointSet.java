import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *  A set of points in the unit square
 */
public class PointSet implements Searchable {

    private final TreeSet<Point> points;

    public PointSet() {
        points = new TreeSet<>();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return points.size();
    }

    @Override
    public void insert(Point point) {
        if (point == null) {
            throw new NullPointerException("point is null");
        }
        points.add(point);
    }

    @Override
    public boolean contains(Point queryPoint) {
        if (queryPoint == null) {
            throw new NullPointerException("query point is null");
        }
        return points.contains(queryPoint);
    }

    @Override
    public Iterable<Point> range(Rectangle queryRectangle) {
        if (queryRectangle == null) {
            throw new NullPointerException("query rectangle is null");
        }

        List<Point> pointsInRange = new ArrayList<>();
        for (Point point: points) {
            if (queryRectangle.contains(point)) {
                pointsInRange.add(point);
            }
        }

        return pointsInRange;
    }

    @Override
    public Point nearest(Point queryPoint) {
        if (queryPoint == null) {
            throw new NullPointerException("query point is null");
        }

        Point nearest = null;
        double distance = 0;
        for (Point point: points) {
            if (point.equals(queryPoint)) {
                return point;
            } else if (nearest == null
                    || queryPoint.distanceTo(point) < distance) {
                nearest = point;
                distance = queryPoint.distanceTo(point);
            }
        }

        return nearest;
    }

}
