
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import SQL.JDBCConnection;

public class SinhVienDAO {

    public List<SinhVienDTO> getAll() {
        List<SinhVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien";
        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new SinhVienDTO(
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        rs.getDouble("diemToan"),
                        rs.getDouble("diemAnh"),
                        rs.getDouble("diemVan")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(SinhVienDTO sv) {
        String sql = "INSERT INTO sinhvien (maSV, hoTen, diemToan, diemVan, diemAnh) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getMaSV());
            ps.setString(2, sv.getHoTen());
            ps.setDouble(3, sv.getDiemToan());
            ps.setDouble(4, sv.getDiemVan());
            ps.setDouble(5, sv.getDiemAnh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SinhVienDTO sv) {
        String sql = "UPDATE sinhvien SET hoTen=?, diemToan=?, diemVan=?, diemAnh=? WHERE maSV=?";
        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setDouble(2, sv.getDiemToan());
            ps.setDouble(3, sv.getDiemVan());
            ps.setDouble(4, sv.getDiemAnh());
            ps.setString(5, sv.getMaSV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maSV) {
        String sql = "DELETE FROM sinhvien WHERE maSV=?";
        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SinhVienDTO> search(String keyword) {
        List<SinhVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien WHERE maSV LIKE ? OR hoTen LIKE ?";
        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SinhVienDTO(
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        rs.getDouble("diemToan"),
                        rs.getDouble("diemAnh"),
                        rs.getDouble("diemVan")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}