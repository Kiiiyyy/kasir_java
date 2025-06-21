package kasir_uas.view;

import kasir_uas.config.DBC;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class LogTransaksi extends JDialog {
    private JDateChooser dateChooser = new JDateChooser();
    private JButton btnTampilkan = new JButton("Tampilkan");
    private JButton btnSemua = new JButton("Tampilkan Semua");
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LogTransaksi() {
        setTitle("Riwayat Transaksi");
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
            new String[]{"ID Transaksi", "Tanggal", "Total", "Bayar", "Kembalian"}, 0
        );
        table = new JTable(model);
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Format angka untuk kolom total, bayar, kembalian
        table.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer());
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel bawah untuk info
        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblInfo = new JLabel("Klik dua kali pada baris untuk melihat detail transaksi");
        lblInfo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        panelBawah.add(lblInfo);
        add(panelBawah, BorderLayout.SOUTH);

        // Aksi tombol
        btnTampilkan.addActionListener(e -> tampilkanDataByTanggal());
        btnSemua.addActionListener(e -> tampilkanSemuaData());
        
        // Double click untuk detail
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String idTransaksi = table.getValueAt(row, 0).toString();
                        tampilkanDetailTransaksi(idTransaksi);
                    }
                }
            }
        });
        
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
                SELECT * FROM transaksi 
                WHERE DATE(tanggal) = ?
                ORDER BY tanggal DESC
                """);
            ps.setString(1, tanggalFilter);

            ResultSet rs = ps.executeQuery();
            int jumlahData = 0;
            int totalHarian = 0;
            
            while (rs.next()) {
                int total = rs.getInt("total");
                totalHarian += total;
                
                model.addRow(new Object[]{
                    rs.getString("id_transaksi"),
                    rs.getTimestamp("tanggal"),
                    total,
                    rs.getInt("bayar"),
                    rs.getInt("kembalian")
                });
                jumlahData++;
            }
            
            if (jumlahData == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada transaksi pada tanggal " + tanggalFilter);
            } else {
                JOptionPane.showMessageDialog(this, 
                    String.format("Ditemukan %d transaksi pada tanggal %s\nTotal Penjualan: Rp %,d", 
                    jumlahData, tanggalFilter, totalHarian));
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
                SELECT * FROM transaksi 
                ORDER BY tanggal DESC
                """);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_transaksi"),
                    rs.getTimestamp("tanggal"),
                    rs.getInt("total"),
                    rs.getInt("bayar"),
                    rs.getInt("kembalian")
                });
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void tampilkanDetailTransaksi(String idTransaksi) {
        try {
            Connection conn = DBC.getConnection();
            PreparedStatement ps = conn.prepareStatement("""
                SELECT dt.*, b.nama_barang, b.harga
                FROM detail_transaksi dt
                JOIN barang b ON dt.id_barang = b.id_barang
                WHERE dt.id_transaksi = ?
                ORDER BY dt.id_detail
                """);
            ps.setString(1, idTransaksi);
            
            ResultSet rs = ps.executeQuery();
            
            StringBuilder detail = new StringBuilder();
            detail.append("Detail Transaksi: ").append(idTransaksi).append("\n");
            detail.append("="+ "=".repeat(50)).append("\n");
            
            int totalItem = 0;
            while (rs.next()) {
                String namaBarang = rs.getString("nama_barang");
                int harga = rs.getInt("harga");
                int qty = rs.getInt("qty");
                int subtotal = rs.getInt("subtotal");
                
                detail.append(String.format("%-25s | Qty: %2d | @Rp %,8d | Rp %,10d\n", 
                    namaBarang, qty, harga, subtotal));
                totalItem += qty;
            }
            
            detail.append("="+ "=".repeat(50)).append("\n");
            detail.append(String.format("Total Item: %d", totalItem));
            
            // Tampilkan dalam dialog
            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
            
            rs.close();
            ps.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil detail: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Custom renderer untuk format currency
    private class CurrencyRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Integer) {
                setText(String.format("Rp %,d", (Integer) value));
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
}

