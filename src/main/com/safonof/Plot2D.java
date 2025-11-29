package com.safonof;

import javax.swing.*;
import java.awt.*;

public class Plot2D {
    public PlotPanel panel;
    public String title;
    public int width, height;

    public Plot2D(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.panel = new PlotPanel(width, height);
    }

    public Plot2D(String title) {
        this.title = title;
        this.width = 333;
        this.height = 333;
        this.panel = new PlotPanel(width, height);
    }

    public void addSequence(Sequence2D sequence) {
        panel.addSequence(sequence);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame(title != null ? title : "Plot2D");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            panel.setPreferredSize(new Dimension(width, height));
            f.getContentPane().add(panel);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
