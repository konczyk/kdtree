import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;

public class Plane extends JPanel {

    private static final int DIM = 600;

    private static final Random rand = new Random();
    private final Searchable points;
    private final boolean stdin;
    private final int num;
    private BufferedImage pointsCache;

    public Plane(Searchable points, int num, boolean stdin) {
        this.points = points;
        this.num = num;
        this.stdin = stdin;

        setPreferredSize(new Dimension(DIM, DIM));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (pointsCache != null) {
            g.drawImage(pointsCache, 0, 0, null);
        } else {
            pointsCache = new BufferedImage(getWidth(), getHeight(),
                                            BufferedImage.TYPE_INT_ARGB);
            Graphics ig = pointsCache.getGraphics();
            drawPoints(ig);
            paintComponent(g);
        }
    }

    private void drawPoints(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.BLACK);
        if (stdin) {
            try (Scanner sc = new Scanner(System.in)) {
                while (sc.hasNext()) {
                    Point p = new Point(sc.nextDouble(), sc.nextDouble());
                    points.insert(p);
                    drawPoint(g2d, p);
                }
            }
        } else {
            for (int i = 0; i < num; i++) {
                Point p = new Point(rand.nextDouble(), rand.nextDouble());
                points.insert(p);
                drawPoint(g2d, p);
            }
        }
    }

    private void drawPoint(Graphics2D g2d, Point p) {
        double x = scale(p.x());
        double y = DIM - scale(p.y()) - 1;
        g2d.fillRect((int) Math.round(x), (int) Math.round(y), 1, 1);
    }

    private double scale(double p) {
        return p * (DIM - 1);
    }

}
