package kasir_uas.view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransaksiView extends JFrame {
    private JTable tableBarang;
    private JTable tableKeranjang;
    private JTextField txtJumlahBeli, txtBayar, txtCariBarang;
    private JButton btnTambahKeranjang, btnSelesaiTransaksi;
    private JLabel lblTotalBelanja, lblKembalianBelanja;
    private JButton btnHapusKeranjang;
    private JButton btnLogTransaksi;
    
    // Modern color scheme
    private final Color PRIMARY_COLOR = new Color(79, 172, 254);      // Modern blue
    private final Color SECONDARY_COLOR = new Color(248, 250, 252);   // Light gray
    private final Color ACCENT_COLOR = new Color(34, 197, 94);        // Green
    private final Color DANGER_COLOR = new Color(239, 68, 68);        // Red
    private final Color TEXT_DARK = new Color(30, 41, 59);            // Dark gray
    private final Color TEXT_LIGHT = new Color(100, 116, 139);        // Light gray
    private final Color WHITE = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);      // Light border
    
    // Modern fonts
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public TransaksiView() {
        initializeFrame();
        createComponents();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("Point of Sale - Toko Kelontong");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setBackground(SECONDARY_COLOR);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createComponents() {
        setLayout(new BorderLayout(0, 0));
        
        // Create header
        add(createHeader(), BorderLayout.NORTH);
        
        // Create main content
        add(createMainContent(), BorderLayout.CENTER);
        
        // Create bottom panel
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Title section
        JLabel title = new JLabel("Point of Sale System");
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_DARK);
        
        // Search section
        JPanel searchPanel = createSearchPanel();
        
        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        
        // Add subtle bottom border
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        return header;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        searchPanel.setBackground(WHITE);
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 8));
        
        txtCariBarang = createModernTextField("Search products...", 20);
        
        searchPanel.add(searchIcon);
        searchPanel.add(txtCariBarang);
        
        return searchPanel;
    }
    
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 0, 30));
        
        // Left panel - Products
        JPanel leftPanel = createProductPanel();
        
        // Right panel - Cart
        JPanel rightPanel = createCartPanel();
        
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createProductPanel() {
        JPanel panel = createModernCard("Available Products");
        panel.setPreferredSize(new Dimension(600, 400));
        
        // Create modern table
        tableBarang = createModernTable(new String[]{"Code", "Product Name", "Price", "Stock"});
        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(WHITE);
        
        // Add to panel with proper spacing
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add input section
        JPanel inputPanel = createProductInputPanel();
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createProductInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        panel.setBackground(WHITE);
        
        JLabel label = new JLabel("Quantity:");
        label.setFont(BODY_FONT);
        label.setForeground(TEXT_DARK);
        
        txtJumlahBeli = createModernTextField("1", 8);
        btnTambahKeranjang = createModernButton("Add to Cart", PRIMARY_COLOR, WHITE);
        btnTambahKeranjang.setPreferredSize(new Dimension(120, 35));
        
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(txtJumlahBeli);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(btnTambahKeranjang);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = createModernCard("Shopping Cart");
        panel.setPreferredSize(new Dimension(450, 400));
        
        // Cart table
        tableKeranjang = createModernTable(new String[]{"Code", "Product", "Price", "Qty", "Total"});
        JScrollPane scrollPane = new JScrollPane(tableKeranjang);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Cart actions
        JPanel cartActions = createCartActionsPanel();
        panel.add(cartActions, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCartActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Remove button
        btnHapusKeranjang = createModernButton("Remove Item", DANGER_COLOR, WHITE);
        btnHapusKeranjang.setPreferredSize(new Dimension(120, 35));
        
        // Total label
        lblTotalBelanja = new JLabel("Total: Rp 0");
        lblTotalBelanja.setFont(SUBTITLE_FONT);
        lblTotalBelanja.setForeground(TEXT_DARK);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(WHITE);
        leftPanel.add(btnHapusKeranjang);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(WHITE);
        rightPanel.add(lblTotalBelanja);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        // Left side - Log button
        btnLogTransaksi = createModernButton("Transaction Log", TEXT_LIGHT, WHITE);
        btnLogTransaksi.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        
        // Right side - Payment section
        JPanel paymentPanel = createPaymentPanel();
        
        panel.add(btnLogTransaksi, BorderLayout.WEST);
        panel.add(paymentPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setBackground(WHITE);
        
        // Payment input
        JLabel payLabel = new JLabel("Payment:");
        payLabel.setFont(BODY_FONT);
        payLabel.setForeground(TEXT_DARK);
        
        txtBayar = createModernTextField("0", 12);
        
        // Change label
        lblKembalianBelanja = new JLabel("Change: Rp 0");
        lblKembalianBelanja.setFont(BODY_FONT);
        lblKembalianBelanja.setForeground(ACCENT_COLOR);
        
        // Complete transaction button
        btnSelesaiTransaksi = createModernButton("Complete Transaction", ACCENT_COLOR, WHITE);
        btnSelesaiTransaksi.setPreferredSize(new Dimension(180, 40));
        btnSelesaiTransaksi.setFont(BUTTON_FONT);
        
        panel.add(payLabel);
        panel.add(txtBayar);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblKembalianBelanja);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnSelesaiTransaksi);
        
        return panel;
    }
    
    private JPanel createModernCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    private JTextField createModernTextField(String placeholder, int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(WHITE);
        field.setForeground(TEXT_DARK);
        
        // Add placeholder effect
        if (!placeholder.isEmpty()) {
            field.setText(placeholder);
            field.setForeground(TEXT_LIGHT);
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(TEXT_DARK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(TEXT_LIGHT);
                    }
                }
            });
        }
        
        return field;
    }
    
    private JButton createModernButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JTable createModernTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(BODY_FONT);
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(79, 172, 254, 50));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        // Header styling
        table.getTableHeader().setFont(SUBTITLE_FONT);
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setForeground(TEXT_DARK);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        // Cell alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Apply renderers based on column type
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].contains("Price") || columns[i].contains("Total") || columns[i].contains("Stock") || columns[i].contains("Qty")) {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            } else if (columns[i].contains("Code")) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        return table;
    }

    // Getter methods (unchanged)
    public JTable getTableBarang() { return tableBarang; }
    public JTable getTableKeranjang() { return tableKeranjang; }
    public JTextField getTxtJumlahBeli() { return txtJumlahBeli; }
    public JTextField getTxtCariBarang() { return txtCariBarang; }
    public JTextField getTxtBayar() { return txtBayar; }
    public JButton getBtnTambahKeranjang() { return btnTambahKeranjang; }
    public JButton getBtnSelesaiTransaksi() { return btnSelesaiTransaksi; }
    public JLabel getLblTotalBelanja() { return lblTotalBelanja; }
    public JLabel getLblKembalianBelanja() { return lblKembalianBelanja; }
    public JButton getBtnHapusKeranjang() { return btnHapusKeranjang; }
    public JButton getBtnLogTransaksi() { return btnLogTransaksi; }
}