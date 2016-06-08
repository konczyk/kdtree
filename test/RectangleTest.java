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

}
