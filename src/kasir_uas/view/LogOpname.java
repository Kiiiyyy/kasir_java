package kasir_uas.view;

import kasir_uas.config.DBC;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class LogOpname extends JDialog {
    private JDateChooser dateChooser = new JDateChooser();
    private JButton btnTampilkan = new JButton("Tampilkan");
    private JButton btnSemua = new JButton("Tampilkan Semua");
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LogOpname() {
        setTitle("Riwayat Semua Stok Opname");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Panel atas untuk filter
        JPanel panelAtas = new JPanel(new FlowLayout());
        panelAtas.add(new JLabel("Pilih Tanggal:"));
        
        // Set ukuran date chooser
        dateChooser.setPreferredSize(new Dimension(150, 25));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        panelAtas.add(dateChooser);
        
        panelAtas.add(btnTampilkan);
        panelAtas.add(btnSemua);
        add(panelAtas, BorderLayout.NORTH);

        // Tabel
        model = new DefaultTableModel(
            new String[]{"Tanggal", "Nama Barang", "Stok Sistem", "Stok Aktual", "Selisih", "Keterangan"}, 0
        );
        table = new JTable(model);
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Aksi tombol
        btnTampilkan.addActionListener(e -> tampilkanDataByTanggal());
        btnSemua.addActionListener(e -> tampilkanSemuaData());
        
        // Tampilkan semua data saat pertama kali dibuka
        tampilkanSemuaData();
    }

    private void tampilkanDataByTanggal() {
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih tanggal terlebih dahulu!");
            return;
        }
        
        model.setRowCount(0); // clear table
        try {
            String tanggalFilter = dateFormat.format(dateChooser.getDate());
            
            Connection conn = DBC.getConnection();
            PreparedStatement ps = conn.prepareStatement("""
                SELECT so.*, b.nama_barang
                FROM stok_opname so
                JOIN barang b ON so.id_barang = b.id_barang
                WHERE DATE(so.tanggal) = ?
                ORDER BY so.tanggal DESC
                """);
            ps.setString(1, tanggalFilter);

            ResultSet rs = ps.executeQuery();
            int jumlahData = 0;
            
            while (rs.next()) {
                int sistem = rs.getInt("stok_sistem");
                int aktual = rs.getInt("stok_aktual");
                int selisih = aktual - sistem;

                model.addRow(new Object[]{
                    rs.getTimestamp("tanggal"),
                    rs.getString("nama_barang"),
                    sistem,
                    aktual,
                    selisih,
                    rs.getString("keterangan")
                });
                jumlahData++;
            }
            
            if (jumlahData == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data stok opname pada tanggal " + tanggalFilter);
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void tampilkanSemuaData() {
        model.setRowCount(0); // clear table
        try {
            Connection conn = DBC.getConnection();
            PreparedStatement ps = conn.prepareStatement("""
                SELECT so.*, b.nama_barang
                FROM stok_opname so
                JOIN barang b ON so.id_barang = b.id_barang
                ORDER BY so.tanggal DESC
                """);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sistem = rs.getInt("stok_sistem");
                int aktual = rs.getInt("stok_aktual");
                int selisih = aktual - sistem;

                model.addRow(new Object[]{
                    rs.getTimestamp("tanggal"),
                    rs.getString("nama_barang"),
                    sistem,
                    aktual,
                    selisih,
                    rs.getString("keterangan")
                });
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}