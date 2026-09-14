package bai10;

public class SinhVienDTO {
    private String hoTen;
    private Double diemToan;
    private Double diemLapTrinh;
    private Double diemTrungBinh;

    public SinhVienDTO(String maSV, Double diemToan, Double diemLapTrinh) {
        this.hoTen = maSV;
        this.diemToan = diemToan;
        this.diemLapTrinh = diemLapTrinh;
        this.diemTrungBinh = (diemToan + diemLapTrinh) / 2;
    }

    // set data
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setDiemToan(double diemToan) {
        this.diemToan = diemToan;

        if (this.diemLapTrinh != null) {
            this.diemTrungBinh = (this.diemToan + this.diemLapTrinh) / 2;
        } else {
            this.diemTrungBinh = null;
        }
    }

    public void setDiemLapTrinh(double diemLapTrinh) {
        this.diemLapTrinh = diemLapTrinh;

        if (this.diemToan != null) {
            this.diemTrungBinh = (this.diemToan + this.diemLapTrinh) / 2;
        } else {
            this.diemTrungBinh = null;
        }
    }

    // get data
    public String getHoTen() {
        return hoTen;
    }

    public double getDiemToan() {
        return diemToan;
    }

    public double getDiemLapTrinh() {
        return diemLapTrinh;
    }

    public double getDiemTrungBinh() {
        return diemTrungBinh;
    }
}
