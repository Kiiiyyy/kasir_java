package kasir_uas.view;
import javax.swing.*;
import java.awt.*;

public class StokOpnameDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField tfStokBaru = new JTextField(5);
    private JTextArea taKet = new JTextArea(3, 20);

    public StokOpnameDialog(String idBarang, String namaBarang, int stokSistem) {
        setTitle("Stok Opname Barang");
        setModal(true);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(new JLabel("Nama: " + namaBarang));
        panel.add(new JLabel("Stok Sistem: " + stokSistem));
        panel.add(new JLabel("Stok Fisik:"));
        panel.add(tfStokBaru);
        panel.add(new JLabel("Keterangan:"));
        panel.add(new JScrollPane(taKet));

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.addActionListener(e -> {
            String stokInput = tfStokBaru.getText().trim();
            if (stokInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stok fisik harus diisi!");
                return;
            }
            try {
                Integer.parseInt(stokInput);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stok fisik harus angka!");
                return;
            }

            if (taKet.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keterangan harus diisi!");
                return;
            }

            confirmed = true;
            setVisible(false);
        });

        add(panel, BorderLayout.CENTER);
        add(btnSimpan, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getStokBaru() {
        return Integer.parseInt(tfStokBaru.getText());
    }

    public String getKeterangan() {
        return taKet.getText();
    }
}
