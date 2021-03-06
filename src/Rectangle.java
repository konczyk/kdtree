/**
 * A 2D axis-aligned rectangle
 */
public final class Rectangle {

    private final double xmin;
    private final double ymin;
    private final double xmax;
    private final double ymax;

    public Rectangle(double xmin, double ymin, double xmax, double ymax) {
        if (Double.compare(xmax, xmin) < 0 || Double.compare(ymax, ymin) < 0) {
            throw new IllegalArgumentException("invalid rectangle coordinates");
        }
        this.xmin = xmin + 0.0;
        this.ymin = ymin + 0.0;
        this.xmax = xmax + 0.0;
        this.ymax = ymax + 0.0;
    }

    public double xmin() {
        return xmin;
    }

    public double ymin() {
        return ymin;
    }

    public double xmax() {
        return xmax;
    }

    public double ymax() {
        return ymax;
    }

    public boolean contains(Point point) {
        return point.x() >= xmin && point.x() <= xmax
            && point.y() >= ymin && point.y() <= ymax;
    }

    public boolean intersects(Rectangle other) {
        return xmax >= other.xmin && ymax >= other.ymin
            && other.xmax >= xmin && other.ymax >= ymin;
    }

    public double distanceSquaredTo(Point point) {
        double dx = 0.0;
        double dy = 0.0;
        if (point.x() < xmin) {
            dx = xmin - point.x();
        } else if (point.x() > xmax) {
            dx = point.x() - xmax;
        }
        if (point.y() < ymin) {
            dy = ymin - point.y();
        } else if (point.y() > ymax) {
            dy = point.y() - ymax;
        }

        return dx*dx + dy*dy;
    }

}
