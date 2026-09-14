package bai8;

public class SachDTO extends TaiLieuDTO {
    private String tenTG;
    private int soTrang;

    public SachDTO(String maTL, String tenTL, String namXB, String tenTG, int soTrang) {
        super(maTL, tenTL, namXB);
        this.tenTG = tenTG;
        this.soTrang = soTrang;
    }

    // setters and getters
    public String getTenTG() {
        return tenTG;
    }

    public void setTenTG(String tenTG) {
        this.tenTG = tenTG;
    }

    public int getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(int soTrang) {
        this.soTrang = soTrang;
    }

}
