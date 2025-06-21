package kasir_uas.controller;

import kasir_uas.model.*;
import kasir_uas.view.LogTransaksi;
import kasir_uas.view.TransaksiView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TransaksiController {
    private TransaksiModel model;
	private TransaksiView view;
    private BarangDAO barangDAO;
    private DefaultTableModel modelKeranjang;
    private int totalBayar = 0;

    public TransaksiController(TransaksiView view, TransaksiModel model) {
        this.view = view;
        this.model = model;
        this.barangDAO = new BarangDAO();
        this.modelKeranjang = (DefaultTableModel) view.getTableKeranjang().getModel();

        // Load barang awal
        loadTableBarang("");

        // Listener untuk live search
        view.getTxtCariBarang().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = view.getTxtCariBarang().getText();
                loadTableBarang(keyword);
            }
        });
        
        view.getTxtBayar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = view.getTxtBayar().getText().trim();
                if (!input.matches("\\d+")) {
                    view.getLblKembalianBelanja().setText("Kembalian : Rp. 0");
                    return;
                }
                String inputStr = input.replaceAll("[^\\d]", ""); // hasilnya "60000"
                int bayar = Integer.parseInt(inputStr);
                int kembalian = hitungKembalian(bayar);

                // Tampilkan kembalian (kalau minus bisa dibuat 0 atau ditandai)
                if (kembalian >= 0) {
                    view.getLblKembalianBelanja().setText("Kembalian : Rp. " + kembalian);
                } else {
                    view.getLblKembalianBelanja().setText("Belum cukup bayar");
                }
            }
        });


        // Listener tombol
        view.getBtnTambahKeranjang().addActionListener(e -> tambahKeKeranjang());
        view.getBtnHapusKeranjang().addActionListener(e -> hapusDariKeranjang());
        view.getBtnSelesaiTransaksi().addActionListener(e -> simpanTransaksi());
        view.getBtnLogTransaksi().addActionListener(e -> {
            LogTransaksi logView = new LogTransaksi();
            logView.setVisible(true);
        });


    }
    
    private int hitungKembalian(int bayar) {
        String labelTotal = view.getLblTotalBelanja().getText(); // "Total: Rp 60000"
        String totalStr = labelTotal.replaceAll("[^\\d]", ""); // hasilnya "60000"
        int total = Integer.parseInt(totalStr);
    	int kembalian = bayar - total;
    	return kembalian;
    }
    
    private void simpanTransaksi() {
        DefaultTableModel keranjangModel = (DefaultTableModel) view.getTableKeranjang().getModel();
        int rowCount = keranjangModel.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(view, "Keranjang masih kosong!");
            return;
        }

        int total = 0;
        List<DetailTransaksi> detailList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            String kode = keranjangModel.getValueAt(i, 0).toString();
            int harga = Integer.parseInt(keranjangModel.getValueAt(i, 2).toString());
            int jumlah = Integer.parseInt(keranjangModel.getValueAt(i, 3).toString());
            int subTotal = Integer.parseInt(keranjangModel.getValueAt(i, 4).toString());

            total += harga * jumlah;
        
            detailList.add(new DetailTransaksi(kode, harga, jumlah, subTotal));
        }

        int idUser = 1; 
        if (view.getTxtBayar().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Masukkan jumlah uang bayar terlebih dahulu.");
            return;
        }
        int bayar = Integer.parseInt(view.getTxtBayar().getText());
        String labelKembalian = view.getLblKembalianBelanja().getText(); // "Total: Rp 60000"
        String kembalianStr = labelKembalian.replaceAll("[^\\d]", ""); // hasilnya "60000"
        if (kembalianStr.isEmpty()) {
        	kembalianStr = "0";
        }
        int kembalian = Integer.parseInt(kembalianStr);

        if(bayar < total) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan transaksi, Uang nya kurang");
            return;
        }
        String idTransaksi = model.simpanTransaksi(idUser, total, bayar, kembalian);

        if (idTransaksi != null) {
            for (DetailTransaksi detail : detailList) {
                model.simpanDetailTransaksi(idTransaksi, detail);
            }
            JOptionPane.showMessageDialog(view, "Transaksi berhasil disimpan!");
            kosongkanKeranjang();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan transaksi.");
        }
    }
    
    private void kosongkanKeranjang() {
        DefaultTableModel keranjangModel = (DefaultTableModel) view.getTableKeranjang().getModel();
        keranjangModel.setRowCount(0);
        view.getLblTotalBelanja().setText("Total: Rp 0");
    }

    private void loadTableBarang(String keyword) {
        List<Barang> list = barangDAO.searchBarang(keyword);
        DefaultTableModel model = (DefaultTableModel) view.getTableBarang().getModel();
        model.setRowCount(0); // clear table
        for (Barang b : list) {
            model.addRow(new Object[]{b.getIdBarang(), b.getNamaBarang(), b.getHarga(), b.getStok()});
        }
    }
    
    private void hapusDariKeranjang() {
        int selected = view.getTableKeranjang().getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(view, "Pilih item di keranjang yang ingin dihapus!");
            return;
        }

        DefaultTableModel modelKeranjang = (DefaultTableModel) view.getTableKeranjang().getModel();
        DefaultTableModel modelBarang = (DefaultTableModel) view.getTableBarang().getModel();

        // Ambil informasi dari keranjang
        String idBarang = modelKeranjang.getValueAt(selected, 0).toString(); // kolom ke-0 adalah kode barang
        int jumlah = Integer.parseInt(modelKeranjang.getValueAt(selected, 3).toString()); // kolom ke-3 = jumlah
        int subtotal = Integer.parseInt(modelKeranjang.getValueAt(selected, 4).toString()); // kolom ke-4 = subtotal

        // Hapus dari keranjang
        modelKeranjang.removeRow(selected);
        totalBayar -= subtotal;

        // Cari barang di tabel barang dan tambahkan kembali stoknya
        for (int i = 0; i < modelBarang.getRowCount(); i++) {
            if (modelBarang.getValueAt(i, 0).toString().equals(idBarang)) {
                int stokLama = Integer.parseInt(modelBarang.getValueAt(i, 3).toString()); // kolom ke-3 = stok
                modelBarang.setValueAt(stokLama + jumlah, i, 3);
                break;
            }
        }

        // Update total belanja
        view.getLblTotalBelanja().setText("Total: Rp " + totalBayar);
    }


    private void tambahKeKeranjang() {
        int selected = view.getTableBarang().getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(view, "Pilih barang terlebih dahulu!");
            return;
        }
        
        String input = view.getTxtJumlahBeli().getText().trim();
        System.out.println("Isi input: [" + input + "]");
        input = input.replaceAll("[^\\d]", ""); // Hanya ambil angka
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Jumlah harus berupa angka!");
            return;
        }
        
        int jumlah = Integer.parseInt(input);
        System.out.println("DEBUG: input = [" + input + "]");
        try {

            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view, "Jumlah harus lebih dari 0!");
                return;
            }

            DefaultTableModel modelBarang = (DefaultTableModel) view.getTableBarang().getModel();
            String kode = modelBarang.getValueAt(selected, 0).toString();
            String nama = modelBarang.getValueAt(selected, 1).toString();
            double hargaDouble = Double.parseDouble(modelBarang.getValueAt(selected, 2).toString());
            int harga = (int) hargaDouble;
            int stok = Integer.parseInt(modelBarang.getValueAt(selected, 3).toString());

            if (jumlah > stok) {
                JOptionPane.showMessageDialog(view, "Stok tidak mencukupi!");
                return;
            }

            int subtotal = harga * jumlah;
            modelKeranjang.addRow(new Object[]{kode, nama, harga, jumlah, subtotal});

            totalBayar += subtotal;
            view.getLblTotalBelanja().setText("Total: Rp " + totalBayar);
            
//          kurangi stok barang di tampilan
            modelBarang.setValueAt(stok - jumlah, selected, 3);

            // Kosongkan input jumlah
            view.getTxtJumlahBeli().setText("");

        } catch (NumberFormatException e) {
        	e.printStackTrace();            
        	JOptionPane.showMessageDialog(view, "Masukkan jumlah yang valid!");
        }
    }
}


