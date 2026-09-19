# Quản lý sinh viên – Java Swing + MySQL

## Cấu trúc
```
src/
├─ Main.java                  điểm chạy chương trình
├─ SQL/JDBCConnection.java    kết nối MySQL (sửa USER / PASSWORD ở đây)
├─ dto/SinhVienDTO.java       đối tượng sinh viên
├─ dao/SinhVienDAO.java       truy vấn SQL (getAll / insert / update / delete)
├─ bus/SinhVienBUS.java       nghiệp vụ (App cũ): validate, tìm kiếm, sắp xếp, thống kê
└─ ui/                        giao diện
   ├─ MainFrame.java          cửa sổ chính
   ├─ Theme.java              màu sắc, font
   ├─ UIKit.java              nút / ô nhập / thẻ tự vẽ bo góc
   ├─ StatCard.java           thẻ thống kê
   ├─ SinhVienTableModel.java model của bảng
   └─ TableStyle.java         kiểu dáng bảng + badge xếp loại
database/quanlysinhvien.sql   tạo DB + 20 sinh viên mẫu
```

## Cách chạy
1. Chạy `database/quanlysinhvien.sql` trong MySQL (Workbench / phpMyAdmin / XAMPP).
2. Tải **MySQL Connector/J** (file `mysql-connector-j-x.y.z.jar`) và thêm vào Libraries / classpath.
3. (Tùy chọn, nên làm) thêm **FlatLaf** (`flatlaf-x.y.jar`) – app tự nhận và giao diện đẹp hơn.
4. Sửa `USER`, `PASSWORD` trong `SQL/JDBCConnection.java` cho đúng máy bạn.
5. Chạy `Main`.

Dòng lệnh (Windows dùng `;` thay cho `:`):
```
javac -encoding UTF-8 -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" Main
```

> IDE phải để encoding **UTF-8** (Eclipse: Project > Properties > Resource > Text file encoding)
> nếu không tiếng Việt trong code sẽ bị lỗi font.
