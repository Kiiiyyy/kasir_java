package kasir_uas.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OpnameView extends JFrame {
    public JTable table;
    public DefaultTableModel tableModel;
    public JButton btnRiwayatSemua;

    // Color palette (copied from LaporanView)
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color LIGHT_GRAY = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public OpnameView() {
        setupFrame();
        setupComponents();
        setupLayout();
        applyStyles();
    }

    private void setupFrame() {
        setTitle("Stok Opname");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(LIGHT_GRAY);

        // Set Nimbus Look and Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComponents() {
        // Setup table
        String[] header = {"ID", "Nama", "Stok Sistem", "Aksi"};
        tableModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only "Aksi" column is editable
            }
        };
        table = new JTable(tableModel);

        // Setup button
        btnRiwayatSemua = new JButton("Lihat Riwayat Semua");
        btnRiwayatSemua.addActionListener(e -> new LogOpname().setVisible(true));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainContainer.setBackground(LIGHT_GRAY);

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(btnRiwayatSemua);

        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(tablePanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GRAY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Title
        JLabel titleLabel = new JLabel("STOK OPNAME");
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

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                " Data Stok Opname ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(WHITE);


        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(contentPanel, BorderLayout.CENTER);
        return tablePanel;
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return now.format(formatter);
    }

    private void applyStyles() {
        // Style button
        styleButton(btnRiwayatSemua, SUCCESS_COLOR, "[HISTORY]");

        // Style table
        styleTable();
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(new Color(52, 152, 219, 50));
        table.setSelectionForeground(DARK_GRAY);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Cell renderer for alternating row colors and alignment
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(248, 249, 250));
                }

                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                // Center align ID and Stok Sistem columns
                if (column == 0 || column == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        });

        // Apply ButtonRenderer to Aksi column
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
    }

    // Placeholder ButtonRenderer class (styled to match LaporanView)
    class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton("Aksi");
            styleButton(button, PRIMARY_COLOR, "[ACTION]");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                button.setBackground(PRIMARY_COLOR.darker());
            } else {
                button.setBackground(PRIMARY_COLOR);
            }
            return button;
        }
    }
}