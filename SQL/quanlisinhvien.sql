-- ==========================================
-- DATABASE QUẢN LÝ SINH VIÊN
-- ==========================================

CREATE DATABASE IF NOT EXISTS quanly_sinhvien
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE quanly_sinhvien;


-- ==========================================
-- XÓA BẢNG CŨ NẾU ĐÃ TỒN TẠI
-- ==========================================

DROP TABLE IF EXISTS sinhvien;


-- ==========================================
-- TẠO BẢNG SINH VIÊN
-- ==========================================

CREATE TABLE sinhvien (
    maSV VARCHAR(20) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    diemToan DECIMAL(4,2),
    diemVan DECIMAL(4,2),
    diemAnh DECIMAL(4,2)
);


-- ==========================================
-- THÊM 20 SINH VIÊN
-- ==========================================

INSERT INTO sinhvien
    (maSV, hoTen, diemToan, diemVan, diemAnh)
VALUES
    ('SV001', 'Nguyễn Văn An',      8.50, 7.00, 8.00),
    ('SV002', 'Trần Thị Bình',     7.00, 8.50, 7.50),
    ('SV003', 'Lê Hoàng Nam',      9.00, 7.50, 8.50),
    ('SV004', 'Phạm Minh Anh',     6.50, 8.00, 7.00),
    ('SV005', 'Hoàng Quốc Bảo',    8.00, 6.50, 9.00),
    ('SV006', 'Võ Thị Lan',        7.50, 9.00, 8.00),
    ('SV007', 'Đặng Minh Tuấn',    6.00, 6.50, 7.00),
    ('SV008', 'Bùi Ngọc Mai',      9.50, 8.50, 9.00),
    ('SV009', 'Đỗ Thành Công',     7.00, 7.50, 6.50),
    ('SV010', 'Nguyễn Thị Hương',  8.50, 9.00, 8.50),

    ('SV011', 'Trương Gia Huy',    5.50, 6.00, 7.50),
    ('SV012', 'Phan Thị Ngọc',     8.00, 8.50, 7.00),
    ('SV013', 'Huỳnh Minh Đức',    9.00, 8.00, 9.50),
    ('SV014', 'Mai Thanh Tùng',    6.50, 7.00, 6.00),
    ('SV015', 'Ngô Thị Trang',     7.50, 8.00, 8.50),
    ('SV016', 'Đinh Quốc Khánh',   8.50, 7.50, 7.00),
    ('SV017', 'Vũ Minh Khang',     9.00, 9.00, 8.00),
    ('SV018', 'Đoàn Thị Yến',      6.00, 7.50, 7.00),
    ('SV019', 'Lý Hoàng Long',     7.50, 6.50, 8.50),
    ('SV020', 'Dương Thùy Linh',   8.00, 9.50, 9.00);