package kasir_uas.view;
import kasir_uas.controller.OpnameController;
import kasir_uas.model.OpnameModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import kasir_uas.model.OpnameModel;


public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private JTable table;
    private StokOpnameDialog dialog;
    private OpnameController controller;


    public ButtonEditor(JTable table, OpnameController controller) {
        this.table = table;
        this.controller = controller;
        button = new JButton("Opname");
        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            String idBarang = (String) table.getValueAt(row, 0);
            String nama = table.getValueAt(row, 1).toString();
            int stokSistem = (int) table.getValueAt(row, 2);

            // Tampilkan modal (JDialog)
            dialog = new StokOpnameDialog(idBarang, nama, stokSistem);
            dialog.setVisible(true);

            // Kalau disimpan, simpan ke database
            if (dialog.isConfirmed()) {
                int stokBaru = dialog.getStokBaru();
                String ket = dialog.getKeterangan();

                try {
                    new OpnameModel().simpanOpname(idBarang, stokSistem, stokBaru, ket);
                    JOptionPane.showMessageDialog(null, "Berhasil disimpan.");
                    controller.loadBarang(); // Refresh data
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage());
                }
            }

            fireEditingStopped();
        });
    }
    
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
