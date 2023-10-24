/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] collinearSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

    // find collinear segments
    private static List<LineSegment> findCollinearSegments(Point[] points) {
        List<LineSegment> segments = new ArrayList<LineSegment>();
        List<LineSegment> segmentsToAvoid = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length - 3; i++) {
            Point[] otherPoints = new Point[points.length - i - 1];
            for (int j = i + 1; j < points.length; j++) {
                otherPoints[j - i - 1] = points[j];
            }
            Arrays.sort(otherPoints, points[i].slopeOrder());
            double previousSlope = points[i].slopeTo(otherPoints[0]);
            List<Point> potentialCollinearPoints = new ArrayList<Point>();
            for (int j = 1; j < otherPoints.length; j++) {
                double currentSlope = points[i].slopeTo(otherPoints[j]);
                if (currentSlope == previousSlope) {
                    if (potentialCollinearPoints.isEmpty()) {
                        potentialCollinearPoints.add(otherPoints[j - 1]);
                    }
                    potentialCollinearPoints.add(otherPoints[j]);
                }
                else {
                    if (potentialCollinearPoints.size() > 0) {
                        if (potentialCollinearPoints.size() >= 3) {
                            Point endPoint = potentialCollinearPoints.get(
                                    potentialCollinearPoints.size() - 1);
                            LineSegment segmentToAdd = new LineSegment(points[i], endPoint);
                            if (!(segmentsToAvoid.contains(segmentToAdd))) {
                                segments.add(segmentToAdd);
                                for (int k = 0; k < potentialCollinearPoints.size() - 1; k++) {
                                    segmentsToAvoid.add(
                                            new LineSegment(potentialCollinearPoints.get(k),
                                                            endPoint));
                                }
                            }
                        }
                        potentialCollinearPoints.clear();
                    }
                    previousSlope = currentSlope;
                }
            }
        }
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
