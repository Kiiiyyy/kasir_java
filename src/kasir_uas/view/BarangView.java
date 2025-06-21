package kasir_uas.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class BarangView extends JFrame {
    public JTextField tfNama = new JTextField(15);
    public JTextField tfSatuan = new JTextField(10);
    public JTextField tfStok = new JTextField(5);
    public JTextField tfHarga = new JTextField(10);
    public JButton btnTambah = new JButton("Tambah");
    public JButton btnUbah = new JButton("Ubah");
    public JButton btnHapus = new JButton("Hapus");
    public JTable tableBarang = new JTable();
    public DefaultTableModel tableModel;

    // Color palette
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_GRAY = new Color(236, 240, 241);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color WHITE = Color.WHITE;

    public BarangView() {
        setupFrame();
        setupComponents();
        setupLayout();
        applyStyles();
    }

    private void setupFrame() {
        setTitle("Manajemen Barang - Sistem Kasir");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(WHITE);
        
        // Set modern look and feel
        try {
        	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComponents() {
        // Setup table
        String[] header = {"ID", "Nama Barang", "Satuan", "Stok", "Harga (Rp)"};
        tableModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        tableBarang.setModel(tableModel);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainContainer.setBackground(LIGHT_GRAY);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        
        // Combine form and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(LIGHT_GRAY);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(tablePanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(LIGHT_GRAY);
        
        JLabel titleLabel = new JLabel("MANAJEMEN BARANG");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_GRAY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                " Form Input Data ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Nama Barang:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfNama, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Satuan:"), gbc);
        gbc.gridx = 3;
        formPanel.add(tfSatuan, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Stok:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfStok, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Harga (Rp):"), gbc);
        gbc.gridx = 3;
        formPanel.add(tfHarga, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(LIGHT_GRAY);
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnHapus);
        
        return buttonPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                " Data Barang ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DARK_GRAY);
        return label;
    }

    private void applyStyles() {
        // Style text fields
        styleTextField(tfNama);
        styleTextField(tfSatuan);
        styleTextField(tfStok);
        styleTextField(tfHarga);

        // Style buttons
        styleButton(btnTambah, SUCCESS_COLOR, "[+]");
        styleButton(btnUbah, WARNING_COLOR, "[EDIT]");
        styleButton(btnHapus, DANGER_COLOR, "[DEL]");

        // Style table
        styleTable();
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(WHITE);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));
        
        // Add focus effects
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private void styleButton(JButton button, Color bgColor, String icon) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        
        // Add icon to button text
        String originalText = button.getText();
        button.setText(icon + " " + originalText);
        
        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void styleTable() {
        // Table styling
        tableBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableBarang.setRowHeight(35);
        tableBarang.setGridColor(new Color(189, 195, 199));
        tableBarang.setSelectionBackground(new Color(52, 152, 219, 50));
        tableBarang.setSelectionForeground(DARK_GRAY);
        tableBarang.setShowGrid(true);
        tableBarang.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = tableBarang.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Cell renderer for alternating row colors
        tableBarang.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(248, 249, 250));
                }
                
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                setHorizontalAlignment(column == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                
                return c;
            }
        });

        // Set column widths
        if (tableBarang.getColumnModel().getColumnCount() > 0) {
            tableBarang.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
            tableBarang.getColumnModel().getColumn(1).setPreferredWidth(200); // Nama
            tableBarang.getColumnModel().getColumn(2).setPreferredWidth(80);  // Satuan
            tableBarang.getColumnModel().getColumn(3).setPreferredWidth(80);  // Stok
            tableBarang.getColumnModel().getColumn(4).setPreferredWidth(120); // Harga
        }
    }
}