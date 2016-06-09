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
public class PointTest {

    private static final double DELTA = 1e-3;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructPoint() {
        Point p = new Point(10, 2);

        assertThat(p.x(), is(10.0));
        assertThat(p.y(), is(2.0));
    }

    @Test
    @Parameters({
        " 1.0, 2.0,  3.0, 1.0,  1",
        " 1.0, 2.0,  3.0, 3.0, -1",
        "-3.0, 2.0, -4.0, 2.0,  1",
        "-3.0, 2.0, -1.0, 2.0, -1",
        " 0.0, 3.0, -0.0, 3.0,  0"})
    public void compareTo(double x1, double y1,
                          double x2, double y2, int result) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        assertThat(p1.compareTo(p2), is(result));
    }

    @Test
    @Parameters({
        "1.0,  2.0,  3.0,  1.0,  2.236",
        "5.0, -2.0, -8.0, -2.3, 13.003"})
    public void distanceTo(double x1, double y1,
                           double x2, double y2, double result) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        assertThat(p1.distanceTo(p2), is(closeTo(result, DELTA)));
    }

    @Test
    @Parameters({
        "1.0,  2.0,  3.0,  1.0,   5.0",
        "5.0, -2.0, -8.0, -2.3, 169.09"})
    public void distanceSquaredTo(double x1, double y1,
                                  double x2, double y2, double result) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        assertThat(p1.distanceSquaredTo(p2), is(closeTo(result, DELTA)));
    }

    @Test
    @Parameters({
        " 1.0, 2.0,  3.0, 1.0, -1",
        "-3.0, 2.0, -4.0, 2.0,  1",
        " 0.0, 3.0, -0.0, 3.0,  0"})
    public void compareByX(double x1, double y1,
                           double x2, double y2, int result) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        assertThat(Point.X_ORDER.compare(p1, p2), is(result));
    }

    @Test
    @Parameters({
        " 1.0, 2.0,  3.0,  1.0,  1",
        "-3.0, 1.0, -4.0,  2.0, -1",
        " 3.0, 0.0,  3.0, -0.0,  0"})
    public void compareByY(double x1, double y1,
                           double x2, double y2, int result) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        assertThat(Point.Y_ORDER.compare(p1, p2), is(result));
    }

}
