import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class UI extends JFrame {
    private App controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtMaSV, txtHoTen, txtToan, txtVan, txtAnh, txtTimKiem;

    public UI() {
        controller = new App();
        initUI();
        loadDataToTable(controller.danhSachSinhVien());
    }

    private void initUI() {
        setTitle("HỆ THỐNG QUẢN LÝ SINH VIÊN");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TITLE BANNER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel lblTitle = new JLabel("QUẢN LÝ SINH VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM INPUT PANEL (LEFT) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Sinh Viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSV = new JTextField(15);
        txtHoTen = new JTextField(15);
        txtToan = new JTextField(15);
        txtVan = new JTextField(15);
        txtAnh = new JTextField(15);

        addFormField(formPanel, "Mã SV:", txtMaSV, gbc, 0);
        addFormField(formPanel, "Họ và Tên:", txtHoTen, gbc, 1);
        addFormField(formPanel, "Điểm Toán:", txtToan, gbc, 2);
        addFormField(formPanel, "Điểm Văn:", txtVan, gbc, 3);
        addFormField(formPanel, "Điểm Anh:", txtAnh, gbc, 4);

        // Action Buttons
        JPanel btnFormPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm Mới");

        btnFormPanel.add(btnAdd);
        btnFormPanel.add(btnEdit);
        btnFormPanel.add(btnDelete);
        btnFormPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(btnFormPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // --- TABLE & SEARCH PANEL (CENTER) ---
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        // Search & Filter Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTimKiem = new JTextField(15);
        JButton btnSearch = new JButton("Tìm Kiếm");
        JButton btnSortDesc = new JButton("Sắp Xếp ĐTB ↓");
        JButton btnStats = new JButton("Thống Kê");

        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnSearch);
        searchPanel.add(btnSortDesc);
        searchPanel.add(btnStats);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Mã SV", "Họ Tên", "Toán", "Văn", "Anh", "ĐTB" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // --- EVENT HANDLERS ---
        btnAdd.addActionListener(e -> actionAdd());
        btnEdit.addActionListener(e -> actionEdit());
        btnDelete.addActionListener(e -> actionDelete());
        btnClear.addActionListener(e -> resetForm());
        btnSearch.addActionListener(e -> actionSearch());
        btnSortDesc.addActionListener(e -> loadDataToTable(controller.sapXepTheoDTB(true)));
        btnStats.addActionListener(e -> actionShowStats());
    }

    private void addFormField(JPanel panel, String label, JTextField tf, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(tf, gbc);
    }

    private void loadDataToTable(List<SinhVienDTO> list) {
        tableModel.setRowCount(0);
        for (SinhVienDTO sv : list) {
            tableModel.addRow(new Object[] {
                    sv.getMaSV(),
                    sv.getHoTen(),
                    sv.getDiemToan(),
                    sv.getDiemVan(),
                    sv.getDiemAnh(),
                    String.format("%.2f", sv.getDiemTB())
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaSV.setText(tableModel.getValueAt(row, 0).toString());
            txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
            txtToan.setText(tableModel.getValueAt(row, 2).toString());
            txtVan.setText(tableModel.getValueAt(row, 3).toString());
            txtAnh.setText(tableModel.getValueAt(row, 4).toString());
            txtMaSV.setEditable(false);
        }
    }

    private void resetForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtToan.setText("");
        txtVan.setText("");
        txtAnh.setText("");
        txtMaSV.setEditable(true);
        table.clearSelection();
        loadDataToTable(controller.danhSachSinhVien());
    }

    private void actionAdd() {
        try {
            SinhVienDTO sv = getDTOFromInput();
            if (controller.themSinhVien(sv)) {
                JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm số hợp lệ!", "Lỗi Định Dạng",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actionEdit() {
        try {
            SinhVienDTO sv = getDTOFromInput();
            if (controller.suaThongTinSinhVien(sv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại điểm nhập vào!", "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actionDelete() {
        String maSV = txtMaSV.getText().trim();
        if (maSV.isEmpty())
            return;

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa SV này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.xoaSinhVien(maSV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                resetForm();
            }
        }
    }

    private void actionSearch() {
        String keyword = txtTimKiem.getText().trim();
        loadDataToTable(controller.timKiemSinhVien(keyword));
    }

    private void actionShowStats() {
        Map<String, Integer> stats = controller.thongKeDiemTB();
        StringBuilder sb = new StringBuilder("=== THỐNG KÊ PHÂN LOẠI ===\n\n");
        sb.append("Tổng số sinh viên: ").append(controller.danhSachSinhVien().size()).append("\n");
        stats.forEach((k, v) -> sb.append("- ").append(k).append(": ").append(v).append(" SV\n"));
        JOptionPane.showMessageDialog(this, sb.toString(), "Thống Kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private SinhVienDTO getDTOFromInput() {
        return new SinhVienDTO(
                txtMaSV.getText().trim(),
                txtHoTen.getText().trim(),
                Double.parseDouble(txtToan.getText().trim()),
                Double.parseDouble(txtAnh.getText().trim()),
                Double.parseDouble(txtVan.getText().trim()));
    }

}