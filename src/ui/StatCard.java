package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Thẻ thống kê nhỏ: chấm màu + tiêu đề + số lớn + dòng phụ. */
public class StatCard extends UIKit.CardPanel {

    private final JLabel lblValue = UIKit.label("0", Font.BOLD, 28, Theme.TEXT);
    private final JLabel lblSub = UIKit.label(" ", Font.PLAIN, 12, Theme.MUTED);

    public StatCard(String title, Color accent) {
        super(new BorderLayout(0, 2));
        setBorder(new EmptyBorder(14, 16, 12, 16));

        // BoxLayout (không tự xuống dòng như FlowLayout) để tiêu đề dài không bị cắt
        JPanel top = UIKit.transparent(null);
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.setBorder(new EmptyBorder(0, 0, 4, 0));
        Dot dot = new Dot(accent);
        dot.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel lblTitle = UIKit.label(title, Font.BOLD, 12, Theme.MUTED);
        lblTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
        top.add(dot);
        top.add(Box.createHorizontalStrut(8));
        top.add(lblTitle);
        top.add(Box.createHorizontalGlue());

        add(top, BorderLayout.NORTH);
        add(lblValue, BorderLayout.CENTER);
        add(lblSub, BorderLayout.SOUTH);
    }

    public void setValue(String value) {
        lblValue.setText(value);
    }

    public void setSubtitle(String text) {
        lblSub.setText(text);
    }

    private static class Dot extends JComponent {
        private final Color color;

        Dot(Color color) {
            this.color = color;
            Dimension size = new Dimension(10, 10);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            UIKit.smooth(g2);
            g2.setColor(color);
            g2.fillOval(0, 0, 10, 10);
            g2.dispose();
        }
    }
}
