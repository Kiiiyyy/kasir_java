package kasir_uas.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import kasir_uas.config.DBC;

public class BarangDAO {

    private Connection conn;

    public List<Barang> searchBarang(String keyword) {
    	this.conn = DBC.getConnection();
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE id_barang LIKE ? OR nama_barang LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Barang barang = new Barang();
                barang.setIdBarang(rs.getString("id_barang"));
                barang.setNamaBarang(rs.getString("nama_barang"));
                barang.setHarga(rs.getInt("harga"));
                barang.setStok(rs.getInt("stok"));
                barang.setSatuan(rs.getString("satuan"));
                list.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tambahan method lain seperti insert, update, delete bisa ditambahkan nanti
}

