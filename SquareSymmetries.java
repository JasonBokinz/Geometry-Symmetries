package geometry;

import java.util.*;

public class SquareSymmetries implements Symmetries<Square> {
    private boolean checkPoints(Square s1, Square s2, int [] indices) {
        int i = 0;
        for (int index : indices) {
            if (!pointEquals(s1.points.get(i), s2.points.get(index))) {
                return false;
            }
            i++;
        }
        return true;
    }
    private boolean pointEquals(Point point1, Point point2) {
        return (point1.x == point2.x) && (point1.y == point2.y);
    }
    private Square shiftToZero(Square s1) {
        double translateByX = 0 - s1.center.x;
        double translateByY = 0 - s1.center.y;
        return (Square) s1.translateBy(translateByX, translateByY);
    }
    @Override
    public boolean areSymmetric(Square s1, Square s2) {
        Point zero = new Point("zero", 0, 0);
        Square shiftedS1  = s1;
        Square shiftedS2 = s2;
        /* Shift center to (0, 0) */
        if (!pointEquals(s1.center, zero)) {
            shiftedS1 = shiftToZero(s1);
        }
        if (!pointEquals(s2.center, zero)) {
            shiftedS2 = shiftToZero(s2);
        }
        /* Check each 90 degrees of rotation (4 symmetries) */
        for (int i = 0; i < 4; i++) {
            int numSamePoints = 0;
            shiftedS1 = shiftedS1.rotateBy(90);
            for (int j = 0; j < 4; j++) {
                if (pointEquals(shiftedS1.points.get(j), shiftedS2.points.get(j))) {
                    numSamePoints++;
                }
            } if (numSamePoints == 4) { return true; }
        }
        /* Check vertical reflection */
        if (checkPoints(shiftedS1, shiftedS2, new int[]{3, 2, 1, 0})) {
            return true;
        }
        /* Check horizontal reflection */
        if (checkPoints(shiftedS1, shiftedS2, new int[]{1, 0, 3, 2})) {
            return true;
        }
        /* Check diagonal reflection */
        if (checkPoints(shiftedS1, shiftedS2, new int[]{0, 3, 2, 1})) {
            return true;
        }
        /* Check counter-diagonal reflection */
        if (checkPoints(shiftedS1, shiftedS2, new int[]{2, 1, 0, 3})) {
            return true;
        }
        /* Must not be symmetrical */
        return false;
    }

    @Override
    public List<Square> symmetriesOf(Square square) {
        List<Square> symmetries = new ArrayList<>();
        /* Add 4 rotation symmetries (including the identity) */
        for (int i = 0, k = -90; i < 4; i++, k+=90) {
            symmetries.add(square.rotateBy(90+k));
        }
        /* Add vertical reflection symmetry */
        Point verticalLL = new Point(square.points.get(3).name, square.points.get(0).x, square.points.get(0).y);
        Point verticalLR = new Point(square.points.get(2).name, square.points.get(1).x, square.points.get(1).y);
        Point verticalUR = new Point(square.points.get(1).name, square.points.get(2).x, square.points.get(2).y);
        Point verticalUL = new Point(square.points.get(0).name, square.points.get(3).x, square.points.get(3).y);
        symmetries.add(new Square(verticalLL, verticalLR, verticalUR, verticalUL));

        /* Add horizontal reflection symmetry */
        Point horizontalLL = new Point(square.points.get(1).name, square.points.get(0).x, square.points.get(0).y);
        Point horizontalLR = new Point(square.points.get(0).name, square.points.get(1).x, square.points.get(1).y);
        Point horizontalUR = new Point(square.points.get(3).name, square.points.get(2).x, square.points.get(2).y);
        Point horizontalUL = new Point(square.points.get(2).name, square.points.get(3).x, square.points.get(3).y);
        symmetries.add(new Square(horizontalLL, horizontalLR, horizontalUR, horizontalUL));

        /* Add diagonal reflection symmetry */
        Point diagonalLL = new Point(square.points.get(0).name, square.points.get(0).x, square.points.get(0).y);
        Point diagonalLR = new Point(square.points.get(3).name, square.points.get(1).x, square.points.get(1).y);
        Point diagonalUR = new Point(square.points.get(2).name, square.points.get(2).x, square.points.get(2).y);
        Point diagonalUL = new Point(square.points.get(1).name, square.points.get(3).x, square.points.get(3).y);
        symmetries.add(new Square(diagonalLL, diagonalLR, diagonalUR, diagonalUL));

        /* Add counter-diagonal reflection symmetry */
        Point cDiagonalLL = new Point(square.points.get(2).name, square.points.get(0).x, square.points.get(0).y);
        Point cDiagonalLR = new Point(square.points.get(1).name, square.points.get(1).x, square.points.get(1).y);
        Point cDiagonalUR = new Point(square.points.get(0).name, square.points.get(2).x, square.points.get(2).y);
        Point cDiagonalUL = new Point(square.points.get(3).name, square.points.get(3).x, square.points.get(3).y);
        symmetries.add(new Square(cDiagonalLL, cDiagonalLR, cDiagonalUR, cDiagonalUL));

        return symmetries;
    }
}
