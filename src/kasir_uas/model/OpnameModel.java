package kasir_uas.model;

import kasir_uas.config.DBC;
import java.sql.*;
import java.util.ArrayList;

public class OpnameModel {
    private Connection conn;

    public OpnameModel() {
        conn = DBC.getConnection();
    }

    public ArrayList<Object[]> getDataBarang() {
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM barang");
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("stok"),
                    "", "", // stok fisik dan keterangan (diisi user)
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void simpanOpname(String idBarang, int stokSistem, int stokFisik, String keterangan) throws SQLException {
        int selisih = stokFisik - stokSistem;

        String sql = "INSERT INTO stok_opname (id_user, id_barang, stok_sistem, stok_aktual, keterangan) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 1); // user tunggal
        ps.setString(2, idBarang);
        ps.setInt(3, stokSistem);
        ps.setInt(4, stokFisik);
        ps.setString(5, keterangan);
        ps.executeUpdate();

        PreparedStatement ps2 = conn.prepareStatement("UPDATE barang SET stok=? WHERE id_barang=?");
        ps2.setInt(1, stokFisik);
        ps2.setString(2, idBarang);
        ps2.executeUpdate();
    }
}
