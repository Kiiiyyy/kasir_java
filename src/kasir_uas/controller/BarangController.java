package kasir_uas.controller;


import  kasir_uas.config.DBC;
import  kasir_uas.model.Barang;
import  kasir_uas.view.BarangView;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class BarangController {
    private BarangView view;
    private Connection conn;

    public BarangController(BarangView view) {
        this.view = view;
        this.conn = DBC.getConnection();

        loadBarang();

        view.btnTambah.addActionListener(e -> tambahBarang());
        view.btnUbah.addActionListener(e -> ubahBarang());
        view.btnHapus.addActionListener(e -> hapusBarang());

        view.tableBarang.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.tableBarang.getSelectedRow();
                if (row >= 0) {
                    view.tfNama.setText(view.tableModel.getValueAt(row, 1).toString());
                    view.tfSatuan.setText(view.tableModel.getValueAt(row, 2).toString());
                    view.tfStok.setText(view.tableModel.getValueAt(row, 3).toString());
                    view.tfHarga.setText(view.tableModel.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void loadBarang() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM barang");
            view.tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getInt("stok"),
                    rs.getDouble("harga")
                };
                view.tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tambahBarang() {
        try {
        	// Ambil id_barang terakhir
            String lastIdQuery = "SELECT id_barang FROM barang ORDER BY id_barang DESC LIMIT 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(lastIdQuery);
            String newId;
            if (rs.next()) {
                String lastId = rs.getString("id_barang"); // Misal: BR023
                int number = Integer.parseInt(lastId.substring(2)); // Ambil angka: 23
                number++; // Tambah 1
                newId = String.format("BR%03d", number); // Jadi BR024
            } else {
                newId = "BR001"; // Kalau belum ada data
            }
        	
            String sql = "INSERT INTO barang (id_barang, nama_barang, satuan, stok, harga, tanggal_input) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newId);
            ps.setString(2, view.tfNama.getText());
            ps.setString(3, view.tfSatuan.getText());
            ps.setInt(4, Integer.parseInt(view.tfStok.getText()));
            ps.setDouble(5, Double.parseDouble(view.tfHarga.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Barang berhasil ditambahkan.");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal menambahkan barang: " + e.getMessage());
        }
    }

    private void ubahBarang() {
        int row = view.tableBarang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Pilih barang terlebih dahulu.");
            return;
        }

        try {
            String id = (String) view.tableModel.getValueAt(row, 0);
            String sql = "UPDATE barang SET nama_barang=?, satuan=?, stok=?, harga=? WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, view.tfNama.getText());
            ps.setString(2, view.tfSatuan.getText());
            ps.setInt(3, Integer.parseInt(view.tfStok.getText()));
            ps.setDouble(4, Double.parseDouble(view.tfHarga.getText()));
            ps.setString(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Barang berhasil diubah.");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal mengubah barang: " + e.getMessage());
        }
    }

    private void hapusBarang() {
        int row = view.tableBarang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Pilih barang terlebih dahulu.");
            return;
        }

        try {
            String id = (String) view.tableModel.getValueAt(row, 0);
            String sql = "DELETE FROM barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Barang berhasil dihapus.");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal menghapus barang: " + e.getMessage());
        }
    }
}
