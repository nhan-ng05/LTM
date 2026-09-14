package bai4;

import java.util.Scanner;

public class App4 {
    private Scanner scanner;
    private int n;

    public App4() {
        // initialize the scanner and n
        this.scanner = new Scanner(System.in);
        System.out.print("Nhap so phan tu n: ");
        this.n = scanner.nextInt();
        this.inMang(this.nhapMang());
    }

    // nhap mang so nguyen
    private int[] nhapMang() {
        int[] arr = new int[n];
        System.out.print("Nhap mang so nguyen: ");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        return arr;
    }

    // in mang so nguyen
    private void inMang(int[] arr) {
        System.out.print("Mang so nguyen: ");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println(); // Print a new line after the array
    }
}
