/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;

    private class Node {
        Point2D point;
        int level;
        int count;
        Node left, right;

        public Node(Point2D p, int levelIn, int countIn) {
            this.point = p;
            this.level = levelIn;
            this.count = countIn;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        root = insert(root, p, 0);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        Node node = root;
        while (node != null) {
            double pCoordinate = p.x();
            double nodeCoordinate = node.point.x();
            if (node.level % 2 == 1) {
                pCoordinate = p.y();
                nodeCoordinate = node.point.y();
            }
            if (pCoordinate < nodeCoordinate) {
                node = node.left;
            }
            else if (pCoordinate > nodeCoordinate) {
                node = node.right;
            }
            else {
                if (p.compareTo(node.point) == 0) {
                    return true;
                }
                else {
                    node = node.right;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0, 1);
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        return range(root, rect, new RectHV(0, 0, 1, 1));
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null input is not allowed!");
        }
        if (isEmpty()) {
            return null;
        }
        return nearest(root, p, new RectHV(0, 0, 1, 1), p.distanceSquaredTo(root.point),
                       root.point);
    }

    // recursive insert into 2d tree
    private Node insert(Node node, Point2D p, int level) {
        if (node == null) return new Node(p, level, 1);
        double pCoordinate = p.x();
        double nodeCoordinate = node.point.x();
        if (node.level % 2 == 1) {
            pCoordinate = p.y();
            nodeCoordinate = node.point.y();
        }
        if (pCoordinate < nodeCoordinate) {
            node.left = insert(node.left, p, level + 1);
        }
        else if (pCoordinate >= nodeCoordinate) {
            if (p.compareTo(node.point) != 0)
                node.right = insert(node.right, p, level + 1);
        }
        node.count = 1 + size(node.left) + size(node.right);
        return node;
    }

    // recursive size of a 2d tree
    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    // draw from node
    private void draw(Node node, double left, double right) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        Point2D start, end;
        double parentAxis;
        if (node.level % 2 == 1) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            start = new Point2D(left, node.point.y());
            end = new Point2D(right, node.point.y());
            parentAxis = node.point.y();
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            start = new Point2D(node.point.x(), left);
            end = new Point2D(node.point.x(), right);
            parentAxis = node.point.x();
        }
        start.drawTo(end);
        draw(node.left, 0, parentAxis);
        draw(node.right, parentAxis, 1);
    }

    // recursive range search
    private Iterable<Point2D> range(Node node, RectHV rect, RectHV nodeRectangle) {
        List<Point2D> insidePoints = new ArrayList<Point2D>();
        if (node == null) return insidePoints;
        if (rect.intersects(nodeRectangle)) {
            RectHV left, right;
            if (node.level % 2 == 1) {
                left = new RectHV(nodeRectangle.xmin(), nodeRectangle.ymin(), nodeRectangle.xmax(),
                                  node.point.y());
                right = new RectHV(nodeRectangle.xmin(), node.point.y(), nodeRectangle.xmax(),
                                   nodeRectangle.ymax());
            }
            else {
                left = new RectHV(nodeRectangle.xmin(), nodeRectangle.ymin(), node.point.x(),
                                  nodeRectangle.ymax());
                right = new RectHV(node.point.x(), nodeRectangle.ymin(), nodeRectangle.xmax(),
                                   nodeRectangle.ymax());
            }
            if (rect.contains(node.point)) {
                insidePoints.add(node.point);
            }
            insidePoints.addAll((ArrayList<Point2D>) range(node.left, rect, left));
            insidePoints.addAll((ArrayList<Point2D>) range(node.right, rect, right));
        }
        return insidePoints;
    }

    // recursive nearest neighbor
    private Point2D nearest(Node node, Point2D p, RectHV nodeRectangle, double nearestDistance,
                            Point2D nearestNeighbor) {
        if (node == null) return nearestNeighbor;
        if (nodeRectangle.distanceSquaredTo(p) < nearestDistance) {
            RectHV left, right;
            boolean leftFirst = true;
            if (node.level % 2 == 1) {
                left = new RectHV(nodeRectangle.xmin(), nodeRectangle.ymin(), nodeRectangle.xmax(),
                                  node.point.y());
                right = new RectHV(nodeRectangle.xmin(), node.point.y(), nodeRectangle.xmax(),
                                   nodeRectangle.ymax());
                if (node.point.y() <= p.y()) leftFirst = false;
            }
            else {
                left = new RectHV(nodeRectangle.xmin(), nodeRectangle.ymin(), node.point.x(),
                                  nodeRectangle.ymax());
                right = new RectHV(node.point.x(), nodeRectangle.ymin(), nodeRectangle.xmax(),
                                   nodeRectangle.ymax());
                if (node.point.x() <= p.x()) leftFirst = false;
            }
            double distance = p.distanceSquaredTo(node.point);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestNeighbor = node.point;
            }
            if (leftFirst) {
                Point2D nearestLeft = nearest(node.left, p, left, nearestDistance, nearestNeighbor);
                double distanceLeft = p.distanceSquaredTo(nearestLeft);
                if (distanceLeft < nearestDistance) {
                    nearestDistance = distanceLeft;
                    nearestNeighbor = nearestLeft;
                }
                Point2D nearestRight = nearest(node.right, p, right, nearestDistance,
                                               nearestNeighbor);
                double distanceRight = p.distanceSquaredTo(nearestRight);
                if (distanceRight < nearestDistance) {
                    nearestDistance = distanceRight;
                    nearestNeighbor = nearestRight;
                }
            }
            else {
                Point2D nearestRight = nearest(node.right, p, right, nearestDistance,
                                               nearestNeighbor);
                double distanceRight = p.distanceSquaredTo(nearestRight);
                if (distanceRight < nearestDistance) {
                    nearestDistance = distanceRight;
                    nearestNeighbor = nearestRight;
                }
                Point2D nearestLeft = nearest(node.left, p, left, nearestDistance, nearestNeighbor);
                double distanceLeft = p.distanceSquaredTo(nearestLeft);
                if (distanceLeft < nearestDistance) {
                    nearestDistance = distanceLeft;
                    nearestNeighbor = nearestLeft;
                }
            }
        }
        return nearestNeighbor;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        // tree.insert(new Point2D(0.5, 0.5));
        // tree.insert(new Point2D(0.7, 0.5));
        // tree.insert(new Point2D(0.3, 0.3));
        // tree.insert(new Point2D(0.9, 0.5));
        // tree.insert(new Point2D(0.372, 0.497));
        // tree.insert(new Point2D(0.564, 0.413));
        // tree.insert(new Point2D(0.226, 0.577));
        // tree.insert(new Point2D(0.144, 0.179));
        // tree.insert(new Point2D(0.083, 0.510));
        // tree.insert(new Point2D(0.320, 0.708));
        // tree.insert(new Point2D(0.417, 0.362));
        // tree.insert(new Point2D(0.862, 0.825));
        // tree.insert(new Point2D(0.785, 0.725));
        // tree.insert(new Point2D(0.499, 0.208));
        tree.draw();
        System.out.println(tree.contains(new Point2D(0.5, 0.4)));
        System.out.println(tree.nearest(new Point2D(0.199, 0.513)));
        RectHV rect = new RectHV(0.1, 0.1, 0.3, 0.3);
        ArrayList<Point2D> insidePoints = (ArrayList<Point2D>) tree.range(rect);
        System.out.println(insidePoints.size());
        for (Point2D p : insidePoints) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            p.draw();
        }
    }
}
