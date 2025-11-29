package com.safonof;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sequence2D {
    public SequenceStyle style;
    final public List<Point2D> points = new ArrayList<>();
    public Color color;

    public Sequence2D(SequenceStyle style, Color color) {
        this.color = color;
        this.style = style;
    }

    public void add(double x, double y) {
        points.add(new Point2D(x, y));
        points.sort(Comparator.comparingDouble(p -> p.x));
    }

    public void addAll(double[] xs, double[] ys) {
        for (int i = 0; i < Math.min(xs.length, ys.length); i++) {
            add(xs[i], ys[i]);
        }
    }
    public void addAll(List<Double> xs, List<Double> ys) {
        for (int i = 0; i < Math.min(xs.size(), ys.size()); i++) {
            add(xs.get(i), ys.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Point2D point:
             points) {
            result.append(point.toString()).append(" ");
        }
        return result.toString();
    }

}
