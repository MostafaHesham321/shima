/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author MH
 */

import databaseAccess.BudgetDAO;
import databaseAccess.TransactionDAO;
import model.Budget;
import transaction.*;
import core.Session;
import java.util.Date;
import java.util.List;

public class TransactionService {
 
    private static void updateBudgetsForTransaction(Transaction t) {

        if (!(t instanceof Expense)) return;

        int userId = Session.currentUser.getId();

        Expense e = (Expense) t;

        List<Budget> affected = BudgetDAO.getAffectedBudgets(
                userId,
                e.getCategory(),
                e.getDate()
        );

        for (Budget b : affected) {
            BudgetDAO.updateSpent(userId, b);
        }
    }
    
    // ================= CREATE =================
    public static String createTransaction(Transaction t) {

        int userId = Session.currentUser.getId();

        String error = validateTransaction(t);
        if (error != null) return error;

        t.setUserId(userId);

        TransactionDAO.insert(t);

        // 🔥 update budgets
        updateBudgetsForTransaction(t);

        return "SUCCESS";
    }

    // ================= UPDATE =================
    public static String editTransaction(Transaction oldT, Transaction newT) {

        int userId = Session.currentUser.getId();

        String error = validateTransaction(newT);
        if (error != null) return error;

        newT.setUserId(userId);

        TransactionDAO.update(newT);

        // 🔥 update budgets (old + new)
        updateBudgetsForTransaction(oldT);
        updateBudgetsForTransaction(newT);

        return "SUCCESS";
    }

    // ================= DELETE =================
    public static void deleteTransaction(Transaction t) {

        int userId = Session.currentUser.getId();

        TransactionDAO.delete(t.getId(), userId);

        // 🔥 update budgets
        updateBudgetsForTransaction(t);
    }

    // ================= VALIDATION =================
    private static String validateTransaction(Transaction t) {

        if (t == null)
            return "Transaction cannot be null";

        if (t.getAmount() <= 0)
            return "Amount must be greater than 0";

        if (t.getDescription() == null || t.getDescription().trim().isEmpty())
            return "Description is required";

        if (t.getDate() == null)
            return "Date is required";

        // ================= EXPENSE =================
        if (t instanceof Expense) {

            Expense e = (Expense) t;

            if (e.getCategory() == null)
                return "Expense category is required";

            if (e.getPaymentMethod() == null)
                return "Payment method is required";
        }

        // ================= INCOME =================
        else if (t instanceof Income) {

            Income i = (Income) t;

            if (i.getSource() == null)
                return "Income source is required";
        }

        return null; // valid
    }

    // ================= GET ALL =================
    public static List<Transaction> getAllTransactions() {
        int userId = Session.currentUser.getId();
        return TransactionDAO.getAll(userId);
    }

    // ================= BUDGET LOGIC =================
//    private static void updateBudgets(int userId) {
//        // نفس اللوجيك اللي عندك أو نربطه بعدين
//    }
}