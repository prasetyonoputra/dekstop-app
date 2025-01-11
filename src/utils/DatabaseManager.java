package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

    public Expense addExpense(Expense expense) {
        String query = "INSERT INTO expenses (description, amount, created_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setDate(3, new java.sql.Date(expense.getCreatedDate().getTime()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        expense.setId(id);
                    }
                }
            }

            return expense;
        } catch (SQLException e) {
            e.printStackTrace();

            return expense;
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
        String query = "SELECT * FROM expenses";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                long id = Long.valueOf(rs.getInt("id"));
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                Date createdDate = rs.getDate("created_date");
                Date updatedDate = rs.getDate("updated_date");
                expenses.add(new Expense(id, description, amount, createdDate, updatedDate));
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
