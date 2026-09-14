package bai8;

abstract class TaiLieuDTO {
    private String maTL;
    private String tenTL;
    private String namXB;

    public TaiLieuDTO(String maTL, String tenTL, String namXB) {
        this.maTL = maTL;
        this.tenTL = tenTL;
        this.namXB = namXB;
    }

    // setters and getters
    public String getMaTL() {
        return maTL;
    }

    public void setMaTL(String maTL) {
        this.maTL = maTL;
    }

    public String getTenTL() {
        return tenTL;
    }

    public void setTenTL(String tenTL) {
        this.tenTL = tenTL;
    }

    public String getNamXB() {
        return namXB;
    }

    public void setNamXB(String namXB) {
        this.namXB = namXB;
    }

}
