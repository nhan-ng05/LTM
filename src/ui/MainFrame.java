package ui;

import bus.SinhVienBUS;
import dto.SinhVienDTO;
import ui.UIKit.Chip;
import ui.UIKit.CardPanel;
import ui.UIKit.Kind;
import ui.UIKit.RoundedButton;
import ui.UIKit.RoundedTextField;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 * Cửa sổ chính. Ánh xạ 10 chức năng của đề bài:
 * 1 Hiển thị (bảng) · 2 Thêm · 3 Sửa · 4 Xóa (form bên phải) · 5 Tìm kiếm (ô
 * search)
 * 6 Sắp xếp (chip) · 7-8 Thống kê (6 thẻ phía trên) · 9 Làm mới · 10 Thoát (góc
 * phải trên).
 */
public class MainFrame extends JFrame {

    private enum SortMode {
        MAC_DINH, DTB_GIAM, DTB_TANG
    }

    private final SinhVienBUS bus;
    private SortMode sortMode = SortMode.MAC_DINH;
    private String editingMa = null; // null = đang ở chế độ Thêm mới
    private boolean rebuilding = false; // chặn listener chọn dòng khi đang nạp lại bảng

    // bảng
    private final SinhVienTableModel tableModel = new SinhVienTableModel();
    private final JTable table = new JTable(tableModel);
    private RoundedTextField txtSearch;
    private JLabel lblCount;
    private JLabel lblStatus;

    // form
    private JLabel lblFormTitle, lblFormHint, lblPreview;
    private RoundedTextField txtMa, txtTen, txtToan, txtVan, txtAnh;
    private RoundedButton btnThem, btnSua, btnXoa, btnBoChon;

    // thống kê
    private StatCard cardTong, cardTbLop, cardGioi, cardKha, cardTrungBinh, cardYeu;

    public MainFrame(SinhVienBUS bus) {
        super("Quản lý sinh viên");
        this.bus = bus;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thoat();
            }
        });
        setMinimumSize(new Dimension(1120, 680));
        setSize(1280, 780);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 24, 12, 24));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
        setContentPane(root);

        enterAddMode();
        SwingUtilities.invokeLater(this::taiDuLieuBanDau);
    }

    // =====================================================================
    // DỰNG GIAO DIỆN
    // =====================================================================
    private JComponent buildHeader() {
        JPanel titles = UIKit.transparent(null);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(UIKit.label("Quản lý sinh viên", Font.BOLD, 26, Theme.TEXT));
        titles.add(Box.createVerticalStrut(2));
        titles.add(UIKit.label("Theo dõi danh sách, điểm số và thống kê của lớp", Font.PLAIN, 13, Theme.MUTED));

        RoundedButton btnRefresh = new RoundedButton("Làm mới dữ liệu", Kind.SECONDARY);
        btnRefresh.addActionListener(e -> lamMoiDuLieu());
        RoundedButton btnExit = new RoundedButton("Thoát", Kind.DANGER);
        btnExit.addActionListener(e -> thoat());

        JPanel actions = UIKit.transparent(new GridBagLayout());
        actions.add(btnRefresh);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(btnExit);

        JPanel header = UIKit.transparent(new BorderLayout());
        header.add(titles, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);
        return header;
    }

    private JComponent buildBody() {
        JPanel main = UIKit.transparent(new BorderLayout(16, 0));
        main.add(buildTableCard(), BorderLayout.CENTER);
        main.add(buildFormCard(), BorderLayout.EAST);

        JPanel body = UIKit.transparent(new BorderLayout(0, 16));
        body.add(buildStats(), BorderLayout.NORTH);
        body.add(main, BorderLayout.CENTER);
        return body;
    }

    private JComponent buildStats() {
        cardTong = new StatCard("Tổng sinh viên", Theme.PRIMARY);
        cardTbLop = new StatCard("ĐTB của lớp", Theme.INFO);
        cardGioi = new StatCard("Giỏi (≥ 8)", Theme.GIOI_FG);
        cardKha = new StatCard("Khá (6.5 – 7.99)", Theme.KHA_FG);
        cardTrungBinh = new StatCard("Trung bình (5 – 6.49)", Theme.TB_FG);
        cardYeu = new StatCard("Yếu (< 5)", Theme.YEU_FG);

        JPanel row = UIKit.transparent(new GridLayout(1, 6, 12, 0));
        row.add(cardTong);
        row.add(cardTbLop);
        row.add(cardGioi);
        row.add(cardKha);
        row.add(cardTrungBinh);
        row.add(cardYeu);
        return row;
    }

    private JComponent buildTableCard() {
        // --- thanh công cụ: tìm kiếm + sắp xếp
        txtSearch = new RoundedTextField("Tìm theo mã hoặc họ tên (không cần gõ dấu)...");
        onChange(txtSearch, this::refreshView);

        Chip chipMacDinh = new Chip("Mặc định");
        Chip chipGiam = new Chip("ĐTB giảm dần");
        Chip chipTang = new Chip("ĐTB tăng dần");
        ButtonGroup group = new ButtonGroup();
        for (Chip c : new Chip[] { chipMacDinh, chipGiam, chipTang }) {
            group.add(c);
        }
        chipMacDinh.setSelected(true);
        chipMacDinh.addActionListener(e -> doiCachSapXep(SortMode.MAC_DINH));
        chipGiam.addActionListener(e -> doiCachSapXep(SortMode.DTB_GIAM));
        chipTang.addActionListener(e -> doiCachSapXep(SortMode.DTB_TANG));

        JPanel sort = UIKit.transparent(null);
        sort.setLayout(new BoxLayout(sort, BoxLayout.X_AXIS));
        JLabel lblSort = UIKit.label("Sắp xếp", Font.BOLD, 12, Theme.MUTED);
        lblSort.setAlignmentY(Component.CENTER_ALIGNMENT);
        sort.add(lblSort);
        for (Chip c : new Chip[] { chipMacDinh, chipGiam, chipTang }) {
            sort.add(Box.createHorizontalStrut(8));
            c.setAlignmentY(Component.CENTER_ALIGNMENT);
            sort.add(c);
        }

        JPanel toolbar = UIKit.transparent(new BorderLayout(16, 0));
        toolbar.setBorder(new EmptyBorder(0, 0, 14, 0));
        toolbar.add(txtSearch, BorderLayout.CENTER);
        JPanel sortWrap = UIKit.transparent(new GridBagLayout());
        sortWrap.add(sort);
        toolbar.add(sortWrap, BorderLayout.EAST);

        // --- bảng
        TableStyle.apply(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || rebuilding) {
                return;
            }
            int row = table.getSelectedRow();
            if (row < 0) {
                if (editingMa != null) {
                    enterAddMode();
                }
                return;
            }
            SinhVienDTO sv = tableModel.get(row);
            if (!sv.getMaSV().equals(editingMa)) {
                enterEditMode(sv);
            }
        });
        table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "xoa");
        table.getActionMap().put("xoa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaSinhVien();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        UIKit.styleScrollPane(scroll);

        lblCount = UIKit.label(" ", Font.PLAIN, 12, Theme.MUTED);
        lblCount.setBorder(new EmptyBorder(10, 2, 0, 0));

        CardPanel card = new CardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 12, 16));
        card.add(toolbar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(lblCount, BorderLayout.SOUTH);
        return card;
    }

    private JComponent buildFormCard() {
        lblFormTitle = UIKit.label("Thêm sinh viên mới", Font.BOLD, 18, Theme.TEXT);
        lblFormHint = UIKit.label(" ", Font.PLAIN, 12, Theme.MUTED);

        txtMa = new RoundedTextField("VD: SV021");
        txtTen = new RoundedTextField("VD: Nguyễn Văn A");
        txtToan = new RoundedTextField("0 – 10");
        txtVan = new RoundedTextField("0 – 10");
        txtAnh = new RoundedTextField("0 – 10");
        for (RoundedTextField f : new RoundedTextField[] { txtToan, txtVan, txtAnh }) {
            f.setHorizontalAlignment(SwingConstants.CENTER);
            onChange(f, this::updatePreview);
        }
        // nhấn Enter trong bất kỳ ô nào = Lưu (Thêm hoặc Cập nhật tùy chế độ)
        for (RoundedTextField f : new RoundedTextField[] { txtMa, txtTen, txtToan, txtVan, txtAnh }) {
            f.addActionListener(e -> luuForm());
        }

        JPanel scores = new JPanel(new GridLayout(1, 3, 10, 0));
        scores.setOpaque(false);
        scores.add(labeled("Toán", txtToan));
        scores.add(labeled("Văn", txtVan));
        scores.add(labeled("Anh", txtAnh));
        leftAlignFullWidth(scores);

        // ô xem trước ĐTB
        lblPreview = UIKit.label("–", Font.BOLD, 20, Theme.PRIMARY);
        CardPanel preview = new CardPanel(new BorderLayout(0, 2), 12).colors(Theme.PRIMARY_SOFT, Theme.PRIMARY_LINE);
        preview.setBorder(new EmptyBorder(10, 14, 10, 14));
        preview.add(UIKit.label("Điểm trung bình dự kiến", Font.PLAIN, 12, Theme.MUTED), BorderLayout.NORTH);
        preview.add(lblPreview, BorderLayout.CENTER);
        leftAlignFullWidth(preview);

        btnThem = new RoundedButton("Thêm sinh viên", Kind.PRIMARY);
        btnThem.addActionListener(e -> themSinhVien());
        btnSua = new RoundedButton("Cập nhật", Kind.PRIMARY);
        btnSua.addActionListener(e -> suaSinhVien());
        btnXoa = new RoundedButton("Xóa", Kind.DANGER);
        btnXoa.addActionListener(e -> xoaSinhVien());
        btnBoChon = new RoundedButton("Bỏ chọn / Nhập mới", Kind.SECONDARY);
        btnBoChon.addActionListener(e -> enterAddMode());

        JPanel editRow = new JPanel(new GridLayout(1, 2, 10, 0));
        editRow.setOpaque(false);
        editRow.add(btnSua);
        editRow.add(btnXoa);

        leftAlignFullWidth(btnThem);
        leftAlignFullWidth(editRow);
        leftAlignFullWidth(btnBoChon);
        leftAlignFullWidth(lblFormTitle); // cho nhãn giãn hết chiều ngang để đổi chữ không bị cắt "..."
        leftAlignFullWidth(lblFormHint);

        JPanel col = UIKit.transparent(null);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(lblFormTitle);
        col.add(Box.createVerticalStrut(2));
        col.add(lblFormHint);
        col.add(Box.createVerticalStrut(18));
        col.add(labeled("Mã sinh viên", txtMa));
        col.add(Box.createVerticalStrut(14));
        col.add(labeled("Họ và tên", txtTen));
        col.add(Box.createVerticalStrut(14));
        col.add(scores);
        col.add(Box.createVerticalStrut(16));
        col.add(preview);
        col.add(Box.createVerticalStrut(20));
        col.add(btnThem);
        col.add(Box.createVerticalStrut(10));
        col.add(editRow);
        col.add(Box.createVerticalStrut(10));
        col.add(btnBoChon);
        col.add(Box.createVerticalGlue());

        CardPanel card = new CardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(340, 0));
        card.add(col, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildStatusBar() {
        lblStatus = UIKit.label(" ", Font.PLAIN, 12, Theme.MUTED);
        return lblStatus;
    }

    // ---- helpers dựng form
    private JPanel labeled(String caption, JComponent field) {
        JPanel p = UIKit.transparent(new BorderLayout(0, 6));
        p.add(UIKit.label(caption, Font.BOLD, 12, Theme.LABEL), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return leftAlignFullWidth(p);
    }

    private static <T extends JComponent> T leftAlignFullWidth(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    private static void onChange(JTextComponent c, Runnable r) {
        c.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                r.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                r.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                r.run();
            }
        });
    }

    // =====================================================================
    // CHẾ ĐỘ FORM: THÊM MỚI <-> CHỈNH SỬA
    // =====================================================================
    private void enterAddMode() {
        editingMa = null;
        table.clearSelection();
        txtMa.setText("");
        txtTen.setText("");
        txtToan.setText("");
        txtVan.setText("");
        txtAnh.setText("");
        txtMa.setEnabled(true);
        lblFormTitle.setText("Thêm sinh viên mới");
        lblFormHint.setText("Nhập thông tin rồi bấm Thêm (hoặc nhấn Enter)");
        btnThem.setEnabled(true);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnBoChon.setEnabled(false);
        updatePreview();
        txtMa.requestFocusInWindow();
    }

    private void enterEditMode(SinhVienDTO sv) {
        editingMa = sv.getMaSV();
        txtMa.setText(sv.getMaSV());
        txtTen.setText(sv.getHoTen());
        txtToan.setText(Theme.fmt(sv.getDiemToan()));
        txtVan.setText(Theme.fmt(sv.getDiemVan()));
        txtAnh.setText(Theme.fmt(sv.getDiemAnh()));
        txtMa.setEnabled(false); // mã là khóa chính, không cho đổi
        lblFormTitle.setText("Chỉnh sửa sinh viên");
        lblFormHint.setText("Mã " + sv.getMaSV() + " không thể thay đổi");
        btnThem.setEnabled(false);
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
        btnBoChon.setEnabled(true);
        updatePreview();
    }

    private void luuForm() {
        if (editingMa == null) {
            themSinhVien();
        } else {
            suaSinhVien();
        }
    }

    // =====================================================================
    // CÁC CHỨC NĂNG
    // =====================================================================
    private void taiDuLieuBanDau() {
        try {
            bus.lamMoiDuLieu();
            setStatus("Đã tải " + bus.thongKeSoLuongSinhVien() + " sinh viên từ cơ sở dữ liệu.");
        } catch (SQLException ex) {
            setStatus("Không kết nối được cơ sở dữ liệu.");
            baoLoiCSDL(ex);
        }
        refreshView();
    }

    // 2. Thêm
    private void themSinhVien() {
        if (editingMa != null) {
            return;
        }
        try {
            SinhVienDTO sv = docForm();
            bus.themSinhVien(sv);
            enterAddMode();
            refreshView();
            int idx = tableModel.indexOf(sv.getMaSV());
            if (idx >= 0) {
                table.scrollRectToVisible(table.getCellRect(idx, 0, true));
            }
            setStatus("Đã thêm sinh viên " + sv.getMaSV() + " – " + sv.getHoTen() + ".");
        } catch (IllegalArgumentException ex) {
            canhBao(ex.getMessage());
        } catch (SQLException ex) {
            baoLoiCSDL(ex);
        }
    }

    // 3. Sửa
    private void suaSinhVien() {
        if (editingMa == null) {
            return;
        }
        try {
            SinhVienDTO update = docForm();
            bus.suaThongTinSinhVien(editingMa, update);
            refreshView();
            setStatus("Đã cập nhật sinh viên " + editingMa + ".");
        } catch (IllegalArgumentException ex) {
            canhBao(ex.getMessage());
        } catch (SQLException ex) {
            baoLoiCSDL(ex);
        }
    }

    // 4. Xóa
    private void xoaSinhVien() {
        if (editingMa == null) {
            return;
        }
        String ma = editingMa;
        int chon = JOptionPane.showOptionDialog(this,
                "Bạn có chắc muốn xóa sinh viên \"" + txtTen.getText().trim() + "\" (" + ma + ")?\n"
                        + "Thao tác này không thể hoàn tác.",
                "Xác nhận xóa", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                new Object[] { "Xóa", "Hủy" }, "Hủy");
        if (chon != 0) {
            return;
        }
        try {
            bus.xoaSinhVien(ma);
            enterAddMode();
            refreshView();
            setStatus("Đã xóa sinh viên " + ma + ".");
        } catch (IllegalArgumentException ex) {
            canhBao(ex.getMessage());
        } catch (SQLException ex) {
            baoLoiCSDL(ex);
        }
    }

    // 6. Sắp xếp
    private void doiCachSapXep(SortMode mode) {
        this.sortMode = mode;
        refreshView();
    }

    // 9. Làm mới dữ liệu
    private void lamMoiDuLieu() {
        try {
            bus.lamMoiDuLieu();
            enterAddMode();
            txtSearch.setText("");
            refreshView();
            setStatus("Đã tải lại " + bus.thongKeSoLuongSinhVien() + " sinh viên từ cơ sở dữ liệu.");
        } catch (SQLException ex) {
            baoLoiCSDL(ex);
        }
    }

    // 10. Thoát
    private void thoat() {
        int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn thoát chương trình?", "Thoát",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Thoát", "Ở lại" }, "Ở lại");
        if (chon == 0) {
            dispose();
            System.exit(0);
        }
    }

    // =====================================================================
    // CẬP NHẬT HIỂN THỊ
    // =====================================================================
    /**
     * Áp dụng sắp xếp + tìm kiếm hiện tại, nạp lại bảng, giữ lại dòng đang chọn.
     */
    private void refreshView() {
        switch (sortMode) {
            case DTB_GIAM:
                bus.sapXepTheoDTB_GiamDan();
                break;
            case DTB_TANG:
                bus.sapXepTheoDTB_TangDan();
                break;
            default:
                bus.sapXepTheoMaSV();
        }

        List<SinhVienDTO> rows = bus.timKiemSinhVien(txtSearch.getText());
        int idx = -1;
        rebuilding = true;
        try {
            tableModel.setData(rows);
            if (editingMa != null) {
                idx = tableModel.indexOf(editingMa);
                if (idx >= 0) {
                    table.setRowSelectionInterval(idx, idx);
                    table.scrollRectToVisible(table.getCellRect(idx, 0, true));
                }
            }
        } finally {
            rebuilding = false;
        }
        if (editingMa != null && idx < 0) {
            enterAddMode(); // sinh viên đang sửa không còn trong danh sách hiển thị
        }

        int tong = bus.thongKeSoLuongSinhVien();
        lblCount.setText(rows.isEmpty()
                ? "Không có sinh viên nào phù hợp"
                : "Hiển thị " + rows.size() + " / " + tong + " sinh viên");
        capNhatThongKe();
    }

    // 7 + 8. Thống kê
    private void capNhatThongKe() {
        int tong = bus.thongKeSoLuongSinhVien();
        Map<String, Integer> tk = bus.thongKeDiemTB();

        cardTong.setValue(String.valueOf(tong));
        cardTong.setSubtitle("sinh viên trong lớp");
        cardTbLop.setValue(tong == 0 ? "–" : Theme.fmt(bus.diemTBLop()));
        cardTbLop.setSubtitle("trên thang điểm 10");
        setRank(cardGioi, tk.get("gioi"), tong);
        setRank(cardKha, tk.get("kha"), tong);
        setRank(cardTrungBinh, tk.get("tb"), tong);
        setRank(cardYeu, tk.get("yeu"), tong);
    }

    private void setRank(StatCard card, int soLuong, int tong) {
        card.setValue(String.valueOf(soLuong));
        card.setSubtitle((tong == 0 ? 0 : Math.round(soLuong * 100.0 / tong)) + "% của lớp");
    }

    /** Tính ĐTB dự kiến ngay khi người dùng gõ điểm. */
    private void updatePreview() {
        if (lblPreview == null || txtToan == null || txtVan == null || txtAnh == null) {
            return;
        }
        try {
            double t = parseDiemNhanh(txtToan), v = parseDiemNhanh(txtVan), a = parseDiemNhanh(txtAnh);
            SinhVienDTO tam = new SinhVienDTO("", "", t, v, a);
            lblPreview.setText(Theme.fmt(tam.getDiemTB()) + "  ·  " + tam.getXepLoai());
            lblPreview.setForeground(Theme.rankFg(tam.getXepLoai()));
        } catch (NumberFormatException ex) {
            lblPreview.setText("–");
            lblPreview.setForeground(Theme.MUTED);
        }
    }

    private double parseDiemNhanh(JTextComponent f) {
        double d = Double.parseDouble(f.getText().trim().replace(',', '.'));
        if (d < 0 || d > 10) {
            throw new NumberFormatException("ngoài khoảng 0-10");
        }
        return d;
    }

    // =====================================================================
    // ĐỌC FORM + THÔNG BÁO
    // =====================================================================
    private SinhVienDTO docForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim().replaceAll("\\s+", " ");
        if (ma.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập mã sinh viên.");
        }
        if (ten.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập họ và tên.");
        }
        return new SinhVienDTO(ma, ten,
                docDiem(txtToan, "Toán"), docDiem(txtVan, "Văn"), docDiem(txtAnh, "Anh"));
    }

    private double docDiem(JTextComponent f, String mon) {
        String s = f.getText().trim().replace(',', '.'); // cho phép gõ 8,5
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập điểm " + mon + ".");
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Điểm " + mon + " phải là một số (0 – 10).");
        }
    }

    private void setStatus(String msg) {
        lblStatus.setText(msg);
    }

    private void canhBao(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông tin chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
    }

    private void baoLoiCSDL(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Không thể thao tác với cơ sở dữ liệu.\n"
                        + "Hãy kiểm tra MySQL đã chạy, đã import file .sql và user/password trong JDBCConnection.\n\n"
                        + "Chi tiết: " + ex.getMessage(),
                "Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
    }
}
