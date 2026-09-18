
public class SinhVienDTO {
    private String maSV;
    private String hoTen;
    private Double diemToan;
    private Double diemVan;
    private Double diemAnh;
    private Double diemTB;

    public SinhVienDTO() {
        this.maSV = null;
        this.hoTen = null;
        this.diemAnh = null;
        this.diemToan = null;
        this.diemVan = null;
        this.diemTB = null;
    }

    public SinhVienDTO(String maSV, String hoTen, double diemToan, double diemAnh, double diemVan) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.diemAnh = diemAnh;
        this.diemToan = diemToan;
        this.diemVan = diemVan;
        this.diemTB = (diemToan + diemAnh + diemVan) / 3;
    }

    // setter and getter
    public String getMaSV() {
        return this.maSV;
    }

    public String getHoTen() {
        return this.hoTen;
    }

    public Double getDiemToan() {
        return this.diemToan;
    }

    public Double getDiemAnh() {
        return this.diemAnh;
    }

    public Double getDiemVan() {
        return this.diemVan;
    }

    public Double getDiemTB() {
        return this.diemTB;
    }

    //
    public void update(SinhVienDTO sinhVienDTO) {
        this.maSV = sinhVienDTO.maSV;
        this.hoTen = sinhVienDTO.hoTen;
        this.diemAnh = sinhVienDTO.diemAnh;
        this.diemToan = sinhVienDTO.diemToan;
        this.diemVan = sinhVienDTO.diemVan;
    }
}