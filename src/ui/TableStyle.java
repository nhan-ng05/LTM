package ui;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Áp dụng phong cách hiện đại (không lưới, header nhạt, badge xếp loại) cho JTable. */
final class TableStyle {

    private TableStyle() {
    }

    private static final int[] WIDTHS = { 56, 100, 260, 80, 80, 80, 90, 130 };

    private static Border cellBorder() {
        return BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Theme.ROW_LINE), new EmptyBorder(0, 12, 0, 12));
    }

    static void apply(JTable table) {
        table.setRowHeight(42);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(Theme.font(Font.PLAIN, 14));
        table.setForeground(Theme.TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(Theme.PRIMARY_SOFT);
        table.setSelectionForeground(Theme.TEXT);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        TableColumnModel cm = table.getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            TableColumn col = cm.getColumn(i);
            col.setPreferredWidth(WIDTHS[i]);
            if (i == 7) {
                col.setCellRenderer(new BadgeRenderer());
            } else {
                int align = (i == 1 || i == 2) ? SwingConstants.LEFT : SwingConstants.CENTER;
                col.setCellRenderer(new CellRenderer(align, i == 6));
            }
        }
    }

    // ---------------------------------------------------------------- header
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                int col) {
            super.getTableCellRendererComponent(t, v, false, false, row, col);
            setOpaque(true);
            setBackground(Theme.HEADER_BG);
            setForeground(Theme.MUTED);
            setFont(Theme.font(Font.BOLD, 12));
            setHorizontalAlignment((col == 1 || col == 2) ? SwingConstants.LEFT : SwingConstants.CENTER);
            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, Theme.BORDER), new EmptyBorder(0, 12, 0, 12)));
            return this;
        }
    }

    // ---------------------------------------------------------------- ô thường
    private static class CellRenderer extends DefaultTableCellRenderer {
        private final boolean bold;

        CellRenderer(int align, boolean bold) {
            this.bold = bold;
            setHorizontalAlignment(align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                int col) {
            super.getTableCellRendererComponent(t, v, sel, false, row, col);
            setBackground(sel ? Theme.PRIMARY_SOFT : Color.WHITE);
            setForeground(Theme.TEXT);
            setFont(Theme.font(bold ? Font.BOLD : Font.PLAIN, 14));
            setBorder(cellBorder());
            return this;
        }

        @Override
        protected void setValue(Object v) {
            setText(v instanceof Double ? Theme.fmt((Double) v) : String.valueOf(v));
        }
    }

    // ---------------------------------------------------------------- badge xếp loại
    private static class BadgeRenderer extends DefaultTableCellRenderer {
        private Color pillBg = Color.LIGHT_GRAY;
        private Color pillFg = Color.BLACK;

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                int col) {
            super.getTableCellRendererComponent(t, v, sel, false, row, col);
            String xepLoai = String.valueOf(v);
            pillBg = Theme.rankBg(xepLoai);
            pillFg = Theme.rankFg(xepLoai);
            setBackground(sel ? Theme.PRIMARY_SOFT : Color.WHITE);
            setFont(Theme.font(Font.BOLD, 12));
            setBorder(cellBorder());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            UIKit.smooth(g2);
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int pw = fm.stringWidth(text) + 24, ph = 24;
            int x = (getWidth() - pw) / 2;
            int y = (getHeight() - ph) / 2 - 1;
            g2.setColor(pillBg);
            g2.fillRoundRect(x, y, pw, ph, ph, ph);
            g2.setColor(pillFg);
            g2.drawString(text, x + 12, y + (ph - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }
}
