import java.util.ArrayList;
import java.util.List;

/**
 * A set of points in the unit square using a 2d-tree implementation
 */
public class KdTree {

    private enum Orientation {
        HORIZONTAL, VERTICAL;
        public Orientation swap() {
            if (this.equals(HORIZONTAL)) {
                return VERTICAL;
            } else {
                return HORIZONTAL;
            }
        }

        public boolean isVertical() {
            return this.equals(VERTICAL);
        }

        public boolean isHorizontal() {
            return this.equals(HORIZONTAL);
        }
    }

    private Node root;
    private int size = 0;

    private static class Node {
        private final Point point;
        private final Rectangle rectangle;
        private Node leftBottomLink;
        private Node rightTopLink;

        private Node(Point point, Rectangle rectangle) {
            this.point = point;
            this.rectangle = rectangle;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point point) {
        if (point == null) {
            throw new NullPointerException("point is null");
        }

        root = insert(null, root, point, Orientation.VERTICAL);
    }

    private Node insert(Node parent, Node link, Point point,
                        Orientation orientation) {
        if (link == null) {
            size++;
            Rectangle rectangle = createRectangle(parent, point,
                                                  orientation.swap());
            return new Node(point, rectangle);
        } else {
            int cmp = comparePoints(point, link.point, orientation);
            if (cmp < 0) {
                link.leftBottomLink = insert(link, link.leftBottomLink, point,
                                             orientation.swap());
            } else if (cmp > 0 || !point.equals(link.point)) {
                link.rightTopLink = insert(link, link.rightTopLink, point,
                                           orientation.swap());
            }
        }

        return link;
    }

    private Rectangle createRectangle(Node parent, Point point,
                                      Orientation orientation) {
        Rectangle newRectangle;
        if (parent == null) {
            newRectangle = new Rectangle(0, 0, 1, 1);
        } else {
            int cmp = comparePoints(point, parent.point, orientation);
            double xmin = parent.rectangle.xmin();
            double ymin = parent.rectangle.ymin();
            double xmax = parent.rectangle.xmax();
            double ymax = parent.rectangle.ymax();
            if (orientation.isVertical() && cmp < 0) {
                xmax = parent.point.x();
            } else if (orientation.isVertical()) {
                xmin = parent.point.x();
            } else if (orientation.isHorizontal() && cmp < 0) {
                ymax = parent.point.y();
            } else {
                ymin = parent.point.y();
            }
            newRectangle = new Rectangle(xmin, ymin, xmax, ymax);
        }

        return newRectangle;
    }

    public boolean contains(Point queryPoint) {
        if (queryPoint == null) {
            throw new NullPointerException("query point is null");
        }
        return !isEmpty() && contains(root, queryPoint, Orientation.VERTICAL);
    }

    private boolean contains(Node node, Point queryPoint, Orientation orientation) {
        if (node == null) {
            return false;
        } else if (queryPoint.equals(node.point)) {
            return true;
        } else {
            if (comparePoints(queryPoint, node.point, orientation) < 0) {
                return contains(node.leftBottomLink, queryPoint, orientation.swap());
            } else {
                return contains(node.rightTopLink, queryPoint, orientation.swap());
            }
        }
    }

    private int comparePoints(Point point, Point other,
                              Orientation orientation) {

        if (orientation.isVertical()) {
            return Point.X_ORDER.compare(point, other);
        } else {
            return Point.Y_ORDER.compare(point, other);
        }
    }

    public Iterable<Point> range(Rectangle rectangle) {
        if (rectangle == null) {
            throw new NullPointerException("rectangle is null");
        }
        List<Point> points = new ArrayList<>();
        range(points, root, rectangle);

        return points;
    }

    private void range(List<Point> points, Node node, Rectangle rectangle) {
        if (node != null && node.rectangle.intersects(rectangle)) {
            if (rectangle.contains(node.point)) {
                points.add(node.point);
            }
            range(points, node.leftBottomLink, rectangle);
            range(points, node.rightTopLink, rectangle);
        }
    }

    public Point nearest(Point queryPoint) {
        if (queryPoint == null) {
            throw new NullPointerException("query point is null");
        }

        if (root == null) {
            return null;
        }

        return nearest(root.point, root, queryPoint, Orientation.VERTICAL);
    }

    private Point nearest(Point nearestPoint, Node node, Point queryPoint,
                          Orientation orientation) {

        if (node == null
                || !isRectangleCloser(queryPoint, nearestPoint, node.rectangle)) {
            return nearestPoint;
        }

        nearestPoint = closer(queryPoint, nearestPoint, node.point);

        Node primaryLink;
        Node secondaryLink;
        if (comparePoints(queryPoint, node.point, orientation) < 0) {
            primaryLink = node.leftBottomLink;
            secondaryLink = node.rightTopLink;
        } else {
            primaryLink = node.rightTopLink;
            secondaryLink = node.leftBottomLink;
        }
        nearestPoint = nearest(nearestPoint, primaryLink, queryPoint, orientation.swap());
        nearestPoint = nearest(nearestPoint, secondaryLink, queryPoint, orientation.swap());

        return nearestPoint;
    }

    private boolean isRectangleCloser(Point queryPoint, Point nearestPoint,
                                      Rectangle nodeRectangle) {

        double nearestDistance = nearestPoint.distanceSquaredTo(queryPoint);
        double rectangleDistance = nodeRectangle.distanceSquaredTo(queryPoint);

        return rectangleDistance < nearestDistance;
    }

    private Point closer(Point queryPoint, Point testPoint1,
                         Point testPoint2) {

        double distance1 = testPoint1.distanceSquaredTo(queryPoint);
        double distance2 = testPoint2.distanceSquaredTo(queryPoint);
        if (distance1 < distance2) {
            return testPoint1;
        } else {
            return testPoint2;
        }
    }

}
