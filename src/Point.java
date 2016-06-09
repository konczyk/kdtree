import java.util.Comparator;
import java.util.Objects;

/**
 * A point in the plane
 */

public final class Point implements Comparable<Point> {

    public static final Comparator<Point> X_ORDER = new XOrder();
    public static final Comparator<Point> Y_ORDER = new YOrder();

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x + 0.0;
        this.y = y + 0.0;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double distanceTo(Point other) {
        return Math.sqrt(distanceSquaredTo(other));
    }

    public double distanceSquaredTo(Point other) {
        double dx = other.x - x;
        double dy = other.y - y;

        return dx*dx + dy*dy;
    }

    @Override
    public int compareTo(Point other) {
        int compareY = Double.compare(y, other.y);
        if (compareY != 0) {
            return compareY;
        } else {
            return Double.compare(x, other.x);
        }
    }

    private static class XOrder implements Comparator<Point> {
        @Override
        public int compare(Point p, Point q) {
            return Double.compare(p.x, q.x);
        }
    }

    private static class YOrder implements Comparator<Point> {
        @Override
        public int compare(Point p, Point q) {
            return Double.compare(p.y, q.y);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0
            && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
