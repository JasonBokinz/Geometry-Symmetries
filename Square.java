package geometry;

import java.util.*;

public class Square extends Shape {
    public Point center;
    public List<Point> points;
    public Square(Point a, Point b, Point c, Point d) throws IllegalArgumentException {
        List<Point> points = new ArrayList<>();
        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);
        if (!validSquare(points)) {
            throw new IllegalArgumentException("Not a valid square!");
        }
        this.points = points;
        this.center = this.center();
    }
    @Override
    public Point center() {
        this.center = new Point("center", (points.get(0).x + points.get(2).x) / 2, (points.get(0).y + points.get(2).y) / 2);
        return center;
    }

    @Override
    public Square rotateBy(int degrees) {
        if (points == null) {
            throw new IllegalArgumentException("Invalid Square to rotate.");
        }
        double inRadians = Math.toRadians(degrees);
        List<Point> rotatedPoints = new ArrayList<>();
        for (Point point : points) {
            double xFromCenter = point.x - center.x;
            double yFromCenter = point.y - center.y;

            double rotatedX = center.x + (xFromCenter * Math.cos(inRadians) - yFromCenter * Math.sin(inRadians));
            double rotatedY = center.y + (xFromCenter * Math.sin(inRadians) + yFromCenter * Math.cos(inRadians));

            rotatedPoints.add(new Point(point.name, Math.round(rotatedX * 100.0) / 100.0, Math.round(rotatedY * 100.0) / 100.0));
        }
        return new Square(rotatedPoints.get(0), rotatedPoints.get(1), rotatedPoints.get(2), rotatedPoints.get(3));
    }

    @Override
    public Shape translateBy(double x, double y) {
        List<Point> translatedPoints = new ArrayList<>();
        for (Point point : points) {
            double translatedX = Math.round((point.x + x) * 100.0) / 100.0;
            double translatedY = Math.round((point.y + y) * 100.0) / 100.0;
            translatedPoints.add(new Point(point.name, translatedX, translatedY));
        }
        double translatedCenterX = Math.round((center.x + x) * 100.0) / 100.0;
        double translatedCenterY = Math.round((center.y + y) * 100.0) / 100.0;
        Point translatedCenter = new Point(center.name, translatedCenterX, translatedCenterY);

        return new Square(translatedPoints.get(0), translatedPoints.get(1), translatedPoints.get(2), translatedPoints.get(3));
    }
    @Override
    public String toString() {
        List<Point> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparingDouble(point -> {
            double angle = (Math.toDegrees(Math.atan2(point.y, point.x)) + 360) % 360;
            double distance = Math.sqrt(point.x * point.x + point.y * point.y);
            return angle * 1000 + distance;
        }));

        StringBuilder string = new StringBuilder("[");
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
    private boolean validSquare(List<Point> points) {
        double[] sideLengths = new double[6];
        int k = 0;

        // Calculate the side lengths and store them
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                sideLengths[k] = Math.sqrt(Math.pow(points.get(0).x - points.get(1).x, 2) + Math.pow(points.get(0).y - points.get(1).y, 2));
                k++;
            }
        }

        Arrays.sort(sideLengths); // Sort side lengths for comparison

        // Check for valid square properties
        return sideLengths[0] > 0 &&
                sideLengths[0] == sideLengths[1] && // First four are sides of the square
                sideLengths[0] == sideLengths[2] &&
                sideLengths[0] == sideLengths[3] &&
                sideLengths[4] == sideLengths[5]; // Last two are diagonals
    }
}
