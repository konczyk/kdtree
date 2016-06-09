import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PointSetTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructPointSet() {
        PointSet set = new PointSet();

        assertThat(set.size(), is(0));
        assertThat(set.isEmpty(), is(true));
    }

    @Test
    public void insertNulThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("point is null");

        new PointSet().insert(null);
    }

    @Test
    public void insert() {
        PointSet set = new PointSet();

        set.insert(new Point(0, 0));

        assertThat(set.size(), is(1));
        assertThat(set.isEmpty(), is(false));
    }

    @Test
    public void insertDuplicate() {
        PointSet set = new PointSet();

        set.insert(new Point(0, 0));
        set.insert(new Point(0, 0));

        assertThat(set.size(), is(1));
        assertThat(set.isEmpty(), is(false));
    }

    @Test
    public void containsNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("point is null");

        new PointSet().contains(null);
    }

    @Test
    public void contains() {
        PointSet set = new PointSet();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);

        set.insert(p1);

        assertThat(set.contains(p1), is(true));
        assertThat(set.contains(p2), is(false));
    }

    @Test
    public void rangeWithNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("rectangle is null");

        new PointSet().range(null);
    }

    @Test
    public void rangeWithinEmptySet() {
        PointSet set = new PointSet();
        Rectangle r = new Rectangle(0, 0, 1, 1);

        assertThat(set.range(r).iterator().hasNext(), is(false));
    }

    @Test
    public void rangeWithNoMatchingPoints() {
        PointSet set = new PointSet();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 3);

        set.insert(p1);
        set.insert(p2);

        Rectangle r = new Rectangle(0, 1, 4, 2);
        assertThat(set.range(r).iterator().hasNext(), is(false));
    }

    @Test
    public void range() {
        PointSet set = new PointSet();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 3);
        Point p3 = new Point(3, 5);

        set.insert(p1);
        set.insert(p2);
        set.insert(p3);

        Rectangle r = new Rectangle(2, 3, 3, 5);
        Iterator<Point> range = set.range(r).iterator();
        assertThat(range.next(), is(p2));
        assertThat(range.next(), is(p3));
        assertThat(range.hasNext(), is(false));
    }

    @Test
    public void nearestToNullThrowsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("target point is null");

        new PointSet().nearest(null);
    }

    @Test
    public void nearestWithEmptySet() {
        assertThat(new PointSet().nearest(new Point(0,0)),
                   is(nullValue()));
    }

    @Test
    public void nearestWithEquidistantPoints() {
        PointSet set = new PointSet();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(2, 2);

        set.insert(p1);
        set.insert(p2);

        assertThat(set.nearest(new Point(1, 1)), is(p1));
    }

    @Test
    public void nearest() {
        PointSet set = new PointSet();
        Point p1 = new Point(5, 1);
        Point p2 = new Point(2, 2);

        set.insert(p1);
        set.insert(p2);

        assertThat(set.nearest(new Point(1, 3)), is(p2));
    }


}
