package kasir_uas.controller;

import kasir_uas.config.DBC;
import kasir_uas.view.*;

import java.awt.event.*;
import java.sql.*;
import javax.swing.JOptionPane;

public class LoginController {
    protected static final BarangView BarangView = null;
	private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;

        this.view.btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DBC.getConnection();
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM user WHERE username=? AND password=?"
                    );
                    ps.setString(1, view.tfUsername.getText());
                    ps.setString(2, String.valueOf(view.pfPassword.getPassword()));
                    ResultSet rs = ps.executeQuery();

                    String username = view.tfUsername.getText();
                    String password = new String(view.pfPassword.getPassword());
                    
                    if (username.isEmpty() || password.isEmpty()) {
    				    JOptionPane.showMessageDialog(null, "Input your username and password please!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }else if (rs.next()) {
                    	JOptionPane.showMessageDialog(null, "Login Success", "Success", JOptionPane.INFORMATION_MESSAGE);
    				    view.tfUsername.setText(null);
    				    view.pfPassword.setText(null);
    				    view.dispose();
				    	DashboardView dashView = new DashboardView();
				    	dashView.setVisible(true);
                        // TODO: lanjut ke dashboard
                    } else {
                        JOptionPane.showMessageDialog(view, "Username atau Password salah!");
                        view.tfUsername.setText(null);
    				    view.pfPassword.setText(null);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
