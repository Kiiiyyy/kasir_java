package kasir_uas.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kasir_uas.config.DBC;

public class TransaksiModel {
    private Connection conn;

    public TransaksiModel() {
        conn = DBC.getConnection(); // pastikan koneksi kamu jalan ya
    }

    public String simpanTransaksi(int idUser, int total, int bayar, int kembalian) {
        String idTransaksi = generateIdTransaksi();
        try {
            String sql = "INSERT INTO transaksi (id_transaksi, id_user, total, bayar, kembalian, tanggal) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, idTransaksi);
            ps.setInt(2, idUser);
            ps.setInt(3, total);
            ps.setInt(4, bayar);
            ps.setInt(5, kembalian);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan transaksi: " + e.getMessage());
        }
        return idTransaksi;
    }

    public void simpanDetailTransaksi(String idTransaksi, DetailTransaksi detail) {
        try {
            String sql = "INSERT INTO detail_transaksi (id_transaksi, id_barang, harga, qty, sub_total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            ps.setString(2, detail.getKodeBarang());
            ps.setInt(3, detail.getHarga());
            ps.setInt(4, detail.getJumlah());	
            ps.setInt(5, detail.getSubTotal());
            ps.executeUpdate();
            restokBarang(detail.getJumlah(), detail.getKodeBarang());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan detail: " + e.getMessage());
        }
    }
    
    public String generateIdTransaksi() {
    	String newId = null;
    	try {
        	// Ambil id_barang terakhir
            String lastIdQuery = "SELECT id_transaksi FROM transaksi ORDER BY id_transaksi DESC LIMIT 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(lastIdQuery);
            if (rs.next()) {	
                String lastId = rs.getString("id_transaksi"); // Misal: BR023
                int number = Integer.parseInt(lastId.substring(2)); // Ambil angka: 23
                number++; // Tambah 1
                newId = String.format("TR%03d", number); // Jadi BR024
            } else {
                newId = "TR001"; // Kalau belum ada data
            }
            return newId;
    	}catch (Exception e) {
    		JOptionPane.showInputDialog("Gagal mengambil data: " + e.getMessage());
    		return newId;
    	}
    }    
    
    public void restokBarang(int qty, String id_barang) {
    	try {
            String sql = "UPDATE `barang` SET `stok`=`stok` - ? WHERE id_barang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, qty);
            ps.setString(2, id_barang);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal restok barang: " + e.getMessage());
        }
    }
    
   
    
}


