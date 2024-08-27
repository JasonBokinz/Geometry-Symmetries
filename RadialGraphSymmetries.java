package geometry;

import java.util.*;
import java.util.Comparator;

public class RadialGraphSymmetries implements Symmetries<RadialGraph> {
    private boolean pointEquals(Point point1, Point point2) {
        return (point1.x == point2.x) && (point1.y == point2.y);
    }
    private RadialGraph shiftToZero(RadialGraph s1) {
        double translateByX = 0 - s1.center.x;
        double translateByY = 0 - s1.center.y;
        return (RadialGraph) s1.translateBy(translateByX, translateByY);
    }
    private double calculateAngle(Point center, Point point) {
        return Math.toDegrees(Math.atan2(point.y - center.y, point.x - center.x));
    }

    private double calculateAngle(Point center, Point point1, Point point2) {
        double angle1 = calculateAngle(center, point1);
        double angle2 = calculateAngle(center, point2);
        return angle2 - angle1;
    }
    @Override
    public boolean areSymmetric(RadialGraph s1, RadialGraph s2) {
        Point zero = new Point("zero", 0, 0);
        RadialGraph shiftedS1  = s1;
        RadialGraph shiftedS2 = s2;
        /* Shift center to (0, 0) */
        if (!pointEquals(s1.center, zero)) {
            shiftedS1 = shiftToZero(s1);
        }
        if (!pointEquals(s2.center, zero)) {
            shiftedS2 = shiftToZero(s2);
        }
        /* Check if the number of neighbors are equal */
        if (shiftedS1.neighbors.size() != shiftedS2.neighbors.size()) {
            return false;
        }
        /* Check if the lengths of the neighbors are equal*/
        double distanceFromCenterS1 = Math.sqrt(Math.pow(shiftedS1.center().x - shiftedS1.neighbors.get(0).x, 2) + Math.pow(shiftedS1.center().y - shiftedS1.neighbors.get(0).y, 2));
        double distanceFromCenterS2 = Math.sqrt(Math.pow(shiftedS2.center().x - shiftedS2.neighbors.get(0).x, 2) + Math.pow(shiftedS2.center().y - shiftedS2.neighbors.get(0).y, 2));
        if (distanceFromCenterS1 != distanceFromCenterS2) {
            return false;
        }
        boolean hasPoint = false;
        for (Point pointS1 : shiftedS1.neighbors) {
            for (Point pointS2 : shiftedS2.neighbors) {
                if (pointEquals(pointS1, pointS2)) {
                    hasPoint = true;
                    break;
                }
            }
        }
        List<Point> sortedPointsS1 = new ArrayList<>(shiftedS1.neighbors);
        sortedPointsS1.sort(Comparator.comparingDouble(point -> {
            double angle = (Math.toDegrees(Math.atan2(point.y, point.x)) + 360) % 360;
            double distance = Math.sqrt(point.x * point.x + point.y * point.y);
            return angle * 1000 + distance;
        }));
        List<Point> sortedPointsS2 = new ArrayList<>(shiftedS2.neighbors);
        sortedPointsS2.sort(Comparator.comparingDouble(point -> {
            double angle = (Math.toDegrees(Math.atan2(point.y, point.x)) + 360) % 360;
            double distance = Math.sqrt(point.x * point.x + point.y * point.y);
            return angle * 1000 + distance;
        }));

        int n = s1.neighbors.size();
        for (int i = 0; i < n; i++) {
            double angle1 = calculateAngle(sortedPointsS1.get((i + 1) % n), sortedPointsS1.get(i));
            double angle2 = calculateAngle(sortedPointsS2.get((i + 1) % n), sortedPointsS2.get(i));

            if (angle1 != angle2) {
                return false;
            }
        }
        return hasPoint;
    }
    @Override
    public List<RadialGraph> symmetriesOf(RadialGraph radialGraph) {
        List<Point> sortedNeighbors = new ArrayList<>(radialGraph.neighbors);
        sortedNeighbors.sort(Comparator.comparingDouble(point -> calculateAngle(radialGraph.center, point)));

        List<RadialGraph> symmetries = new ArrayList<>();
        int angle = 0;
        symmetries.add(radialGraph);
        for (int i = 0; i < sortedNeighbors.size() - 1; i++) {
            Point currentPoint = sortedNeighbors.get(i);
            Point nextPoint = sortedNeighbors.get(i+1);
            angle += calculateAngle(radialGraph.center, currentPoint, nextPoint);
            symmetries.add(radialGraph.rotateBy(angle));
        }
        return symmetries;
    }
}
