/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseAccess;

/**
 *
 * @author MH
 */
import transaction.ExpenseCategory;
import model.Budget;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BudgetDAO {

    //     ================= INSERT =================
    public static int insertBudget(int userId, Budget budget) {

        String sql = "INSERT INTO budgets " +
                "(user_id, category, budget_limit, spent, start_date, end_date, alert_threshold) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (budget.getStartDate() == null || budget.getEndDate() == null) {
                throw new RuntimeException("Start and End dates are required");
            }

            ps.setInt(1, userId);
            ps.setString(2, budget.getCategory().name());
            ps.setDouble(3, budget.getBudgetLimit());
            ps.setDouble(4, 0.0);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            ps.setString(5, df.format(budget.getStartDate()));
            ps.setString(6, df.format(budget.getEndDate()));
            ps.setInt(7, budget.getAlertThreshold());

            ps.executeUpdate();

            // 🔥 هنا بقى نجيب الـ ID
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1);
                budget.setBudgetId(id); // مهم جدًا
                return id;
            }

        } catch (Exception e) {
            System.out.println("Insert Budget Error: " + e.getMessage());
            e.printStackTrace();
        }

        return -1; // لو فشل
    }
    // ================= GET BY USER =================
    public static List<Budget> getBudgetsByUser(int userId) {

        List<Budget> list = new ArrayList<>();

        String sql = "SELECT * FROM budgets WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Budget b = new Budget();

                b.setBudgetId(rs.getInt("budget_id"));
                b.setUserId(rs.getInt("user_id"));

                b.setCategory(ExpenseCategory.valueOf(rs.getString("category")));
                b.setBudgetLimit(rs.getDouble("budget_limit"));
                b.setSpent(rs.getDouble("spent"));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                b.setStartDate(df.parse(rs.getString("start_date")));
                b.setEndDate(df.parse(rs.getString("end_date")));

                b.setAlertThreshold(rs.getInt("alert_threshold"));

                list.add(b);
            }

        } catch (Exception e) {
            System.out.println("Get Budgets Error: " + e.getMessage());
        }

        return list;
    }

    // ================= UPDATE =================
    public static void updateBudget(int userId, Budget b) {

        String sql = "UPDATE budgets SET category=?, budget_limit=?, start_date=?, end_date=?, alert_threshold=? " +
                     "WHERE budget_id=? AND user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, b.getCategory().name());
            ps.setDouble(2, b.getBudgetLimit());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            ps.setString(3, df.format(b.getStartDate()));
            ps.setString(4, df.format(b.getEndDate()));

            ps.setInt(5, b.getAlertThreshold());
            ps.setInt(6, b.getBudgetId());
            ps.setInt(7, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Update Budget Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // ================= DELETE =================
    public static void deleteBudget(int userId, int budgetId) {

        String sql = "DELETE FROM budgets WHERE budget_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, budgetId);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Delete Budget Error: " + e.getMessage());
        }
    }

    // ================= calculateSpent =================
    public static double calculateSpent(int userId, ExpenseCategory category, Date start, Date end) {

        String sql = "SELECT SUM(amount) FROM transactions " +
                     "WHERE user_id=? AND type='EXPENSE' " +
                     "AND category=? AND date BETWEEN ? AND ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, category.name());

            ps.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(start));
            ps.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(end));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public static void updateSpent(int userId, Budget b) {

        double total = calculateSpent(
                userId,
                b.getCategory(),
                b.getStartDate(),
                b.getEndDate()
        );
        

        String sql = "UPDATE budgets SET spent = ? WHERE budget_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, total);
            ps.setInt(2, b.getBudgetId());
            System.out.println(total);
            System.out.println(b.getBudgetId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Update Spent Error: " + e.getMessage());
        }
    }
    
    
    
    public static List<Budget> getAffectedBudgets(int userId, ExpenseCategory category, Date date) {

    List<Budget> list = new ArrayList<>();

    String sql = "SELECT * FROM budgets WHERE user_id=? AND category=? " +
                 "AND date(?) BETWEEN date(start_date) AND date(end_date)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ps.setString(2, category.name());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ps.setString(3, df.format(date));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Budget b = new Budget();

            b.setBudgetId(rs.getInt("budget_id"));
            b.setUserId(rs.getInt("user_id"));
            b.setCategory(ExpenseCategory.valueOf(rs.getString("category")));
            b.setBudgetLimit(rs.getDouble("budget_limit"));
            b.setSpent(rs.getDouble("spent"));

            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            b.setStartDate(d.parse(rs.getString("start_date")));
            b.setEndDate(d.parse(rs.getString("end_date")));

            b.setAlertThreshold(rs.getInt("alert_threshold"));

            list.add(b);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}