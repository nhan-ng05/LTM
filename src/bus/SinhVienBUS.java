package bus;

import dao.SinhVienDAO;
import dto.SinhVienDTO;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tầng nghiệp vụ (trước đây là class App).
 * - Giữ 1 danh sách cache trong bộ nhớ để giao diện hiển thị nhanh.
 * - Mọi thao tác thêm/sửa/xóa đều ghi xuống DB trước, thành công mới cập nhật cache.
 * - Dữ liệu sai => ném IllegalArgumentException (giao diện hiện cảnh báo).
 * - Lỗi DB => ném SQLException (giao diện hiện thông báo lỗi).
 */
public class SinhVienBUS {

    private final SinhVienDAO dao;
    private List<SinhVienDTO> listSinhVien = new ArrayList<>();

    public SinhVienBUS() {
        this(new SinhVienDAO());
    }

    public SinhVienBUS(SinhVienDAO dao) {
        this.dao = dao;
    }

    // 9. Làm mới dữ liệu: tải lại toàn bộ từ DB
    public void lamMoiDuLieu() throws SQLException {
        this.listSinhVien = new ArrayList<>(dao.getAll());
    }

    // 1. Hiển thị danh sách sinh viên (bản chỉ đọc, tránh bên ngoài sửa lén)
    public List<SinhVienDTO> danhSachSinhVien() {
        return Collections.unmodifiableList(listSinhVien);
    }

    // 2. Thêm sinh viên
    public void themSinhVien(SinhVienDTO sv) throws SQLException {
        validate(sv);
        if (timTheoMa(sv.getMaSV()) != null) {
            throw new IllegalArgumentException("Mã sinh viên \"" + sv.getMaSV() + "\" đã tồn tại.");
        }
        dao.insert(sv);
        listSinhVien.add(sv);
    }

    // 3. Sửa thông tin sinh viên (không cho đổi mã vì là khóa chính)
    public void suaThongTinSinhVien(String maSV, SinhVienDTO update) throws SQLException {
        SinhVienDTO goc = timTheoMa(maSV);
        if (goc == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên có mã \"" + maSV + "\".");
        }
        update.setMaSV(goc.getMaSV());
        validate(update);
        dao.update(update);
        goc.update(update);
    }

    // 4. Xóa sinh viên
    public void xoaSinhVien(String maSV) throws SQLException {
        SinhVienDTO goc = timTheoMa(maSV);
        if (goc == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên có mã \"" + maSV + "\".");
        }
        dao.delete(goc.getMaSV());
        listSinhVien.remove(goc);
    }

    // 5. Tìm kiếm theo mã hoặc họ tên: không phân biệt hoa/thường, KHÔNG cần gõ dấu,
    // chỉ cần chứa từ khóa (VD "an" sẽ ra "Nguyễn Văn An", "Phạm Minh Anh"...)
    public List<SinhVienDTO> timKiemSinhVien(String tuKhoa) {
        String key = boDau(tuKhoa);
        if (key.isEmpty()) {
            return new ArrayList<>(listSinhVien);
        }
        List<SinhVienDTO> ketQua = new ArrayList<>();
        for (SinhVienDTO sv : listSinhVien) {
            if (boDau(sv.getMaSV()).contains(key) || boDau(sv.getHoTen()).contains(key)) {
                ketQua.add(sv);
            }
        }
        return ketQua;
    }

    /** Tìm chính xác theo mã (không phân biệt hoa/thường). Trả về null nếu không có. */
    public SinhVienDTO timTheoMa(String maSV) {
        if (maSV == null) {
            return null;
        }
        for (SinhVienDTO sv : listSinhVien) {
            if (sv.getMaSV().equalsIgnoreCase(maSV.trim())) {
                return sv;
            }
        }
        return null;
    }

    // 6. Sắp xếp theo điểm trung bình
    public void sapXepTheoDTB_GiamDan() {
        listSinhVien.sort(Comparator.comparingDouble(SinhVienDTO::getDiemTB).reversed());
    }

    public void sapXepTheoDTB_TangDan() {
        listSinhVien.sort(Comparator.comparingDouble(SinhVienDTO::getDiemTB));
    }

    public void sapXepTheoMaSV() {
        listSinhVien.sort(Comparator.comparing(SinhVienDTO::getMaSV, String.CASE_INSENSITIVE_ORDER));
    }

    // 7. Thống kê số lượng sinh viên
    public int thongKeSoLuongSinhVien() {
        return listSinhVien.size();
    }

    // 8. Thống kê điểm trung bình của lớp: số lượng giỏi / khá / trung bình / yếu
    public Map<String, Integer> thongKeDiemTB() {
        int gioi = 0, kha = 0, tb = 0, yeu = 0;
        for (SinhVienDTO sv : listSinhVien) {
            switch (sv.getXepLoai()) {
                case SinhVienDTO.GIOI:
                    gioi++;
                    break;
                case SinhVienDTO.KHA:
                    kha++;
                    break;
                case SinhVienDTO.TRUNG_BINH:
                    tb++;
                    break;
                default:
                    yeu++;
            }
        }
        Map<String, Integer> thongKe = new LinkedHashMap<>(); // giữ đúng thứ tự put
        thongKe.put("gioi", gioi);
        thongKe.put("kha", kha);
        thongKe.put("tb", tb);
        thongKe.put("yeu", yeu);
        return thongKe;
    }

    /** Điểm trung bình chung của cả lớp (0 nếu lớp trống). */
    public double diemTBLop() {
        if (listSinhVien.isEmpty()) {
            return 0;
        }
        double tong = 0;
        for (SinhVienDTO sv : listSinhVien) {
            tong += sv.getDiemTB();
        }
        return tong / listSinhVien.size();
    }

    // ---------------------------------------------------------------- helpers

    private void validate(SinhVienDTO sv) {
        if (sv.getMaSV() == null || sv.getMaSV().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sinh viên không được để trống.");
        }
        if (sv.getMaSV().trim().length() > 20) {
            throw new IllegalArgumentException("Mã sinh viên tối đa 20 ký tự.");
        }
        if (sv.getHoTen() == null || sv.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        if (sv.getHoTen().trim().length() > 100) {
            throw new IllegalArgumentException("Họ tên tối đa 100 ký tự.");
        }
        kiemTraDiem(sv.getDiemToan(), "Toán");
        kiemTraDiem(sv.getDiemVan(), "Văn");
        kiemTraDiem(sv.getDiemAnh(), "Anh");
        sv.setMaSV(sv.getMaSV().trim());
        sv.setHoTen(sv.getHoTen().trim());
    }

    private void kiemTraDiem(double diem, String mon) {
        if (Double.isNaN(diem) || diem < 0 || diem > 10) {
            throw new IllegalArgumentException("Điểm " + mon + " phải nằm trong khoảng 0 – 10.");
        }
    }

    /** "Nguyễn Văn Ân" -> "nguyen van an" để tìm kiếm không phụ thuộc dấu. */
    private static String boDau(String s) {
        if (s == null) {
            return "";
        }
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return n.replace('đ', 'd').replace('Đ', 'D').toLowerCase().trim();
    }
}
