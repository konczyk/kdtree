/**
 * Searchable set of points in the unit square
 */
public interface Searchable {
    boolean isEmpty();
    int size();
    void insert(Point point);
    boolean contains(Point queryPoint);
    Iterable<Point> range(Rectangle queryRectangle);
    Point nearest(Point queryPoint);
}
