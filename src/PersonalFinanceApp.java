import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PersonalFinanceApp {

    private JFrame frame;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private double totalAmount = 0.0;

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
        frame = new JFrame("Pengelolaan Uang Pribadi");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel descriptionLabel = new JLabel("Deskripsi:");
        descriptionField = new JTextField(20);
        JLabel amountLabel = new JLabel("Jumlah:");
        amountField = new JTextField(10);
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");

        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        JPanel tablePanel = new JPanel();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Deskripsi");
        tableModel.addColumn("Jumlah");
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        JPanel totalPanel = new JPanel();
        totalLabel = new JLabel("Total Pengeluaran: Rp 0.00");
        totalPanel.add(totalLabel);

        panel.add(inputPanel);
        panel.add(tablePanel);
        panel.add(totalPanel);
        frame.add(panel);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

                    tableModel.addRow(new Object[] { description, "Rp " + String.format("%.2f", amount) });
                    totalAmount += amount;
                    totalLabel.setText("Total Pengeluaran: Rp " + String.format("%.2f", totalAmount));

                    descriptionField.setText("");
                    amountField.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Jumlah harus berupa angka!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    double amount = Double.parseDouble(
                            table.getValueAt(selectedRow, 1).toString().replace("Rp ", "").replace(",", ""));
                    tableModel.removeRow(selectedRow);
                    totalAmount -= amount;
                    totalLabel.setText("Total Pengeluaran: Rp " + String.format("%.2f", totalAmount));
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih pengeluaran yang ingin dihapus!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
