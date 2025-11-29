// PlotPanel.java
package com.safonof;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlotPanel extends JPanel {
    private final List<Sequence2D> sequences = new ArrayList<>();
    public int width, height;

    public PlotPanel(int width, int height) {
        setBackground(Color.LIGHT_GRAY);
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    synchronized void addSequence(Sequence2D sequence) {
        if (sequence != null) {
            sequences.add(sequence);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        renderPlot(g2d, getWidth(), getHeight());
    }

    private void renderPlot(Graphics2D g, int w, int h) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        int left = 50, bottom = 40, top = 20, right = 20;
        int plotW = w - left - right;
        int plotH = h - top - bottom;

        // Рисуем фон области графика
        g.setColor(Color.WHITE);
        g.fillRect(left, top, plotW, plotH);
        g.setColor(Color.BLACK);
        g.drawRect(left, top, plotW, plotH);

        // Вычисляем границы данных
        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

        synchronized (this) {
            for (Sequence2D sequence : sequences) {
                if (sequence == null || sequence.points.isEmpty()) continue;
                for (Point2D p : sequence.points) {
                    if (Double.isFinite(p.x) && Double.isFinite(p.y)) {
                        minX = Math.min(minX, p.x);
                        maxX = Math.max(maxX, p.x);
                        minY = Math.min(minY, p.y);
                        maxY = Math.max(maxY, p.y);
                    }
                }
            }
        }

        if (!(minX < maxX && minY < maxY)) {
            minX = 0; maxX = 1; minY = 0; maxY = 1;
        }

        // Добавляем немного отступа по краям
        double xRange = maxX - minX;
        double yRange = maxY - minY;
        minX -= xRange * 0.05;
        maxX += xRange * 0.05;
        minY -= yRange * 0.05;
        maxY += yRange * 0.05;

        // Рисуем оси и метки
        drawAxes(g, left, top, plotW, plotH, minX, maxX, minY, maxY);

        // Рисуем данные
        synchronized (this) {
            for (Sequence2D sequence : sequences) {
                if (sequence != null) {
                    drawSequence(g, sequence, left, top, plotW, plotH, minX, maxX, minY, maxY);
                }
            }
        }
    }

    private void drawAxes(Graphics2D g, int left, int top, int w, int h,
                          double minX, double maxX, double minY, double maxY) {
        g.setColor(Color.BLACK);

        // Метки по оси X
        int xTicks = 10;
        for (int i = 0; i <= xTicks; i++) {
            double xValue = minX + (maxX - minX) * i / xTicks;
            int xPixel = left + (int)(i * w / (double)xTicks);

            g.drawLine(xPixel, top + h, xPixel, top + h + 5);
            String label = String.format("%.2f", xValue);
            FontMetrics fm = g.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g.drawString(label, xPixel - labelWidth/2, top + h + 20);
        }

        // Метки по оси Y
        int yTicks = 8;
        for (int i = 0; i <= yTicks; i++) {
            double yValue = minY + (maxY - minY) * i / yTicks;
            int yPixel = top + h - (int)(i * h / (double)yTicks);

            g.drawLine(left - 5, yPixel, left, yPixel);
            String label = String.format("%.2f", yValue);
            FontMetrics fm = g.getFontMetrics();
            int labelHeight = fm.getHeight();
            g.drawString(label, left - 40, yPixel + labelHeight/4);
        }
    }

    private void drawSequence(Graphics2D g, Sequence2D sequence,
                              int left, int top, int w, int h,
                              double minX, double maxX, double minY, double maxY) {
        if (sequence.points.isEmpty()) return;

        g.setColor(sequence.color);

        // Конвертируем точки в пиксели
        List<Point> pixelPoints = new ArrayList<>();
        for (Point2D dataPoint : sequence.points) {
            if (Double.isFinite(dataPoint.x) && Double.isFinite(dataPoint.y)) {
                int x = left + (int)((dataPoint.x - minX) / (maxX - minX) * w);
                int y = top + h - (int)((dataPoint.y - minY) / (maxY - minY) * h);
                pixelPoints.add(new Point(x, y));
            }
        }

        if (pixelPoints.size() < 2) return;

        // Рисуем в зависимости от стиля
        if (sequence.style == SequenceStyle.LINES) {
            // Соединяем точки линиями
            for (int i = 0; i < pixelPoints.size() - 1; i++) {
                Point p1 = pixelPoints.get(i);
                Point p2 = pixelPoints.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Для обоих стилей рисуем точки
        int pointSize = 4;
        for (Point p : pixelPoints) {
            g.fillOval(p.x - pointSize/2, p.y - pointSize/2, pointSize, pointSize);
        }
    }
}