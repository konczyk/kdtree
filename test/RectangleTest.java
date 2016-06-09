import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RectangleTest {

    private static final double DELTA = 1e-3;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Parameters({
        "1, 4, 0, 8",
        "5, 2, 7, 1"})
    public void constructWithInvalidCoordinatesThrowsException(
            double xmin, double ymin, double xmax, double ymax) {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("invalid rectangle coordinates");

        new Rectangle(xmin, ymin, xmax, ymax);
    }

    @Test
    public void constructRectangle() {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertThat(r.xmin(), is(1.0));
        assertThat(r.ymin(), is(2.0));
        assertThat(r.xmax(), is(3.0));
        assertThat(r.ymax(), is(4.0));
    }

    @Test
    @Parameters({
        "1.5, 3.0",
        "1.0, 2.0",
        "3.0, 4.0"})
    public void contains(double x, double y) {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertThat(r.contains(new Point(x, y)), is(true));
    }

    @Test
    @Parameters({
        "0.9, 3.0",
        "1.8, 4.1",
        "0.1, 0.1"})
    public void doesNotContain(double x, double y) {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertThat(r.contains(new Point(x, y)), is(false));
    }

    @Test
    @Parameters({
        "1,1,4,4, 3,2,5,4",  // top right
        "1,1,4,4, 0,0,2,2",  // bottom left
        "1,1,4,4, 2,2,3,3",  // inside
        "1,1,4,4, 4,4,5,5",  // top right corner
        "1,1,4,4, 0,0,1,3"}) // left border
    public void intersects(double xmin1, double ymin1, double xmax1, double ymax1,
                           double xmin2, double ymin2, double xmax2, double ymax2) {
        Rectangle r1 = new Rectangle(xmin1, ymin1, xmax1, ymax1);
        Rectangle r2 = new Rectangle(xmin2, ymin2, xmax2, ymax2);

        assertThat(r1.intersects(r2), is(true));
    }

    @Test
    @Parameters({
        "2,2,4,4, 5,2,5,3",
        "2,2,4,4, 0,0,1,1",
        "2,2,4,4, 0,1,5,1",
        "2,2,4,4, 2,5,4,6"})
    public void doesNotIntersect(double xmin1, double ymin1, double xmax1, double ymax1,
                                 double xmin2, double ymin2, double xmax2, double ymax2) {
        Rectangle r1 = new Rectangle(xmin1, ymin1, xmax1, ymax1);
        Rectangle r2 = new Rectangle(xmin2, ymin2, xmax2, ymax2);

        assertThat(r1.intersects(r2), is(false));
    }

    @Test
    @Parameters({
        "4.0, 4.0, 0.0",
        "6.0, 4.0, 4.0",
        "0.0, 0.0, 2.0"})
    public void distanceSquaredTo(double x, double y, double distance) {
        Rectangle r = new Rectangle(1.0, 1.0, 4.0, 4.0);
        Point p = new Point(x, y);

        assertThat(r.distanceSquaredTo(p), is(closeTo(distance, DELTA)));
    }

}
