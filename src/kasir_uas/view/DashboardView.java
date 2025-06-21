package kasir_uas.view;

import javax.swing.*;
import kasir_uas.controller.BarangController;
import kasir_uas.controller.LaporanController;
import kasir_uas.controller.LoginController;
import kasir_uas.controller.OpnameController;
import kasir_uas.controller.TransaksiController;
import kasir_uas.model.BarangDAO;
import kasir_uas.model.LaporanModel;
import kasir_uas.model.TransaksiModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardView extends JFrame {
    public JButton btnBarang = new JButton("Kelola Barang");
    public JButton btnTransaksi = new JButton("Transaksi");
    public JButton btnLaporan = new JButton("Laporan");
    public JButton btnLogout = new JButton("Logout");
    public JButton btnOpname = new JButton("Stock Opname");
    
    // Color palette
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_COLOR = new Color(52, 73, 94);
    private final Color LIGHT_GRAY = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_SHADOW = new Color(0, 0, 0, 20);

    public DashboardView() {
        setupFrame();
        setupComponents();
        setupLayout();
        applyStyles();
        setupEventHandlers();
    }

    private void setupFrame() {
        setTitle("Dashboard Sistem Kasir");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(LIGHT_GRAY);
        
        // Set modern look and feel
        try {
        	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComponents() {
        // Update button texts with better descriptions
        btnBarang.setText("Kelola Barang");
        btnTransaksi.setText("Transaksi Penjualan");
        btnLaporan.setText("Laporan & Analisis");
        btnOpname.setText("Stock Opname");
        btnLogout.setText("Keluar Sistem");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main container with gradient background
        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    0, getHeight(), new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Menu Panel
        JPanel menuPanel = createMenuPanel();
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(menuPanel, BorderLayout.CENTER);
        mainContainer.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        // Welcome section
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Kasir");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Pilih menu untuk memulai aktivitas");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Date/Time section
        JLabel dateTimeLabel = new JLabel(getCurrentDateTime());
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateTimeLabel.setForeground(new Color(255, 255, 255, 160));
        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(welcomePanel, BorderLayout.CENTER);
        headerPanel.add(dateTimeLabel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        menuPanel.add(createMenuCard(btnBarang, "Kelola data barang, stok,\ndan informasi produk", SUCCESS_COLOR, "[BARANG]"), gbc);
        
        gbc.gridx = 1;
        menuPanel.add(createMenuCard(btnTransaksi, "Proses transaksi\npenjualan harian", INFO_COLOR, "[KASIR]"), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        menuPanel.add(createMenuCard(btnLaporan, "Lihat laporan penjualan\ndan analisis data", WARNING_COLOR, "[LAPORAN]"), gbc);
        
        gbc.gridx = 1;
        menuPanel.add(createMenuCard(btnOpname, "Audit dan verifikasi\nstok barang", DARK_COLOR, "[OPNAME]"), gbc);
        
        // Logout button - span across bottom
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.3;
        JPanel logoutContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutContainer.setOpaque(false);
        JPanel logoutCard = createMenuCard(btnLogout, "Keluar dari sistem", DANGER_COLOR, "[LOGOUT]");
        logoutCard.setPreferredSize(new Dimension(250, 80));
        logoutContainer.add(logoutCard);
        menuPanel.add(logoutContainer, gbc);
        
        return menuPanel;
    }

    private JPanel createMenuCard(JButton button, String description, Color bgColor, String icon) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(CARD_SHADOW);
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 15, 15);
                
                // Draw card background
                g2d.setColor(WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);
            }
        };
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(280, 120));
        
        // Icon and title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        iconLabel.setForeground(bgColor);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel titleLabel = new JLabel(button.getText());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_COLOR);
        
        topPanel.add(iconLabel);
        topPanel.add(titleLabel);
        
        // Description
        JLabel descLabel = new JLabel("<html>" + description.replace("\n", "<br>") + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        
        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                button.doClick();
            }
        });
        
        return card;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel footerLabel = new JLabel("Sistem Kasir UAS - Developed with Java Swing");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(255, 255, 255, 120));
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private void applyStyles() {
        // Hide original buttons since we're using cards
        btnBarang.setVisible(false);
        btnTransaksi.setVisible(false);
        btnLaporan.setVisible(false);
        btnOpname.setVisible(false);
        btnLogout.setVisible(false);
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm");
        return now.format(formatter);
    }

    private void setupEventHandlers() {
        // Event handlers tetap sama seperti aslinya
        btnBarang.addActionListener(e -> {
            BarangView barangView = new BarangView();
            new BarangController(barangView);
            DashboardView dashboardView = new DashboardView();
            dashboardView.dispose();
            barangView.setVisible(true);
        });
        
        btnTransaksi.addActionListener(e -> {
            TransaksiView transaksiView = new TransaksiView();
            TransaksiModel transaksiModel = new TransaksiModel();
            DashboardView dashboardView = new DashboardView();
            BarangDAO baDao = new BarangDAO();
            dashboardView.dispose();
            new TransaksiController(transaksiView, transaksiModel);
            transaksiView.setVisible(true);
        });
        
        btnLaporan.addActionListener(e -> {
            LaporanView lapView = new LaporanView();
            LaporanModel lapMod = new LaporanModel();
            new LaporanController(lapView, lapMod);
            DashboardView dashboardView = new DashboardView();
            dashboardView.dispose();
            lapView.setVisible(true);
        });
        
        btnOpname.addActionListener(e -> {
            OpnameView OpView = new OpnameView();
            new OpnameController(OpView);
            DashboardView dashboardView = new DashboardView();
            dashboardView.dispose();
            OpView.setVisible(true);
        });
        
        btnLogout.addActionListener(e -> {
            // Custom dialog untuk konfirmasi logout
            int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin keluar dari sistem?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                LoginView login = new LoginView();
                new LoginController(login);
                login.setVisible(true);
            }
        });
    }
}