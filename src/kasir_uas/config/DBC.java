package kasir_uas.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBC {
	private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/kasir_db";
                String user = "root";
                String pass = "password_baru";
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Koneksi sukses!");
            } catch (Exception e) {
                System.err.println("Koneksi gagal: " + e.getMessage());
            }
        }
        return conn;
    }
}
