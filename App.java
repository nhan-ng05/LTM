import java.util.*;

public class App {
    private SinhVienDAO dao;

    public App() {
        this.dao = new SinhVienDAO();
    }

    public List<SinhVienDTO> danhSachSinhVien() {
        return dao.getAll();
    }

    public boolean themSinhVien(SinhVienDTO sv) {
        return dao.insert(sv);
    }

    public boolean suaThongTinSinhVien(SinhVienDTO sv) {
        return dao.update(sv);
    }

    public boolean xoaSinhVien(String maSV) {
        return dao.delete(maSV);
    }

    public List<SinhVienDTO> timKiemSinhVien(String keyword) {
        return dao.search(keyword);
    }

    public List<SinhVienDTO> sapXepTheoDTB(boolean giamDan) {
        List<SinhVienDTO> list = dao.getAll();
        if (giamDan) {
            list.sort(Comparator.comparingDouble(SinhVienDTO::getDiemTB).reversed());
        } else {
            list.sort(Comparator.comparingDouble(SinhVienDTO::getDiemTB));
        }
        return list;
    }

    public Map<String, Integer> thongKeDiemTB() {
        Map<String, Integer> thongKe = new HashMap<>();
        int gioi = 0, kha = 0, tb = 0, yeu = 0;
        for (SinhVienDTO sv : dao.getAll()) {
            double dtb = sv.getDiemTB();
            if (dtb >= 8.0)
                gioi++;
            else if (dtb >= 6.5)
                kha++;
            else if (dtb >= 5.0)
                tb++;
            else
                yeu++;
        }
        thongKe.put("Giỏi (>=8.0)", gioi);
        thongKe.put("Khá (6.5 - 7.9)", kha);
        thongKe.put("Trung Bình (5.0 - 6.4)", tb);
        thongKe.put("Yếu (<5.0)", yeu);
        return thongKe;
    }
}