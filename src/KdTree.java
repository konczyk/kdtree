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
            Rectangle rect = createRectangle(parent, point, orientation.swap());
            return new Node(point, rect);
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
        Rectangle newRect;
        if (parent == null) {
            newRect = new Rectangle(0, 0, 1, 1);
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
            newRect = new Rectangle(xmin, ymin, xmax, ymax);
        }

        return newRect;
    }

    public boolean contains(Point point) {
        if (point == null) {
            throw new NullPointerException("point is null");
        }
        return !isEmpty() && contains(root, point, Orientation.VERTICAL);
    }

    private boolean contains(Node node, Point point, Orientation orientation) {
        if (node == null) {
            return false;
        } else if (point.equals(node.point)) {
            return true;
        } else {
            if (comparePoints(point, node.point, orientation) < 0) {
                return contains(node.leftBottomLink, point, orientation.swap());
            } else {
                return contains(node.rightTopLink, point, orientation.swap());
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

    private void range(List<Point> points, Node node, Rectangle rect) {
        if (node != null && node.rectangle.intersects(rect)) {
            if (rect.contains(node.point)) {
                points.add(node.point);
            }
            range(points, node.leftBottomLink, rect);
            range(points, node.rightTopLink, rect);
        }
    }

    public Point nearest(Point targetPoint) {
        if (targetPoint == null) {
            throw new NullPointerException("target point is null");
        }

        if (root == null) {
            return null;
        }

        return nearest(root.point, root, targetPoint, Orientation.VERTICAL);
    }

    private Point nearest(Point current, Node node, Point query,
                          Orientation orientation) {

        if (node == null || !isRectCloser(query, current, node.rectangle)) {
            return current;
        }

        current = closer(query, current, node.point);

        Node primaryLink;
        Node secondaryLink;
        if (comparePoints(query, node.point, orientation) < 0) {
            primaryLink = node.leftBottomLink;
            secondaryLink = node.rightTopLink;
        } else {
            primaryLink = node.rightTopLink;
            secondaryLink = node.leftBottomLink;
        }
        current = nearest(current, primaryLink, query, orientation.swap());
        current = nearest(current, secondaryLink, query, orientation.swap());

        return current;
    }

    private boolean isRectCloser(Point queryPoint, Point nearestPoint,
                                 Rectangle nodeRect) {

        double nearestDistance = nearestPoint.distanceSquaredTo(queryPoint);
        double rectDistance = nodeRect.distanceSquaredTo(queryPoint);

        return rectDistance < nearestDistance;
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
