package kasir_uas.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LaporanView extends JFrame {
    public JComboBox<String> comboJenis;
    public JButton btnTampilkan, btnExportPDF;
    public JTable tableLaporan;
    public DefaultTableModel tableModel;
    
    // Color palette
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color LIGHT_GRAY = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_BG = new Color(248, 249, 250);

    public LaporanView() {
        setupFrame();
        setupComponents();
        setupLayout();
        applyStyles();
    }

    private void setupFrame() {
        setTitle("Laporan & Analisis Transaksi");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(LIGHT_GRAY);
        
        // Set modern look and feel
        try {
        	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComponents() {
        // Setup combo box
        comboJenis = new JComboBox<>(new String[] {
            "Laporan Harian", 
            "Laporan Mingguan", 
            "Laporan Bulanan"
        });
        
        // Setup buttons
        btnTampilkan = new JButton("Tampilkan Laporan");
        btnExportPDF = new JButton("Export ke PDF");
        
        // Setup table
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        tableLaporan = new JTable(tableModel);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainContainer.setBackground(LIGHT_GRAY);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Control Panel
        JPanel controlPanel = createControlPanel();
        
        // Statistics Panel
//        JPanel statsPanel = createStatsPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        
        // Combine control and stats
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(LIGHT_GRAY);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);
//        topPanel.add(statsPanel, BorderLayout.SOUTH);

        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(tablePanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GRAY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Title section
        JLabel titleLabel = new JLabel("LAPORAN & ANALISIS TRANSAKSI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtitle with current date
        JLabel subtitleLabel = new JLabel("Generated on: " + getCurrentDateTime());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(LIGHT_GRAY);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        controlPanel.setBackground(WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                " Filter Laporan ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Filter section
        JLabel filterLabel = createStyledLabel("Jenis Laporan:");
        controlPanel.add(filterLabel);
        controlPanel.add(comboJenis);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnTampilkan);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(btnExportPDF);

        return controlPanel;
    }

//    private JPanel createStatsPanel() {
//        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
//        statsPanel.setBackground(LIGHT_GRAY);
//        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
//
//        // Create stat cards
//        statsPanel.add(createStatCard("Total Transaksi", "0", SUCCESS_COLOR, "[TRANS]"));
//        statsPanel.add(createStatCard("Total Pendapatan", "Rp 0", INFO_COLOR, "[MONEY]"));
//        statsPanel.add(createStatCard("Rata-rata/Transaksi", "Rp 0", WARNING_COLOR, "[AVG]"));
//        statsPanel.add(createStatCard("Periode Aktif", "0 Hari", DARK_GRAY, "[TIME]"));
//
//        return statsPanel;
//    }

    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card background with rounded corners
                g2d.setColor(WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Draw colored border
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
            }
        };
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(180, 80));

        // Icon and title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        iconLabel.setForeground(color);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(127, 140, 141));
        
        topPanel.add(iconLabel);
        topPanel.add(Box.createHorizontalStrut(8));
        topPanel.add(titleLabel);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(DARK_GRAY);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                " Data Laporan Transaksi ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        JScrollPane scrollPane = new JScrollPane(tableLaporan);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(WHITE);
        
        // Add empty state message
        JLabel emptyLabel = new JLabel("Pilih jenis laporan dan klik 'Tampilkan Laporan' untuk melihat data");
        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        emptyLabel.setForeground(new Color(127, 140, 141));
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initially show empty message
        if (tableModel.getRowCount() == 0) {
            contentPanel.add(emptyLabel, BorderLayout.NORTH);
        }
        
        tablePanel.add(contentPanel, BorderLayout.CENTER);
        return tablePanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DARK_GRAY);
        return label;
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return now.format(formatter);
    }

    private void applyStyles() {
        // Style combo box
        styleComboBox(comboJenis);

        // Style buttons
        styleButton(btnTampilkan, SUCCESS_COLOR, "[VIEW]");
        styleButton(btnExportPDF, DANGER_COLOR, "[PDF]");

        // Style table
        styleTable();
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setPreferredSize(new Dimension(180, 35));
        
        // Custom renderer for dropdown items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                
                if (isSelected) {
                    setBackground(PRIMARY_COLOR);
                    setForeground(WHITE);
                } else {
                    setBackground(WHITE);
                    setForeground(DARK_GRAY);
                }
                return this;
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
        button.setPreferredSize(new Dimension(160, 40));
        
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
        tableLaporan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableLaporan.setRowHeight(35);
        tableLaporan.setGridColor(new Color(189, 195, 199));
        tableLaporan.setSelectionBackground(new Color(52, 152, 219, 50));
        tableLaporan.setSelectionForeground(DARK_GRAY);
        tableLaporan.setShowGrid(true);
        tableLaporan.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = tableLaporan.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Cell renderer for alternating row colors and formatting
        tableLaporan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(248, 249, 250));
                }
                
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Center align first column (typically ID or No)
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                // Format currency columns (assuming columns with "Harga" or "Total" contain currency)
                if (value != null && table.getColumnName(column).toLowerCase().contains("harga") ||
                    table.getColumnName(column).toLowerCase().contains("total")) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                
                return c;
            }
        });
    }

    // Method to update table data (same as original)
    public void setTableData(String[] headers, java.util.List<String[]> data) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        
        for (String header : headers) {
            tableModel.addColumn(header);
        }
        
        for (String[] row : data) {
            tableModel.addRow(row);
        }
        
        // Auto-resize columns based on content
        if (headers.length > 0) {
            tableLaporan.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
    }
}