/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] collinearSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument to the constructor should not be null");
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("None of the input points can be null");
            }
        }
        Point[] myPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            myPoints[i] = points[i];
        }
        Arrays.sort(myPoints);
        if (checkDuplicates(myPoints)) {
            throw new IllegalArgumentException("Input contains duplicate points");
        }
        List<LineSegment> listCollinearSegments = findCollinearSegments(myPoints);
        collinearSegments = new LineSegment[listCollinearSegments.size()];
        int i = 0;
        for (LineSegment segment : listCollinearSegments) {
            collinearSegments[i] = segment;
            i++;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return collinearSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return collinearSegments.clone();
    }

    // collinearity check
    private static boolean isCollinear(Point[] points) {
        Point p = points[0];
        double slope = p.slopeTo(points[1]);
        for (int i = 2; i < points.length; i++) {
            double otherSlope = p.slopeTo(points[i]);
            if (slope != otherSlope) return false;
        }
        return true;
    }

    // check for duplicate points
    private static boolean checkDuplicates(Point[] points) {
        Point previousPoint = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(previousPoint) == 0) {
                return true;
            }
            previousPoint = points[i];
        }
        return false;
    }

    // generate all combinations of 4 points
    private static List<LineSegment> findCollinearSegments(Point[] array) {
        List<LineSegment> collinearSegments = new ArrayList<LineSegment>();
        for (int i = 0; i < array.length - 3; i++) {
            for (int j = i + 1; j < array.length - 2; j++) {
                for (int k = j + 1; k < array.length - 1; k++) {
                    for (int m = k + 1; m < array.length; m++) {
                        Point[] segment = { array[i], array[j], array[k], array[m] };
                        if (isCollinear(segment)) {
                            LineSegment collinearSegment = new LineSegment(array[i], array[m]);
                            collinearSegments.add(collinearSegment);
                        }
                    }
                }
            }
        }
        return collinearSegments;
    }

    public static void main(String[] args) {
        Point[] myPoints = {
                new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4)
        };
        BruteCollinearPoints points = new BruteCollinearPoints(myPoints);
        System.out.println(points.numberOfSegments());
    }
}
