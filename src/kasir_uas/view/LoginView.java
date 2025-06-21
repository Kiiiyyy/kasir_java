package kasir_uas.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginView extends JFrame {
    public JTextField tfUsername = new JTextField(15);
    public JPasswordField pfPassword = new JPasswordField(15);
    public JButton btnLogin = new JButton("Login");

    public LoginView() {
//        setTitle("Login");
//        setSize(300, 150);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        JPanel panel = new JPanel();
//        panel.add(new JLabel("Username"));
//        panel.add(tfUsername);
//        panel.add(new JLabel("Password"));
//        panel.add(pfPassword);
//        panel.add(btnLogin);
//        add(panel);
    	
    	setTitle("Login Kasir App");
		setBounds(200, 200, 600, 600);
		getContentPane().setBackground(new Color(51, 69, 84));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
		JLabel logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		ImageIcon originalIcon = new ImageIcon(this.getClass().getResource("/kasir_uas/img/logo.png"));

		// Dapatkan ukuran JLabel
		int labelWidth = 166;   // lebar logoLabel
		int labelHeight = 220;  // tinggi logoLabel

		// Resize image supaya sesuai ukuran JLabel
		Image scaledImage = originalIcon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

		// Set icon yang sudah di-resize
		logoLabel.setIcon(new ImageIcon(scaledImage));

		getContentPane().add(logoLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		this.tfUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.tfUsername.setColumns(10);
		
		this.pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.pfPassword.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblKasir = new JLabel("Kasir App");
		lblKasir.setHorizontalAlignment(SwingConstants.CENTER);
		lblKasir.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		this.btnLogin.setBackground(new Color(145, 159, 161));
		this.btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(209)
							.addComponent(logoLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(162)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(tfUsername, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
								.addComponent(lblUsername)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.pfPassword)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(246)
							.addComponent(lblKasir, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(239)
							.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(181, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(logoLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblKasir)
					.addGap(25)
					.addComponent(lblUsername)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfUsername, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.pfPassword, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(77, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
    }
}

