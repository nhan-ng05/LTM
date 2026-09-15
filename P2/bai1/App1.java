package P2.bai1;

import java.io.BufferedReader;
import java.io.IOException;

public class App1 {
    private int number_a;
    private int number_b;
    private int result;
    private BufferedReader br;

    public App1() {
        this.br = new BufferedReader(new java.io.InputStreamReader(System.in));
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void runApp() {

        try {
            System.out.println("Nhap so nguyen a: ");
            this.number_a = Integer.parseInt(this.br.readLine());
            System.out.println("Nhap so nguyen b: ");
            this.number_b = Integer.parseInt(this.br.readLine());
        } catch (IOException e) {
            System.out.println("Loi nhap du lieu!");
        }

        if (isInteger(String.valueOf(this.number_a)) && isInteger(String.valueOf(this.number_b))) {
            this.result = this.number_a / this.number_b;
            System.out.println("Ket qua: " + this.result);
        } else if (number_b == 0) {
            System.out.println("Khong the chia cho 0!");
        } else {
            System.out.println("Vui long nhap so nguyen!");
        }
    }
}
