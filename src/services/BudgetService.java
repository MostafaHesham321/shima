/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author MH
 */

import core.Session;
import databaseAccess.BudgetDAO;
import java.util.HashSet;
import model.Budget;
import java.util.List;
import transaction.ExpenseCategory;

public class BudgetService {

    private static int getUserId() {
        return Session.currentUser.getId();
    }

    // ================= GET ALL =================
    public static List<Budget> getAllBudgets() {
        return BudgetDAO.getBudgetsByUser(getUserId());
    }

    // ================= CREATE =================
    public static void createBudget(Budget b) {
        int userId = getUserId();
        int budgetId = BudgetDAO.insertBudget(userId, b);
        b.setBudgetId(budgetId);
        BudgetDAO.updateSpent(userId, b);
    }

    // ================= UPDATE =================
    public static void updateBudget(Budget b) {
        int userId = getUserId();
        BudgetDAO.updateBudget(userId, b);
        BudgetDAO.updateSpent(userId, b);
    }

    // ================= DELETE =================
    public static void deleteBudget(int budgetId) {
        int userId = getUserId();
        BudgetDAO.deleteBudget(userId, budgetId);
    }
}
