package kasir_uas.model;
import kasir_uas.config.*;
import java.sql.*;
import java.util.ArrayList;

public class LaporanModel {
    private Connection conn;

    public LaporanModel() {
        this.conn = DBC.getConnection();
    }

    public ArrayList<String[]> getLaporan(String jenis) throws SQLException {
        String query = "";
        switch (jenis) {
            case "Laporan Harian":
                query = "SELECT * FROM view_laporan_harian";
                break;
            case "Laporan Mingguan":
                query = "SELECT * FROM view_laporan_mingguan";
                break;
            case "Laporan Bulanan":
                query = "SELECT * FROM view_laporan_bulanan";
                break;
        }

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        ArrayList<String[]> data = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        // Tambahkan nama kolom
        String[] header = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            header[i - 1] = meta.getColumnName(i);
        }
        data.add(header);

        // Tambahkan data
        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getString(i);
            }
            data.add(row);
        }

        rs.close();
        stmt.close();
        return data;
    }
}
