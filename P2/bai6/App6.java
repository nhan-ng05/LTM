package P2.bai6;

import java.util.LinkedList;
import java.util.Queue;

public class App6 {
    private Queue<String> queue;

    public App6() {
        this.queue = new LinkedList<String>();
    }

    private void addCustomer(String customerName) {
        queue.add(customerName);
        System.out.println("Khach hang " + customerName + " da duoc them vao hang doi.");
    }

    private void serveCustomer() {
        if (queue.isEmpty()) {
            System.out.println("Khong co khach hang nao trong hang doi.");
        } else {
            String servedCustomer = queue.poll();
            System.out.println("Phuc vu khach hang: " + servedCustomer);
        }
    }

    private void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("Khong co khach hang nao trong hang doi.");
        } else {
            System.out.println("Danh sach khach hang dang cho:");
            for (String customer : queue) {
                System.out.println("- " + customer);
            }
        }
    }

    private void displayQueueSize() {
        System.out.println("So luong khach hang con lai trong hang doi: " + queue.size());
    }

    public void runApp() {

        int choice;
        do {
            showMenu();
            choice = Integer.parseInt(System.console().readLine("Nhap lua chon cua ban: "));

            switch (choice) {
                case 1:
                    String customerName = System.console().readLine("Nhap ten khach hang: ");
                    addCustomer(customerName);
                    break;
                case 2:
                    serveCustomer();
                    break;
                case 3:
                    displayQueue();
                    break;
                case 4:
                    displayQueueSize();
                    break;
                case 0:
                    System.out.println("Thoát chương trình.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long thu lai.");
            }
        } while (choice != 0);
    }

    private void showMenu() {
        System.out.println("\n--- QUẢN LÝ HÀNG ĐỢI ---");
        System.out.println("1. Thêm khach hang vào hàng đợi");
        System.out.println("2. Phục vụ khách hàng dau tien");
        System.out.println("3. Hiển thị khach hang dang cho");
        System.out.println("4. Hien thi so luong khach con lai");
        System.out.println("0. Thoát");
    }
}
