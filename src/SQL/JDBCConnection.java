package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

    // useUnicode + characterEncoding: đảm bảo tiếng Việt có dấu không bị lỗi font
    // private static final String URL =
    // "jdbc:mysql://localhost:3306/quanlysinhvien"
    // + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String URL = "jdbc:mysql://localhost:3306/quanlysinhvien";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
