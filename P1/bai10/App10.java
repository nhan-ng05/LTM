package bai10;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class App10 {
    // ma mau ANSI
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";

    private Scanner scanner;
    private ArrayList<SinhVienDTO> danhSachSinhVien;

    // init
    public App10() {
        this.scanner = new Scanner(System.in);
        this.scanner.useLocale(Locale.US);
        this.danhSachSinhVien = new ArrayList<>();
    }

    // 1. nhap danh sach sinh vien
    private void nhapDanhSachSinhVien() {
        System.out.println("Nhap thong tin sinh vien thu :");
        System.out.print("Ho ten: ");
        String hoTen = scanner.nextLine();
        System.out.print("Diem Toan: ");
        Double diemToan = scanner.nextDouble();
        System.out.print("Diem Lap Trinh: ");
        Double diemLapTrinh = scanner.nextDouble();
        scanner.nextLine();

        // them sinh vien vao danh sach
        this.danhSachSinhVien.add(new SinhVienDTO(hoTen, diemToan, diemLapTrinh));
        System.out.println("Da nhap xong danh sach sinh vien.");
    }

    // 2. hien thi danh sach sinh vien
    private void hienThiDanhSachSinhVien() {
        if (this.danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien rong.");
            return;
        }
        System.out.println("Danh sach sinh vien:");
        for (SinhVienDTO sinhVien : this.danhSachSinhVien) {
            System.out.println("Ho ten: " + sinhVien.getHoTen() +
                    ", Diem Toan: " + sinhVien.getDiemToan() +
                    ", Diem Lap Trinh: " + sinhVien.getDiemLapTrinh() +
                    ", Diem Trung Binh: " + sinhVien.getDiemTrungBinh());
        }
    }

    // 3. tinh diem trung binh cua tung sinh vien (da tinh trong constructor va
    // setter roi nen khong can tinh lai)

    // 4. tim sinh vien co diem trung binh cao nhat
    private void sinhVienCoDiemTrungBinhCaoNhat() {
        if (this.danhSachSinhVien.isEmpty()) {
            return;
        }

        SinhVienDTO sinhVienCaoNhat = this.danhSachSinhVien.get(0);
        for (SinhVienDTO sinhVien : this.danhSachSinhVien) {
            if (sinhVien.getDiemTrungBinh() > sinhVienCaoNhat.getDiemTrungBinh()) {
                sinhVienCaoNhat = sinhVien;
            }
        }
        System.out.println("Sinh vien co diem trung binh cao nhat:");
        System.out.println("Ho ten: " + sinhVienCaoNhat.getHoTen() +
                ", Diem Toan: " + sinhVienCaoNhat.getDiemToan() +
                ", Diem Lap Trinh: " + sinhVienCaoNhat.getDiemLapTrinh() +
                ", Diem Trung Binh: " + sinhVienCaoNhat.getDiemTrungBinh());
    }

    // 5. sap xep danh sach sinh vien theo diem trung binh giam dan
    private void sapXepDiemTrungBinhGiamDan() {
        this.danhSachSinhVien.sort(
                (sinhVien1, sinhVien2) -> Double.compare(sinhVien2.getDiemTrungBinh(), sinhVien1.getDiemTrungBinh()));
        System.out.println("Da sap xep danh sach sinh vien theo diem trung binh giam dan.");
    }

    // 6. tim sinh vien theo ho ten
    private void timSinhVienTheoHoTen(String hoTen) {
        boolean found = false;
        for (SinhVienDTO sinhVien : this.danhSachSinhVien) {
            if (sinhVien.getHoTen().equalsIgnoreCase(hoTen)) {
                System.out.println("Thong tin sinh vien:");
                System.out.println("Ho ten: " + sinhVien.getHoTen() +
                        ", Diem Toan: " + sinhVien.getDiemToan() +
                        ", Diem Lap Trinh: " + sinhVien.getDiemLapTrinh() +
                        ", Diem Trung Binh: " + sinhVien.getDiemTrungBinh());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay sinh vien co ho ten: " + hoTen);
        }
    }

    public void runApp() {
        boolean isRunning = true;
        while (isRunning) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    nhapDanhSachSinhVien();
                    break;
                case 2:
                    hienThiDanhSachSinhVien();
                    break;
                case 3:
                    // diem trung binh da tinh trong constructor va setter
                    System.out.println("Diem trung binh cua tung sinh vien da duoc tinh.");
                    break;
                case 4:
                    sinhVienCoDiemTrungBinhCaoNhat();
                    break;
                case 5:
                    sapXepDiemTrungBinhGiamDan();
                    break;
                case 6:
                    System.out.print("Nhap ho ten sinh vien can tim: ");
                    String hoTen = scanner.nextLine();
                    timSinhVienTheoHoTen(hoTen);
                    break;
                case 0:
                    isRunning = false;
                    System.out.println("Thoat chuong trinh.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
            }
        }
    }

    private void showMenu() {
        System.out.println("============= MENU ==============");
        System.out.println(BLUE + "1. Nhap danh sach sinh vien" + RESET);
        System.out.println(BLUE + "2. Hien thi danh sach sinh vien" + RESET);
        System.out.println(BLUE + "3. Tinh diem trung binh cua tung sinh vien" + RESET);
        System.out.println(BLUE + "4. Tim sinh vien co diem trung binh cao nhat" + RESET);
        System.out.println(BLUE + "5. Sap xep danh sach sinh vien theo diem trung binh giam dan" + RESET);
        System.out.println(BLUE + "6. Tim sinh vien theo ho ten" + RESET);
        System.out.println(BLUE + "0. Thoat chuong trinh" + RESET);
        System.out.print(BLUE + "Chon chuc nang: " + RESET);
    }
}
