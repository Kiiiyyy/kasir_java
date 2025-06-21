package kasir_uas.controller;

import kasir_uas.view.LaporanView;
import kasir_uas.model.LaporanModel;

import java.awt.event.*;
import java.sql.Connection;
import java.util.*;

import javax.swing.*;
import java.awt.print.PrinterException;

public class LaporanController {
    private LaporanView view;
    private LaporanModel model;

    public LaporanController(LaporanView view, LaporanModel model) {
        this.view = view;
        this.model = model;

        this.view.btnTampilkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanLaporan();
            }
        });

    
        this.view.btnExportPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToPDF();
            }
        });

    }

    private void exportToPDF() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("laporan.pdf"));
            int option = chooser.showSaveDialog(view);

            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();

                com.lowagie.text.Document document = new com.lowagie.text.Document();
                com.lowagie.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
                document.open();

                com.lowagie.text.pdf.PdfPTable pdfTable = new com.lowagie.text.pdf.PdfPTable(view.tableLaporan.getColumnCount());

                // Tambah header
                for (int i = 0; i < view.tableLaporan.getColumnCount(); i++) {
                    pdfTable.addCell(new com.lowagie.text.Phrase(view.tableLaporan.getColumnName(i)));
                }

                // Tambah data
                for (int rows = 0; rows < view.tableLaporan.getRowCount(); rows++) {
                    for (int cols = 0; cols < view.tableLaporan.getColumnCount(); cols++) {
                        Object val = view.tableLaporan.getValueAt(rows, cols);
                        pdfTable.addCell(val != null ? val.toString() : "");
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(view, "PDF berhasil disimpan di:\n" + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal export PDF: " + e.getMessage());
        }
    }

    private void tampilkanLaporan() {
        String jenis = view.comboJenis.getSelectedItem().toString();
        try {
            ArrayList<String[]> data = model.getLaporan(jenis);
            String[] headers = data.get(0);
            data.remove(0);
            view.setTableData(headers, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal: " + e.getMessage());
        }
    }

}
