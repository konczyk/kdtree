import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;

public class Plane extends JPanel {

    private static final int DIM = 600;
    private static final Random rand = new Random();

    private final Searchable points;
    private final boolean stdin;
    private final int num;
    private final List<Point> selected = new ArrayList<>();

    private BufferedImage pointsCache;

    public Plane(Searchable searchable, int num, boolean stdin) {
        this.points = searchable;
        this.num = num;
        this.stdin = stdin;

        setPreferredSize(new Dimension(DIM, DIM));
        setBackground(Color.WHITE);
    }

    public void setNearestNeighborSearch() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (pointsCache != null) {
                    double x = rescaleX(e.getX());
                    double y = rescaleY(e.getY());
                    addSelected(points.nearest(new Point(x, y)));
                    repaint();
                }
            }
        });
    }

    public void setRangeSearch() {
    }

    private double rescaleX(int x) {
        return x / (DIM - 1.0);
    }

    private double rescaleY(int y) {
        return rescaleX(DIM - y - 1);
    }

    private void addSelected(Point point) {
        selected.clear();
        selected.add(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (pointsCache != null) {
            g.drawImage(pointsCache, 0, 0, null);
            drawSelectedPoints(g2d);
        } else {
            pointsCache = new BufferedImage(getWidth(), getHeight(),
                                            BufferedImage.TYPE_INT_ARGB);
            Graphics ig = pointsCache.getGraphics();
            drawAvailablePoints((Graphics2D) ig);
            paintComponent(g2d);
        }
    }

    private void drawSelectedPoints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.RED);
        for (Point point: selected) {
            drawPoint(g2d, point, 5);
        }
    }

    private void drawAvailablePoints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.BLACK);
        if (stdin) {
            try (Scanner sc = new Scanner(System.in)) {
                while (sc.hasNext()) {
                    Point p = new Point(sc.nextDouble(), sc.nextDouble());
                    points.insert(p);
                    drawPoint(g2d, p, 3);
                }
            }
        } else {
            for (int i = 0; i < num; i++) {
                Point p = new Point(rand.nextDouble(), rand.nextDouble());
                points.insert(p);
                drawPoint(g2d, p, 3);
            }
        }
    }

    private void drawPoint(Graphics2D g2d, Point point, int radius) {
        int x = (int) Math.round(scaleX(point.x()));
        int y = (int) Math.round(scaleY(point.y()));
        g2d.fill(new Ellipse2D.Double(x - radius, y - radius, 2*radius, 2*radius));
    }

    private double scaleX(double x) {
        return x * (DIM - 1);
    }

    private double scaleY(double y) {
        return DIM -  y*(DIM - 1) - 1;
    }

}
