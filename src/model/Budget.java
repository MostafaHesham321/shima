/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import transaction.ExpenseCategory;

public class Budget {

    private int budgetId;
    private int userId;

    private ExpenseCategory category;
    private double budgetLimit;
    private double spent;

    private Date startDate;
    private Date endDate;

    private int alertThreshold;

    // ================= CONSTRUCTORS =================
    public Budget() {}

    public Budget(int budgetId, int userId, ExpenseCategory category,
                  double budgetLimit, double spent,
                  Date startDate, Date endDate, int alertThreshold) {

        this.budgetId = budgetId;
        this.userId = userId;
        this.category = category;
        this.budgetLimit = budgetLimit;
        this.spent = spent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alertThreshold = alertThreshold;
    }

    // ================= GETTERS & SETTERS =================

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }
    
}