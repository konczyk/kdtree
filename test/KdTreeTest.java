import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class KdTreeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructKdTree() {
        KdTree tree = new KdTree();

        assertThat(tree.size(), is(0));
        assertThat(tree.isEmpty(), is(true));
    }

    @Test
    public void insertNulThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("point is null");

        new KdTree().insert(null);
    }

    @Test
    public void insert() {
        KdTree tree = new KdTree();

        tree.insert(new Point(0, 0));

        assertThat(tree.size(), is(1));
        assertThat(tree.isEmpty(), is(false));
    }

    @Test
    public void insertDuplicate() {
        KdTree tree = new KdTree();

        tree.insert(new Point(0, 0));
        tree.insert(new Point(0, 0));

        assertThat(tree.size(), is(1));
    }

    @Test
    public void containsNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("point is null");

        new KdTree().contains(null);
    }

    @Test
    public void containsRoot() {
        KdTree tree = new KdTree();
        Point p = new Point(0.1, 0.1);

        tree.insert(p);

        assertThat(tree.contains(p), is(true));
    }

    @Test
    public void containsInLeftSubtree() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.1, 0.1);
        Point p2 = new Point(0.05, 0.1);

        tree.insert(p1);
        tree.insert(p2);

        assertThat(tree.contains(p2), is(true));
    }

    @Test
    public void containsInBottomSubtree() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.1, 0.1);
        Point p2 = new Point(0.05, 0.1);
        Point p3 = new Point(0.03, 0.1);

        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);

        assertThat(tree.contains(p3), is(true));
    }

    @Test
    public void containsInRightSubtree() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.1, 0.1);
        Point p2 = new Point(0.1, 0.2);

        tree.insert(p1);
        tree.insert(p2);

        assertThat(tree.contains(p2), is(true));
    }

    @Test
    public void containsInTopSubtree() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.1, 0.1);
        Point p2 = new Point(0.1, 0.2);
        Point p3 = new Point(0.2, 0.3);

        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);

        assertThat(tree.contains(p3), is(true));
    }

    @Test
    public void doesNotContain() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.1, 0.1);
        Point p2 = new Point(0.05, 0.9);
        Point p3 = new Point(0.1, 0.2);

        tree.insert(p1);
        tree.insert(p2);

        assertThat(tree.contains(p3), is(false));
    }

    @Test
    public void rangeWithNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("rectangle is null");

        new KdTree().range(null);
    }

    @Test
    public void rangeWithinEmptySet() {
        KdTree tree = new KdTree();
        Rectangle r = new Rectangle(0.0, 0.0, 1.0, 1.0);

        assertThat(tree.range(r).iterator().hasNext(), is(false));
    }

    @Test
    public void rangeWithNoMatchingPoints() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(0.3, 0.3);

        tree.insert(p1);
        tree.insert(p2);

        Rectangle r = new Rectangle(0.0, 0.1, 0.4, 0.2);
        assertThat(tree.range(r).iterator().hasNext(), is(false));
    }

    @Test
    public void range() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(0.3, 0.3);
        Point p3 = new Point(0.3, 0.5);

        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);

        Rectangle r = new Rectangle(0.2, 0.3, 0.3, 0.5);
        Iterator<Point> range = tree.range(r).iterator();
        assertThat(range.next(), is(p2));
        assertThat(range.next(), is(p3));
        assertThat(range.hasNext(), is(false));
    }

    @Test
    public void nearestToNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("target point is null");

        new KdTree().nearest(null);
    }

    @Test
    public void nearestWithEmptySet() {
        assertThat(new KdTree().nearest(new Point(0,0)),
                   is(nullValue()));
    }

    @Test
    public void nearestWithEquidistantPoints() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(0.2, 0.2);

        tree.insert(p1);
        tree.insert(p2);

        assertThat(tree.nearest(new Point(0.1, 0.1)), is(p2));
    }

    @Test
    public void nearest() {
        KdTree tree = new KdTree();
        Point p1 = new Point(0.5, 1.0);
        Point p2 = new Point(0.2, 0.2);

        tree.insert(p1);
        tree.insert(p2);

        assertThat(tree.nearest(new Point(0.1, 0.3)), is(p2));
    }

}
