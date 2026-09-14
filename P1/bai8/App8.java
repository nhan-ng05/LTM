package bai8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App8 {
    // ma mau ANSI
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    // service
    // private Scanner scanner;
    private BufferedReader reader;

    // list tai lieu
    List<TaiLieuDTO> taiLieuList;

    // init
    public App8() {
        // this.scanner = new Scanner(System.in);
        // this.scanner.useLocale(Locale.US);
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.taiLieuList = new ArrayList<TaiLieuDTO>();

        // tai lieu mau de chay app
        this.taiLieuList.add(new SachDTO("S001", "Sach A", "2020", "Tac gia A", 100));
        this.taiLieuList.add(new TapChiDTO("T001", "Tap chi A", "2021", 1, 1));
        this.taiLieuList.add(new SachDTO("S002", "Sach B", "2019", "Tac gia B", 200));
        this.taiLieuList.add(new TapChiDTO("T002", "Tap chi B", "2022", 2, 2));
    }

    // 1. Them tai lieu
    public void themTaiLieu() throws IOException {
        System.out.println("Chon loai tai lieu: ");
        System.out.println("1. Sach");
        System.out.println("2. Tap chi");
        System.out.print("Nhap lua chon: ");
        int chon;
        try {
            chon = Integer.parseInt(this.reader.readLine());
        } catch (Exception e) {
            System.out.println("Lua chon khong hop le.");
            return;
        }
        if (chon != 1 && chon != 2) {
            System.out.println("Lua chon khong hop le.");
            return;
        } else if (chon == 1) {
            System.out.println("Nhap thong tin sach: ");
            System.out.print("Ma tai lieu: ");
            String maTL = this.reader.readLine();
            System.out.print("Ten tai lieu: ");
            String tenTL = this.reader.readLine();
            System.out.print("Nam xuat ban: ");
            String namXB = this.reader.readLine();
            System.out.print("Ten tac gia: ");
            String tenTG = this.reader.readLine();
            System.out.print("So trang: ");
            int soTrang;
            try {
                soTrang = Integer.parseInt(this.reader.readLine());
            } catch (Exception e) {
                System.out.println("So trang khong hop le.");
                return;
            }
            this.taiLieuList.add(new SachDTO(maTL, tenTL, namXB, tenTG, soTrang));
        } else if (chon == 2) {
            System.out.println("Nhap thong tin tap chi: ");
            System.out.print("Ma tai lieu: ");
            String maTL = this.reader.readLine();
            System.out.print("Ten tai lieu: ");
            String tenTL = this.reader.readLine();
            System.out.print("Nam xuat ban: ");
            String namXB = this.reader.readLine();
            System.out.print("So phat hanh: ");
            int soPhatHanh;
            try {
                soPhatHanh = Integer.parseInt(this.reader.readLine());
            } catch (Exception e) {
                System.out.println("So phat hanh khong hop le.");
                return;
            }
            System.out.print("Thang phat hanh: ");
            int thangPhatHanh;
            try {
                thangPhatHanh = Integer.parseInt(this.reader.readLine());
            } catch (Exception e) {
                System.out.println("Thang phat hanh khong hop le.");
                return;
            }
            this.taiLieuList.add(new TapChiDTO(maTL, tenTL, namXB, soPhatHanh, thangPhatHanh));
        }
    }

    // 2. Hien thi danh sach tai lieu
    public void hienThiDanhSachTaiLieu() {
        final String template = YELLOW + "Ma tai lieu: " + RESET;
        if (this.taiLieuList.isEmpty()) {
            System.out.println("Danh sach tai lieu rong.");
            return;
        }
        System.out.println("Danh sach tai lieu: ");
        for (TaiLieuDTO taiLieu : this.taiLieuList) {
            System.out.println(template + taiLieu.getMaTL() +
                    ", Ten tai lieu: " + taiLieu.getTenTL() +
                    ", Nam xuat ban: " + taiLieu.getNamXB());
            if (taiLieu instanceof SachDTO) {
                SachDTO sach = (SachDTO) taiLieu;
                System.out.println("Ten tac gia: " + sach.getTenTG() +
                        ", So trang: " + sach.getSoTrang());
            } else if (taiLieu instanceof TapChiDTO) {
                TapChiDTO tapChi = (TapChiDTO) taiLieu;
                System.out.println("So phat hanh: " + tapChi.getSoPhatHanh() +
                        ", Thang phat hanh: " + tapChi.getThangPhatHanh());
            }
        }
    }

    // 3. Tim theo ma tai lieu
    public void timTheoMaTaiLieu(String maTL) {
        boolean found = false;
        for (TaiLieuDTO taiLieu : this.taiLieuList) {
            if (taiLieu.getMaTL().equals(maTL)) {
                System.out.println("Ma tai lieu: " + taiLieu.getMaTL() +
                        ", Ten tai lieu: " + taiLieu.getTenTL() +
                        ", Nam xuat ban: " + taiLieu.getNamXB());
                if (taiLieu instanceof SachDTO) {
                    SachDTO sach = (SachDTO) taiLieu;
                    System.out.println("Ten tac gia: " + sach.getTenTG() +
                            ", So trang: " + sach.getSoTrang());
                } else if (taiLieu instanceof TapChiDTO) {
                    TapChiDTO tapChi = (TapChiDTO) taiLieu;
                    System.out.println("So phat hanh: " + tapChi.getSoPhatHanh() +
                            ", Thang phat hanh: " + tapChi.getThangPhatHanh());
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay tai lieu co ma: " + maTL);
        }
    }

    // 4. Xoa tai lieu
    public void xoaTaiLieu(String maTL) {
        boolean found = false;
        for (int i = 0; i < this.taiLieuList.size(); i++) {
            if (this.taiLieuList.get(i).getMaTL().equals(maTL)) {
                this.taiLieuList.remove(i);
                System.out.println("Da xoa tai lieu co ma: " + maTL);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay tai lieu co ma: " + maTL);
        }
    }

    // 5. Sap xep theo nam xuat ban
    public void sapXepTheoNamXuatBan() {
        this.taiLieuList.sort((tl1, tl2) -> tl1.getNamXB().compareTo(tl2.getNamXB()));
        System.out.println("Da sap xep danh sach tai lieu theo nam xuat ban.");
    }

    // 6. thong ke so luong tung loai tai lieu
    public void thongKeSoLuongTungLoaiTaiLieu() {
        int soLuongSach = 0;
        int soLuongTapChi = 0;
        for (TaiLieuDTO taiLieu : this.taiLieuList) {
            if (taiLieu instanceof SachDTO) {
                soLuongSach++;
            } else if (taiLieu instanceof TapChiDTO) {
                soLuongTapChi++;
            }
        }
        System.out.println("So luong sach: " + soLuongSach);
        System.out.println("So luong tap chi: " + soLuongTapChi);
    }

    public void runApp() throws IOException {
        int choice = 0;
        do {
            showMenu();
            try {
                // System.out.print("Nhap lua chon: ");
                choice = Integer.parseInt(this.reader.readLine());
            } catch (Exception e) {
                System.out.println("Lua chon khong hop le.");
                continue;
            }
            switch (choice) {
                case 1:
                    themTaiLieu();
                    break;
                case 2:
                    hienThiDanhSachTaiLieu();
                    break;
                case 3:
                    System.out.print("Nhap ma tai lieu can tim: ");
                    String maTL = this.reader.readLine();
                    timTheoMaTaiLieu(maTL);
                    break;
                case 4:
                    System.out.print("Nhap ma tai lieu can xoa: ");
                    String maTLXoa = this.reader.readLine();
                    xoaTaiLieu(maTLXoa);
                    break;
                case 5:
                    sapXepTheoNamXuatBan();
                    break;
                case 6:
                    thongKeSoLuongTungLoaiTaiLieu();
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
            }
        } while (choice != 0);
    }

    private void showMenu() {
        System.out.println("============= MENU ==============");
        System.out.println(BLUE + "1. Them tai lieu" + RESET);
        System.out.println(BLUE + "2. Hien thi danh sach tai lieu" + RESET);
        System.out.println(BLUE + "3. Tim theo ma tai lieu" + RESET);
        System.out.println(BLUE + "4. Xoa tai lieu" + RESET);
        System.out.println(BLUE + "5. Sap xep theo nam xuat ban" + RESET);
        System.out.println(BLUE + "6. Thong ke so luong tung loai tai lieu" + RESET);
        System.out.println(BLUE + "0. Thoat chuong trinh" + RESET);
        System.out.print(BLUE + "Chon chuc nang: " + RESET);
    }
}
