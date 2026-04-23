/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseAccess;

/**
 *
 * @author MH
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transaction.*;


public class TransactionDAO {

    // ================= INSERT =================
    public static void insert(Transaction t) {

        String sql = "INSERT INTO transactions " +
                "(user_id, amount, date, description, type, source, category, payment_method) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getUserId());
            ps.setDouble(2, t.getAmount());

            String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(t.getDate());
            ps.setString(3, date);

            ps.setString(4, t.getDescription());

            String type;
            String source = null;
            String category = null;
            String payment = null;

            if (t instanceof Income) {

                type = "INCOME";
                source = ((Income) t).getSource().name();

            } else {

                type = "EXPENSE";
                Expense expense = (Expense) t;
                category = expense.getCategory().name();
                payment = expense.getPaymentMethod().name();
            }

            ps.setString(5, type);
            ps.setString(6, source);
            ps.setString(7, category);
            ps.setString(8, payment);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Insert Transaction Error: " + e.getMessage());
        }
    }

    // ================= GET ALL (UNCHANGED) =================
    public static List<Transaction> getAll(int userId) {

        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("transaction_id");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");

                java.util.Date date =
                        new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .parse(rs.getString("date"));

                String type = rs.getString("type");

                if (type.equals("INCOME")) {

                    Income i = new Income(
                            id,
                            amount,
                            date,
                            description,
                            IncomeSource.valueOf(rs.getString("source"))
                    );

                    i.setUserId(userId);
                    list.add(i);

                } else {

                    Expense e = new Expense(
                            id,
                            amount,
                            date,
                            description,
                            PaymentMethod.valueOf(rs.getString("payment_method")),
                            ExpenseCategory.valueOf(rs.getString("category"))
                    );

                    e.setUserId(userId);
                    list.add(e);
                }
            }

        } catch (Exception e) {
            System.out.println("GetAll Error: " + e.getMessage());
        }

        return list;
    }

    // ================= DELETE =================
    public static void delete(int transactionId, int userId) {

        String sql = "DELETE FROM transactions WHERE transaction_id=? AND user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transactionId);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    // ================= UPDATE =================
    public static void update(Transaction t) {

        String sql = "UPDATE transactions SET amount=?, date=?, description=?, " +
                "source=?, category=?, payment_method=? WHERE transaction_id=? AND user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, t.getAmount());

            String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(t.getDate());
            ps.setString(2, date);

            ps.setString(3, t.getDescription());

            String source = null;
            String category = null;
            String payment = null;

            if (t instanceof Income) {
                source = ((Income) t).getSource().name();
            } else {
                Expense e = (Expense) t;
                category = e.getCategory().name();
                payment = e.getPaymentMethod().name();
            }

            ps.setString(4, source);
            ps.setString(5, category);
            ps.setString(6, payment);

            ps.setInt(7, t.getId());
            ps.setInt(8, t.getUserId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Update Transaction Error: " + e.getMessage());
        }
    }
}