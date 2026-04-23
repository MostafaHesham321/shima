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
import java.util.*;
import transaction.*;
import model.*;

import java.text.SimpleDateFormat;


public class DashboardDAO {
    // ================= TOTAL BALANCE =================
    public static double totalBalance(int userId) {

        String sql = "SELECT " +
                "IFNULL(SUM(CASE WHEN type='INCOME' THEN amount ELSE 0 END),0) - " +
                "IFNULL(SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END),0) " +
                "FROM transactions WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= MONTHLY INCOME =================
    public static double monthlyIncome(int userId) {

        String sql =
            "SELECT IFNULL(SUM(amount),0) FROM transactions " +
            "WHERE user_id=? AND type='INCOME' " +
            "AND strftime('%Y-%m', date)=strftime('%Y-%m','now')";

        return getDouble(sql, userId);
    }

    // ================= MONTHLY EXPENSE =================
    public static double monthlyExpense(int userId) {

        String sql =
            "SELECT IFNULL(SUM(amount),0) FROM transactions " +
            "WHERE user_id=? AND type='EXPENSE' " +
            "AND strftime('%Y-%m', date)=strftime('%Y-%m','now')";

        return getDouble(sql, userId);
    }

    // ================= TOP 5 TRANSACTIONS =================
    public static List<Transaction> topFiveTransactions(int userId) {

        String sql =
            "SELECT * FROM transactions " +
            "WHERE user_id=? ORDER BY date DESC LIMIT 5";

        List<Transaction> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String type = rs.getString("type");

                Transaction t;

                // ================= INCOME =================
                if ("INCOME".equalsIgnoreCase(type)) {

                    t = new Income(
                            rs.getInt("transaction_id"),
                            rs.getDouble("amount"),
                            parseDate(rs.getString("date")),
                            rs.getString("description"),
                            null 
                    );

                }
                // ================= EXPENSE =================
                else {

                    t = new Expense(
                            rs.getInt("transaction_id"),
                            rs.getDouble("amount"),
                            parseDate(rs.getString("date")),
                            rs.getString("description"),
                            null, // payment method
                            null  // category
                    );
                }

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GOALS =================
    public static int allGoals(int userId) {

        String sql = "SELECT COUNT(*) FROM goals WHERE user_id=?";
        return getInt(sql, userId);
    }

    public static int achievedGoals(int userId) {

        String sql = "SELECT COUNT(*) FROM goals " +
                     "WHERE user_id=? AND current_amount >= target_amount";

        return getInt(sql, userId);
    }

    // ================= ACTIVE BUDGETS =================
    public static int activeBudgets(int userId) {

        String sql =
            "SELECT COUNT(*) FROM budgets " +
            "WHERE user_id=? " +
            "AND date('now','localtime') BETWEEN start_date AND end_date";

        return getInt(sql, userId);
    }

    // ================= HELPERS =================
    private static double getDouble(String sql, int userId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static int getInt(String sql, int userId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    
    
    private static java.util.Date parseDate(String text) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (Exception e) {
            return new java.util.Date();
    }
}
}