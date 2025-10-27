
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;



public class EmployeeManagementGUI extends JFrame {
    private ArrayList<Employee> employees = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private final String fileName = "employees.csv";

    public EmployeeManagementGUI() {
        setTitle("Cathy Pettis Employee Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load employees from CSV
        loadFromFile();

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Salary"}, 0);
        table = new JTable(tableModel);
        refreshTable();

        // Buttons
        JButton addBtn = new JButton("Add Employee");
        JButton removeBtn = new JButton("Remove Selected");
        JButton saveBtn = new JButton("Save to File");

        addBtn.addActionListener(e -> addEmployee());
        removeBtn.addActionListener(e -> removeEmployee());
        saveBtn.addActionListener(e -> saveToFile());

        JPanel panel = new JPanel();
        panel.add(addBtn);
        panel.add(removeBtn);
        panel.add(saveBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void addEmployee() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);

            String name = JOptionPane.showInputDialog(this, "Enter Name:");
            if (name == null) return;

            String salaryStr = JOptionPane.showInputDialog(this, "Enter Salary:");
            if (salaryStr == null) return;
            double salary = Double.parseDouble(salaryStr);

            employees.add(new Employee(id, name, salary));
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter numbers for ID and Salary.");
        }
    }

    private void removeEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            employees.remove(selectedRow);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{emp.id, emp.name, emp.salary});
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Employee emp : employees) {
                pw.println(emp.id + "," + emp.name + "," + emp.salary);
            }
            JOptionPane.showMessageDialog(this, "Saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file.");
        }
    }

    private void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double salary = Double.parseDouble(parts[2]);
                    employees.add(new Employee(id, name, salary));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EmployeeManagementGUI().setVisible(true);
        });
    }
}
