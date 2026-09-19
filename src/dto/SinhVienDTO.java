package dto;

import java.util.Objects;

/**
 * Đối tượng truyền dữ liệu của sinh viên.
 * Thứ tự điểm ở mọi nơi (constructor, DB, giao diện) thống nhất: Toán - Văn - Anh.
 */
public class SinhVienDTO {

    public static final String GIOI = "Giỏi";
    public static final String KHA = "Khá";
    public static final String TRUNG_BINH = "Trung bình";
    public static final String YEU = "Yếu";

    private String maSV;
    private String hoTen;
    private double diemToan;
    private double diemVan;
    private double diemAnh;

    public SinhVienDTO() {
    }

    public SinhVienDTO(String maSV, String hoTen, double diemToan, double diemVan, double diemAnh) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.diemToan = diemToan;
        this.diemVan = diemVan;
        this.diemAnh = diemAnh;
    }

    // ---------- getter / setter ----------
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public double getDiemToan() {
        return diemToan;
    }

    public void setDiemToan(double diemToan) {
        this.diemToan = diemToan;
    }

    public double getDiemVan() {
        return diemVan;
    }

    public void setDiemVan(double diemVan) {
        this.diemVan = diemVan;
    }

    public double getDiemAnh() {
        return diemAnh;
    }

    public void setDiemAnh(double diemAnh) {
        this.diemAnh = diemAnh;
    }

    /**
     * Điểm trung bình luôn được tính lại từ 3 môn (làm tròn 2 chữ số),
     * nên không bao giờ bị "cũ" sau khi sửa điểm.
     */
    public double getDiemTB() {
        double tb = (diemToan + diemVan + diemAnh) / 3.0;
        return Math.round(tb * 100.0) / 100.0;
    }

    /** Xếp loại: Giỏi >= 8, Khá >= 6.5, Trung bình >= 5, còn lại Yếu. */
    public String getXepLoai() {
        double tb = getDiemTB();
        if (tb >= 8) {
            return GIOI;
        }
        if (tb >= 6.5) {
            return KHA;
        }
        if (tb >= 5) {
            return TRUNG_BINH;
        }
        return YEU;
    }

    /** Cập nhật thông tin từ đối tượng khác. Mã SV là khóa chính nên KHÔNG bị đổi. */
    public void update(SinhVienDTO other) {
        this.hoTen = other.hoTen;
        this.diemToan = other.diemToan;
        this.diemVan = other.diemVan;
        this.diemAnh = other.diemAnh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SinhVienDTO)) {
            return false;
        }
        return maSV != null && maSV.equalsIgnoreCase(((SinhVienDTO) o).maSV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSV == null ? null : maSV.toLowerCase());
    }

    @Override
    public String toString() {
        return maSV + " - " + hoTen + " (ĐTB: " + getDiemTB() + ")";
    }
}
