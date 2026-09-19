package ui;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/** Bộ component tự vẽ (bo góc, hover, focus) để giao diện trông hiện đại. */
public final class UIKit {

    private UIKit() {
    }

    public static void smooth(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(style, size));
        l.setForeground(color);
        return l;
    }

    public static JPanel transparent(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    // =====================================================================
    //  Nút bấm bo góc
    // =====================================================================
    public enum Kind {
        PRIMARY, DANGER, SECONDARY
    }

    public static class RoundedButton extends JButton {
        private final Kind kind;

        public RoundedButton(String text, Kind kind) {
            super(text);
            this.kind = kind;
            setFont(Theme.font(Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder());
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(getText()) + 36, 40);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            int w = getWidth(), h = getHeight();
            ButtonModel m = getModel();
            Color bg, fg, line;
            if (!isEnabled()) {
                bg = Theme.DISABLED_BG;
                fg = Theme.DISABLED_FG;
                line = Theme.DISABLED_BG;
            } else {
                switch (kind) {
                    case PRIMARY:
                        bg = m.isPressed() ? Theme.PRIMARY_DARK : m.isRollover() ? Theme.PRIMARY_HOVER : Theme.PRIMARY;
                        fg = Color.WHITE;
                        line = bg;
                        break;
                    case DANGER:
                        bg = m.isPressed() ? new Color(0xFECACA) : m.isRollover() ? Theme.DANGER_SOFT : Color.WHITE;
                        fg = Theme.DANGER;
                        line = Theme.DANGER_LINE;
                        break;
                    default:
                        bg = m.isPressed() ? new Color(0xE5E7EB) : m.isRollover() ? new Color(0xF3F4F6) : Color.WHITE;
                        fg = Theme.TEXT;
                        line = Theme.BORDER;
                }
            }
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(line);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(getText())) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(fg);
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    // =====================================================================
    //  Chip (nút lựa chọn dạng viên thuốc) dùng cho phần sắp xếp
    // =====================================================================
    public static class Chip extends JToggleButton {
        public Chip(String text) {
            super(text);
            setFont(Theme.font(Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder());
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(getText()) + 28, 32);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            int w = getWidth(), h = getHeight();
            Color bg, fg, line;
            if (isSelected()) {
                bg = Theme.PRIMARY_SOFT;
                fg = Theme.PRIMARY;
                line = Theme.PRIMARY_LINE;
            } else {
                bg = getModel().isRollover() ? Theme.HEADER_BG : Color.WHITE;
                fg = Theme.MUTED;
                line = Theme.BORDER;
            }
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.setColor(line);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(fg);
            g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // =====================================================================
    //  Ô nhập liệu bo góc có placeholder, viền đổi màu khi focus
    // =====================================================================
    public static class RoundedTextField extends JTextField {
        private final String placeholder;

        public RoundedTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setFont(Theme.font(Font.PLAIN, 14));
            setForeground(Theme.TEXT);
            setCaretColor(Theme.PRIMARY);
            setSelectionColor(Theme.PRIMARY_LINE);
            setDisabledTextColor(Theme.MUTED);
            setBorder(new EmptyBorder(9, 12, 9, 12));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width, Math.max(d.height, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(isEnabled() ? Color.WHITE : Theme.DISABLED_BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();

            super.paintComponent(g);

            if (getText().isEmpty() && placeholder != null && !isFocusOwner()) {
                Graphics2D g3 = (Graphics2D) g.create();
                smooth(g3);
                g3.setFont(getFont());
                g3.setColor(Theme.DISABLED_FG);
                FontMetrics fm = g3.getFontMetrics();
                g3.drawString(placeholder, getInsets().left, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g3.dispose();
            }
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            boolean focus = isFocusOwner() && isEnabled();
            g2.setColor(focus ? Theme.PRIMARY : Theme.BORDER);
            g2.setStroke(new java.awt.BasicStroke(focus ? 1.6f : 1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();
        }
    }

    // =====================================================================
    //  Thẻ (card) nền trắng bo góc
    // =====================================================================
    public static class CardPanel extends JPanel {
        private final int arc;
        private Color fill = Color.WHITE;
        private Color line = Theme.BORDER;

        public CardPanel(LayoutManager layout) {
            this(layout, 16);
        }

        public CardPanel(LayoutManager layout, int arc) {
            super(layout);
            this.arc = arc;
            setOpaque(false);
        }

        public CardPanel colors(Color fill, Color line) {
            this.fill = fill;
            this.line = line;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.setColor(line);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =====================================================================
    //  Thanh cuộn mảnh, hiện đại
    // =====================================================================
    public static void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sp.getViewport().setBackground(Color.WHITE);
        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        sp.getVerticalScrollBar().setUnitIncrement(24);
        sp.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = Theme.SCROLL_THUMB;
            trackColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return zeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return zeroButton();
        }

        private JButton zeroButton() {
            JButton b = new JButton();
            Dimension zero = new Dimension(0, 0);
            b.setPreferredSize(zero);
            b.setMinimumSize(zero);
            b.setMaximumSize(zero);
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(Color.WHITE);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(isDragging || isThumbRollover() ? Theme.SCROLL_THUMB_HOVER : Theme.SCROLL_THUMB);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
