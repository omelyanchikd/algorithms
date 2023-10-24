/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        List<Point2D> insidePoints = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                insidePoints.add(p);
            }
        }
        return insidePoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        if (points.isEmpty()) {
            return null;
        }
        Point2D nearest = points.max();
        double nearestDistance = p.distanceSquaredTo(nearest);
        for (Point2D setPoint : points) {
            double currentDistance = p.distanceSquaredTo(setPoint);
            if (currentDistance < nearestDistance) {
                nearestDistance = currentDistance;
                nearest = setPoint;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
