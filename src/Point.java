/**
 * A point in the plane
 */

public final class Point implements Comparable<Point> {

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
        double dx = other.x - x;
        double dy = other.y - y;

        return Math.sqrt(dx*dx + dy*dy);
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

}
