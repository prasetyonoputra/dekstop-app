package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Expense;

public class DatabaseManager {
    private static final String DB_URL = UserConfig.BASE_URL.getValue();
    private static final String DB_USER = UserConfig.DB_USER.getValue();
    private static final String DB_PASSWORD = UserConfig.DB_PASSWORD.getValue();
    private Connection connection;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExpense(Expense expense) {
        String query = "INSERT INTO expenses (description, amount) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(Expense expense) {
        String query = "DELETE FROM expenses WHERE description = ? AND amount = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT description, amount FROM expenses";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                expenses.add(new Expense(description, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public double getTotalAmount() {
        double totalAmount = 0.0;
        String query = "SELECT SUM(amount) FROM expenses";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                totalAmount = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}
