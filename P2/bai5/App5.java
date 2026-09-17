package P2.bai5;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App5 {

    public App5() {
        Map<String, String> danhBa = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- QUẢN LÝ DANH BẠ ---");
            System.out.println("1. Thêm liên hệ");
            System.out.println("2. Tìm theo số điện thoại");
            System.out.println("3. Cập nhật tên");
            System.out.println("4. Xóa liên hệ");
            System.out.println("5. Hiển thị toàn bộ danh bạ");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int chon = sc.nextInt();
            sc.nextLine(); // bỏ qua newline

            switch (chon) {
                case 1:
                    System.out.print("Nhập số điện thoại: ");
                    String sdt = sc.nextLine();
                    System.out.print("Nhập họ tên: ");
                    String ten = sc.nextLine();
                    danhBa.put(sdt, ten);
                    System.out.println("Đã thêm liên hệ!");
                    break;

                case 2:
                    System.out.print("Nhập số điện thoại cần tìm: ");
                    sdt = sc.nextLine();
                    if (danhBa.containsKey(sdt)) {
                        System.out.println("Số " + sdt + " thuộc về " + danhBa.get(sdt));
                    } else {
                        System.out.println("Không tìm thấy!");
                    }
                    break;

                case 3:
                    System.out.print("Nhập số điện thoại cần cập nhật: ");
                    sdt = sc.nextLine();
                    if (danhBa.containsKey(sdt)) {
                        System.out.print("Nhập tên mới: ");
                        ten = sc.nextLine();
                        danhBa.put(sdt, ten);
                        System.out.println("Đã cập nhật!");
                    } else {
                        System.out.println("Không tìm thấy!");
                    }
                    break;

                case 4:
                    System.out.print("Nhập số điện thoại cần xóa: ");
                    sdt = sc.nextLine();
                    if (danhBa.remove(sdt) != null) {
                        System.out.println("Đã xóa liên hệ!");
                    } else {
                        System.out.println("Không tìm thấy!");
                    }
                    break;

                case 5:
                    System.out.println("Danh bạ:");
                    for (Map.Entry<String, String> entry : danhBa.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }
                    break;

                case 0:
                    System.out.println("Thoát chương trình.");
                    sc.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");

            }
        }
    }
}