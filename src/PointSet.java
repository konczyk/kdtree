import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *  A set of points in the unit square
 */
public class PointSet {

    private final TreeSet<Point> points;

    public PointSet() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point point) {
        if (point == null) {
            throw new NullPointerException("point is null");
        }
        points.add(point);
    }

    public boolean contains(Point point) {
        return points.contains(point);
    }

    public Iterable<Point> range(Rectangle rectangle) {
        if (rectangle == null) {
            throw new NullPointerException("rectangle is null");
        }

        List<Point> pointsInRange = new ArrayList<>();
        for (Point point: points) {
            if (rectangle.contains(point)) {
                pointsInRange.add(point);
            }
        }

        return pointsInRange;
    }

    public Point nearest(Point targetPoint) {
        if (targetPoint == null) {
            throw new NullPointerException("target point is null");
        }

        Point nearest = null;
        double distance = 0;
        for (Point point: points) {
            if (point.equals(targetPoint)) {
                return point;
            } else if (nearest == null
                        || targetPoint.distanceTo(point) < distance) {
                nearest = point;
                distance = targetPoint.distanceTo(point);
            }
        }

        return nearest;
    }

}
