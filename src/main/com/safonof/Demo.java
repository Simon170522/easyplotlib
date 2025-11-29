// Demo.java
package com.safonof;

import java.awt.*;
import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) {
        // Тест с линиями
        Sequence2D lineSequence = new Sequence2D(SequenceStyle.LINES, Color.BLUE);
        // Тест с точками
        Sequence2D pointSequence = new Sequence2D(SequenceStyle.POINTS, Color.RED);

        var listX = new ArrayList<Double>();
        var listY = new ArrayList<Double>();
        var listY2 = new ArrayList<Double>();

        double scale = 55.0;
        for (int i = 1; i < scale; i++) {
            double x = 0.1 + i / scale * 5.0; // увеличим диапазон x
            listX.add(x);
            listY.add(1/x);                    // гипербола
            listY2.add(Math.sin(x) + 1);       // синус
        }

        lineSequence.addAll(listX, listY);
        pointSequence.addAll(listX, listY2);

        Plot2D plot = new Plot2D("Демонстрация графиков", 800, 600);
        plot.addSequence(lineSequence);
        plot.addSequence(pointSequence);
        plot.show();
    }
}