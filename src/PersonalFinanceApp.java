import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import entities.Expense;
import utils.DatabaseManager;

public class PersonalFinanceApp {
    private JFrame frame;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private DatabaseManager dbManager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                PersonalFinanceApp window = new PersonalFinanceApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PersonalFinanceApp() {
        dbManager = new DatabaseManager();
        initializeUI();
        loadExpensesFromDatabase();
    }

    private void initializeUI() {
        frame = new JFrame("Pengelolaan Uang Pribadi");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Pengelolaan Uang Pribadi", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(63, 81, 181));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Tambah Pengeluaran"));
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel descriptionLabel = new JLabel("Deskripsi:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionField = new JTextField(20);

        JLabel amountLabel = new JLabel("Jumlah:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountField = new JTextField(20);

        JButton addButton = new JButton("Tambah");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);

        JButton deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(deleteButton, gbc);

        frame.add(inputPanel, BorderLayout.WEST);

        // Table Panel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Id");
        tableModel.addColumn("Deskripsi");
        tableModel.addColumn("Jumlah");
        tableModel.addColumn("Tanggal");

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Pengeluaran"));

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);

        frame.add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalLabel = new JLabel("Total Pengeluaran: Rp 0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        footerPanel.add(totalLabel);

        frame.add(footerPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteExpense();
            }
        });
    }

    private void addExpense() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();

        if (description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Deskripsi dan jumlah tidak boleh kosong!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(frame, "Jumlah harus lebih besar dari 0!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Expense expense = new Expense(0, description, amount, new Date(), null);
            expense = dbManager.addExpense(expense);

            if (expense.getId() != 0) {
                tableModel.addRow(
                        new Object[] { expense.getId(), expense.getDescription(),
                                "Rp " + String.format("%.2f", expense.getAmount()), expense.getCreatedDate() });

                updateTotalLabel();
            } else {
                JOptionPane.showMessageDialog(frame, "Error on save data!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            descriptionField.setText("");
            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String description = table.getValueAt(selectedRow, 0).toString();
            double amount = Double
                    .parseDouble(table.getValueAt(selectedRow, 1).toString().replace("Rp ", "").replace(",", ""));
            Expense expense = new Expense(description, amount);
            dbManager.deleteExpense(expense);

            tableModel.removeRow(selectedRow);
            updateTotalLabel();
        } else {
            JOptionPane.showMessageDialog(frame, "Pilih pengeluaran yang ingin dihapus!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotalLabel() {
        double totalAmount = dbManager.getTotalAmount();
        totalLabel.setText("Total Pengeluaran: Rp " + String.format("%.2f", totalAmount));
    }

    private void loadExpensesFromDatabase() {
        dbManager.getExpenses().forEach(expense -> {
            tableModel.addRow(
                    new Object[] { expense.getId(), expense.getDescription(),
                            "Rp " + String.format("%.2f", expense.getAmount()), expense.getCreatedDate() });
        });

        updateTotalLabel();
    }
}
