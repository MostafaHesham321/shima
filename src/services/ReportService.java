/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author MH
 */

import databaseAccess.ReportDAO;
import java.util.*;
import transaction.ExpenseCategory;

public class ReportService {

    public static Map<String, Double> getExpensePercentages(int userId) {

        Map<ExpenseCategory, Double> raw =
                ReportDAO.expenseByCategory(userId);

        double total = raw.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        Map<String, Double> result = new LinkedHashMap<>();

        for (ExpenseCategory c : ExpenseCategory.values()) {

            double value = raw.getOrDefault(c, 0.0);

            double percent = total == 0 ? 0 : (value / total) * 100;

            result.put(c.name(), percent);
        }

        return result;
    }

}
