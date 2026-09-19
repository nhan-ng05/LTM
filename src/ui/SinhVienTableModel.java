package ui;

import dto.SinhVienDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/** Model của JTable: chuyển danh sách SinhVienDTO thành các dòng/cột. */
public class SinhVienTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = { "STT", "Mã SV", "Họ và tên", "Toán", "Văn", "Anh", "ĐTB", "Xếp loại" };

    private List<SinhVienDTO> data = new ArrayList<>();

    public void setData(List<SinhVienDTO> list) {
        this.data = new ArrayList<>(list);
        fireTableDataChanged();
    }

    public SinhVienDTO get(int row) {
        return data.get(row);
    }

    /** Vị trí của sinh viên trong bảng theo mã, -1 nếu không có. */
    public int indexOf(String maSV) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getMaSV().equalsIgnoreCase(maSV)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        SinhVienDTO sv = data.get(row);
        switch (col) {
            case 0:
                return row + 1;
            case 1:
                return sv.getMaSV();
            case 2:
                return sv.getHoTen();
            case 3:
                return sv.getDiemToan();
            case 4:
                return sv.getDiemVan();
            case 5:
                return sv.getDiemAnh();
            case 6:
                return sv.getDiemTB();
            default:
                return sv.getXepLoai();
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
