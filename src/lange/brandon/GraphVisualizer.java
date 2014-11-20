package lange.brandon;

import javax.imageio.ImageIO;

import coffeeSolver.Graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class GraphVisualizer {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final int BORDER = 20;
    private static final int USABLE_WIDTH = WIDTH - BORDER * 2;
    private static final int USABLE_HEIGHT = HEIGHT - BORDER * 2;
    private static final int INTERSECTION_RADIUS = 8;
    private static final int FONT_SIZE = 10;

    private static final Color BACKGROUND = Color.WHITE;
    private static final Color FOREGROUND = Color.BLACK;
    private static final Color TEXT = Color.LIGHT_GRAY;

    private static final double PHI = Math.toRadians(40);
    private static final int BARB = 6;

    private static double horizontalDistance;
    private static double verticalDistance;
    private static double minX;
    private static double minY;

    public static <V extends Locatable,E> void visualize(Graph<V,E> map, String type, File file) throws IOException {
        calculateBounds(map);

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font(Font.SERIF, Font.PLAIN, FONT_SIZE));

        g.setColor(BACKGROUND);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        for (Integer srcID : map.getVertices()) {
            Locatable source = map.getData(srcID);
            Point sourceLoc = pointFromLocation(source.getLatitude(), source.getLongitude());
            drawVertex(g, sourceLoc, srcID.toString());

            for (Integer edgeID : map.getEdgesOf(srcID)) {
                int targetID = map.getTarget(edgeID);
                Locatable target = map.getData(targetID);
                Point targetLoc = pointFromLocation(target.getLatitude(), target.getLongitude());
                drawEdge(g, sourceLoc, targetLoc);
            }
        }

        ImageIO.write(img, type, file);
    }

    private static <V extends Locatable,E> void calculateBounds(Graph<V,E> map) {
        int first = map.getVertices().iterator().next();
        double maxX = map.getData(first).getLatitude();
        double maxY = map.getData(first).getLongitude();

        minX = maxX;
        minY = maxY;

        for (Integer id : map.getVertices()) {
            Locatable data = map.getData(id);
            double x = data.getLatitude();
            if (x > maxX) {
                maxX = x;
            }
            else if (x < minX) {
                minX = x;
            }

            double y = data.getLongitude();
            if (y > maxY) {
                maxY = y;
            }
            else if (y < minY) {
                minY = y;
            }
        }

        horizontalDistance = maxX - minX;
        verticalDistance = maxY - minY;
    }

    private static void drawVertex(Graphics2D g, Point p, String label) {
        g.setColor(FOREGROUND);
        drawCircle(g, p);

        g.setColor(TEXT);
        g.drawString(label, p.x, p.y - INTERSECTION_RADIUS - (FONT_SIZE / 4));
    }

    public static void drawEdge(Graphics2D g, Point tail, Point tip) {
        double angle = getAngle(tip, tail);
        Point head = translatePoint(tip, angle, INTERSECTION_RADIUS / 2);

        g.setColor(FOREGROUND);
        drawArrow(g, tail, head);
    }

    private static Point pointFromLocation(double x, double y) {
        int pointX = (int) ((x - minX) / horizontalDistance * USABLE_WIDTH) + BORDER;
        int pointY = (int) ((y - minY) / verticalDistance * USABLE_HEIGHT) + BORDER;
        return new Point(pointX, pointY);
    }

    private static void drawCircle(Graphics2D g, Point p) {
        int x = p.x - INTERSECTION_RADIUS / 2;
        int y = p.y - INTERSECTION_RADIUS / 2;
        g.fillOval(x, y, INTERSECTION_RADIUS, INTERSECTION_RADIUS);
    }

    /*
     * Shamelessly modified from Aich's code at
     * http://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
     */
    private static double getAngle(Point source, Point target) {
        return Math.atan2(target.x - source.x, target.y - source.y) - Math.PI / 2;
    }

    private static Point translatePoint(Point p, double angle, int dist) {
        int x = p.x + (int) (dist * Math.cos(angle));
        int y = p.y + (int) (dist * Math.sin(angle));
        return new Point(x, y);
    }

    /*
     * Shamelessly modified from Craig Wood's code at
     * http://www.coderanch.com/t/340443/GUI/java/Draw-arrow-head-line
     */
    private static void drawArrow(Graphics2D g, Point tail, Point tip) {
        g.drawLine(tail.x, tail.y, tip.x, tip.y);
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + PHI;
        for (int i = 0; i < 2; i++) {
            x = tip.x - BARB * Math.cos(rho);
            y = tip.y - BARB * Math.sin(rho);
            g.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - PHI;
        }
    }
    
    public static File getFile(Scanner scanner, String prompt) {
        File output = null;
        while (true) {
            System.out.print(prompt + ": ");
            String path = scanner.nextLine();
            if (path.isEmpty()) {
                break;
            }
            try {
                File file = new File(path);
                file.getCanonicalPath();
                output = file;
                break;
            } catch (IOException e) {
                System.out.println("Invalid path (" + path + ")");
            }
        }
        return output;
    }
}
