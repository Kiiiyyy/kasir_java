package kasir_uas.controller;

import kasir_uas.model.OpnameModel;
import kasir_uas.view.OpnameView;
import kasir_uas.view.ButtonEditor;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class OpnameController {
    private OpnameView view;
    private OpnameModel model;

    public OpnameController(OpnameView view) {
        this.view = view;
        this.model = new OpnameModel();
        view.table.getColumnModel().getColumn(3)
        .setCellEditor(new ButtonEditor(view.table, this));  // ← THIS is the fix
        loadBarang();
    }

    public void loadBarang() {
        ArrayList<Object[]> data = model.getDataBarang();
        view.tableModel.setRowCount(0);
        for (Object[] row : data) {
            view.tableModel.addRow(row);
        }
    }

}
