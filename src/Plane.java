import java.awt.*;
import java.awt.event.MouseAdapter;
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
    private final List<Point> selectedPoints = new ArrayList<>();

    private BufferedImage pointsCache;
    private java.awt.Point rangeStart;
    private java.awt.Point rangeEnd;

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
                    selectedPoints.clear();
                    selectedPoints.add(points.nearest(new Point(x, y)));
                    repaint();
                }
            }
        });
    }

    public void setRangeSearch() {
        MouseAdapter adapter = new MouseRangeAdapter();
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private class MouseRangeAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            rangeStart = e.getPoint();
            rangeEnd = null;
            selectedPoints.clear();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            rangeEnd = e.getPoint();
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            rangeEnd = e.getPoint();
            selectedPoints.clear();
            for (Point point: points.range(createRange())) {
                selectedPoints.add(point);
            }
            repaint();
        }

        private Rectangle createRange() {
            int xmin, ymin, xmax, ymax;
            if (rangeStart.x <= rangeEnd.x) {
                xmin = rangeStart.x;
                xmax = rangeEnd.x;
            } else {
                xmin = rangeEnd.x;
                xmax = rangeStart.x;
            }

            if (rangeStart.y >= rangeEnd.y) {
                ymin = rangeStart.y;
                ymax = rangeEnd.y;
            } else {
                ymin = rangeEnd.y;
                ymax = rangeStart.y;
            }

            return new Rectangle(rescaleX(xmin), rescaleY(ymin),
                                 rescaleX(xmax), rescaleY(ymax));
        }
    }

    private double rescaleX(int x) {
        return x / (DIM - 1.0);
    }

    private double rescaleY(int y) {
        return rescaleX(DIM - y - 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (pointsCache != null) {
            g.drawImage(pointsCache, 0, 0, null);
            drawSelectedPoints(g2d);
            drawRange(g2d);
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
        for (Point point: selectedPoints) {
            drawPoint(g2d, point);
        }
    }

    private void drawRange(Graphics2D g2d) {
        if (rangeStart == null) {
            return;
        }
        g2d.setPaint(Color.BLUE);
        int xmin;
        int ymin;
        int width = 1;
        int height = 1;
        if (rangeEnd == null) {
            xmin = rangeStart.x;
            ymin = rangeStart.y;
        } else {
            xmin = Math.min(rangeStart.x, rangeEnd.x);
            ymin = Math.min(rangeStart.y, rangeEnd.y);
            width = Math.abs(rangeStart.x - rangeEnd.x);
            height = Math.abs(rangeStart.y - rangeEnd.y);
        }
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(xmin, ymin, width, height);
        g2d.setStroke(oldStroke);
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

    private void drawPoint(Graphics2D g2d, Point point) {
        int x = (int) Math.round(scaleX(point.x()));
        int y = (int) Math.round(scaleY(point.y()));
        int r = 3;
        g2d.fill(new Ellipse2D.Double(x - r, y - r, 2*r, 2*r));
    }

    private double scaleX(double x) {
        return x * (DIM - 1);
    }

    private double scaleY(double y) {
        return DIM -  y*(DIM - 1) - 1;
    }

}
