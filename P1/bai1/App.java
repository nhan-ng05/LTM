package bai1;

import java.util.Scanner;

public class App {
    private Scanner scanner;
    private int n;

    // constructor
    public App() {
        // initialize scanner
        scanner = new Scanner(System.in);

        // get input from terminal
        System.out.print("Nhap ban kinh hinh tron: ");
        n = scanner.nextInt();
    }

    // tinh chu vi hinh tron
    public double tinhChuVi() {
        return 2 * Math.PI * n;
    }

    // tinh dien tich hinh tron
    public double tinhDienTich() {
        return Math.PI * n * n;
    }

    // kiem tra so nguyen to
    public boolean ktSoNguyenTo() {
        // nhap so
        System.out.print("Nhap so nguyen de kiem tra snt: ");
        int so = scanner.nextInt();
        if (so < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(so); i++) {
            if (so % i == 0) {
                return false;
            }
        }
        return true;

    }

    // in bang cuu chuong
    public void inBangCuuChuong() {
        System.out.print("Nhap so nguyen de in bang cuu chuong: ");
        int so = scanner.nextInt();
        for (int i = 1; i <= 10; i++) {
            System.out.println(so + " x " + i + " = " + (so * i));
        }
    }
}
