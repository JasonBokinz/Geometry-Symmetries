package geometry;

import java.util.*;

public class RadialGraph extends Shape {
    public Point center;
    public List<Point> neighbors;

    public RadialGraph(Point center, List<Point> neighbors) throws IllegalArgumentException {
        this.center = center;
        if (!validRadialGraph(neighbors)) {
            throw new IllegalArgumentException("Edges lengths not the same size");
        }
        this.neighbors = new ArrayList<>(neighbors);
    }

    public RadialGraph(Point center) {
        this.center = center;
        this.neighbors = new ArrayList<>(Arrays.asList(center));
    }

    @Override
    public RadialGraph rotateBy(int degrees) throws IllegalArgumentException {
        if (center == null || neighbors == null) {
            throw new IllegalArgumentException("Invalid Graph to rotate.");
        }
        double inRadians = Math.toRadians(degrees);
        List<Point> rotatedNeighbors = new ArrayList<>();
        for (Point point : neighbors) {
            double xFromCenter = point.x - center.x;
            double yFromCenter = point.y - center.y;

            double rotatedX = center.x + (xFromCenter * Math.cos(inRadians) - yFromCenter * Math.sin(inRadians));
            double rotatedY = center.y + (xFromCenter * Math.sin(inRadians) + yFromCenter * Math.cos(inRadians));

            rotatedNeighbors.add(new Point(point.name, Math.round(rotatedX * 100.0) / 100.0, Math.round(rotatedY * 100.0) / 100.0));
        }
        return new RadialGraph(center, rotatedNeighbors);
    }

    @Override
    public Shape translateBy(double x, double y) {
        List<Point> translatedNeighbors = new ArrayList<>();
        for (Point point : neighbors) {
            double translatedX = Math.round((point.x + x) * 100.0) / 100.0;
            double translatedY = Math.round((point.y + y) * 100.0) / 100.0;
            translatedNeighbors.add(new Point(point.name, translatedX, translatedY));
        }
        double translatedCenterX = Math.round((center.x + x) * 100.0) / 100.0;
        double translatedCenterY = Math.round((center.y + y) * 100.0) / 100.0;
        Point translatedCenter = new Point(center.name, translatedCenterX, translatedCenterY);

        return new RadialGraph(translatedCenter, translatedNeighbors);
    }

    @Override
    public String toString() {
        List<Point> sortedPoints = new ArrayList<>(neighbors);
        sortedPoints.sort(Comparator.comparingDouble(point -> {
            double angle = (Math.toDegrees(Math.atan2(point.y, point.x)) + 360) % 360;
            double distance = Math.sqrt(point.x * point.x + point.y * point.y);
            return angle * 1000 + distance;
        }));

        StringBuilder string = new StringBuilder("[");
        string.append(center).append("; ");
        int i = 0;
        for (Point point : sortedPoints) {
            string.append(point);
            if (i < sortedPoints.size() - 1) {
                string.append("; ");
            }
            i++;
        }
        string.append("]");
        return string.toString();
    }

    @Override
    public Point center() {
        return center;
    }

    private boolean validRadialGraph(List<Point> neighbors) {
        if (neighbors.size() < 2) {
            return false;
        }
        Point first = neighbors.get(0);
        double distanceFromCenter = Math.sqrt( Math.pow(center.x - first.x, 2) + Math.pow(center.y - first.y, 2) );
        for (Point point : neighbors) {
            double currentDistance = Math.sqrt(Math.pow(center.x - point.x, 2) + Math.pow(center.y - point.y, 2));
            if (currentDistance != distanceFromCenter) {
                return false;
            }
        }
        return true;
    }

    /* Driver method given to you as an outline for testing your code. You can modify this as you want, but please keep
     * in mind that the lines already provided here as expected to work exactly as they are (some lines have additional
     * explanation of what is expected) */
    public static void main(String... args) {
        Point center = new Point("center", 0, 0);
        Point east = new Point("east", 1, 0);
        Point west = new Point("west", -1, 0);
        Point north = new Point("north", 0, 1);
        Point south = new Point("south", 0, -1);
        Point toofarsouth = new Point("south", 0, -2);

        // A single node is a valid radial graph.
        RadialGraph lonely = new RadialGraph(center);

        // Must print: [(center, 0.0, 0.0)]
        System.out.println(lonely);


        // This line must throw IllegalArgumentException, since the edges will not be of the same length
        RadialGraph nope = new RadialGraph(center, Arrays.asList(north, toofarsouth, east, west));

        Shape g = new RadialGraph(center, Arrays.asList(north, south, east, west));

        // Must follow the documentation in the Shape abstract class, and print the following string:
        // [(center, 0.0, 0.0); (east, 1.0, 0.0); (north, 0.0, 1.0); (west, -1.0, 0.0); (south, 0.0, -1.0)]
        System.out.println(g);

        // After this counterclockwise rotation by 90 degrees, "north" must be at (-1, 0), and similarly for all the
        // other radial points. The center, however, must remain exactly where it was.
        g = g.rotateBy(90);

        // you should similarly add tests for the translateBy(x, y) method
    }
}
