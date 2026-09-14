package bai8;

public class TapChiDTO extends TaiLieuDTO {
    private int soPhatHanh;
    private int thangPhatHanh;

    public TapChiDTO(String maTL, String tenTL, String namXB, int soPhatHanh, int thangPhatHanh) {
        super(maTL, tenTL, namXB);
        this.soPhatHanh = soPhatHanh;
        this.thangPhatHanh = thangPhatHanh;
    }

    // setters and getters
    public int getSoPhatHanh() {
        return soPhatHanh;
    }

    public void setSoPhatHanh(int soPhatHanh) {
        this.soPhatHanh = soPhatHanh;
    }

    public int getThangPhatHanh() {
        return thangPhatHanh;
    }

    public void setThangPhatHanh(int thangPhatHanh) {
        this.thangPhatHanh = thangPhatHanh;
    }

}
