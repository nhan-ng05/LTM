package dao;

import SQL.JDBCConnection;
import dto.SinhVienDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Lớp duy nhất nói chuyện trực tiếp với bảng `sinhvien` trong MySQL. */
public class SinhVienDAO {

    private static final String SELECT_ALL =
            "SELECT maSV, hoTen, diemToan, diemVan, diemAnh FROM sinhvien ORDER BY maSV";

    private static final String INSERT =
            "INSERT INTO sinhvien (maSV, hoTen, diemToan, diemVan, diemAnh) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE sinhvien SET hoTen = ?, diemToan = ?, diemVan = ?, diemAnh = ? WHERE maSV = ?";

    private static final String DELETE = "DELETE FROM sinhvien WHERE maSV = ?";

    /** Lấy toàn bộ sinh viên. */
    public List<SinhVienDTO> getAll() throws SQLException {
        List<SinhVienDTO> list = new ArrayList<>();
        try (Connection con = JDBCConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean insert(SinhVienDTO sv) throws SQLException {
        try (Connection con = JDBCConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, sv.getMaSV());
            ps.setString(2, sv.getHoTen());
            ps.setDouble(3, sv.getDiemToan());
            ps.setDouble(4, sv.getDiemVan());
            ps.setDouble(5, sv.getDiemAnh());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(SinhVienDTO sv) throws SQLException {
        try (Connection con = JDBCConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, sv.getHoTen());
            ps.setDouble(2, sv.getDiemToan());
            ps.setDouble(3, sv.getDiemVan());
            ps.setDouble(4, sv.getDiemAnh());
            ps.setString(5, sv.getMaSV());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maSV) throws SQLException {
        try (Connection con = JDBCConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        }
    }

    /** Chuyển 1 dòng ResultSet thành 1 SinhVienDTO. */
    private SinhVienDTO mapRow(ResultSet rs) throws SQLException {
        return new SinhVienDTO(
                rs.getString("maSV"),
                rs.getString("hoTen"),
                rs.getDouble("diemToan"),
                rs.getDouble("diemVan"),
                rs.getDouble("diemAnh"));
    }
}
