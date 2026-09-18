import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private List<SinhVienDTO> listSinhVien;

    public App() {
        this.listSinhVien = new ArrayList<SinhVienDTO>();
    }

    // 1. Hiển thị danh sách sinh viên
    public List<SinhVienDTO> danhSachSinhVien() {
        return this.listSinhVien;
    }

    // 2. Thêm sinh viên
    public void themSinhVien(SinhVienDTO sinhVienDTO) {
        this.listSinhVien.add(sinhVienDTO);
    }

    // 3. Sửa thông tin sinh viên
    public void suaThongTinSinhVien(String maSV, SinhVienDTO update) {
        for (SinhVienDTO sinhVienDTO : this.listSinhVien) {
            if (sinhVienDTO.getMaSV().equalsIgnoreCase(maSV)) {
                sinhVienDTO.update(update);
            }
        }
    }

    // 4. Xoá sinh viên
    public void xoaSinhVien(String maSV) {
        this.listSinhVien.removeIf(n -> n.getMaSV().equalsIgnoreCase(maSV));
    }

    // 5. Tìm kiếm theo mã hoặc họ tên (trả về danh sách vì có thể trùng tên)
    public ArrayList<SinhVienDTO> timKiemSinhVien(String maSV_hoTen) {
        ArrayList<SinhVienDTO> temp = new ArrayList<SinhVienDTO>();
        for (SinhVienDTO sinhVienDTO : this.listSinhVien) {
            if (sinhVienDTO.getMaSV().equalsIgnoreCase(maSV_hoTen)
                    || sinhVienDTO.getHoTen().equalsIgnoreCase(maSV_hoTen)) {
                temp.add(sinhVienDTO);
            }
        }
        return temp;
    }

    // 6. Sắp xếp theo điểm trung bình
    public void sapXepTheoDTB_GiamDan() {
        Collections.sort(this.listSinhVien, Comparator.comparingDouble(SinhVienDTO::getDiemTB).reversed());
    }

    public void sapXepTheoDTB_TangDan() {
        Collections.sort(this.listSinhVien, Comparator.comparingDouble(SinhVienDTO::getDiemTB));
    }

    // 7. Thống kê số lượng sinh viên
    public void thongKeSoLuongSinhVien() {

    }

    // 8. Thống kê điểm trung bình của lớp - (gioi,kha,tb,yeu)
    public Map<String, Integer> thongKeDiemTB() {
        Map<String, Integer> thongKe = new HashMap<String, Integer>();
        int gioi = 0;
        int kha = 0;
        int tb = 0;
        int yeu = 0;

        for (SinhVienDTO sinhVienDTO : this.listSinhVien) {
            if (sinhVienDTO.getDiemTB() >= 8) {
                gioi++;
            } else if (sinhVienDTO.getDiemTB() >= 6.5) {
                kha++;
            } else if (sinhVienDTO.getDiemTB() >= 5) {
                tb++;
            } else {
                yeu++;
            }
        }
        thongKe.put("gioi", gioi);
        thongKe.put("kha", kha);
        thongKe.put("tb", tb);
        thongKe.put("yeu", yeu);
        return thongKe;
    }

    // 9. Làm mới dữ liệu
    public List<SinhVienDTO> getListSinhVien() {
        return this.listSinhVien;
    }

    // 10. Thoát chương trình
}
