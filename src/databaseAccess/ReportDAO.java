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
import transaction.ExpenseCategory;
import java.util.Map;

public class ReportDAO {
    public static Map<ExpenseCategory, Double> expenseByCategory(int userId) {

        String sql =
            "SELECT category, SUM(amount) as total " +
            "FROM transactions " +
            "WHERE user_id=? AND type='EXPENSE' " +
            "GROUP BY category";

        Map<ExpenseCategory, Double> map = new HashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String cat = rs.getString("category");
                double total = rs.getDouble("total");

                if (cat != null) {
                    map.put(ExpenseCategory.valueOf(cat), total);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}