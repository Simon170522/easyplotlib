package com.safonof;

public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static int compare(Point2D p1, Point2D p2) {
        return Double.compare(p1.x, p2.x);
    }

    @Override
    public String toString() {
        return String.format("{%.2f, %.2f}", x, y);
    }
}
