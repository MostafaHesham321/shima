/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.ArrayList;
import transaction.ExpenseCategory;
public class Budget {

    private int budgetId;
    private double limit;
    private double spent;
    private int month;
    private int year;
    private List<ExpenseCategory> categories;

    // ================= CONSTRUCTOR =================
    public Budget() {
        categories = new ArrayList<>();
    }

    public Budget(int budgetId, double limit, double spent, int month, int year) {
        this.budgetId = budgetId;
        this.limit = limit;
        this.spent = spent;
        this.month = month;
        this.year = year;
        this.categories = new ArrayList<>();
    }

    // ================= GETTERS & SETTERS =================
    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be > 0");
        }
        this.limit = limit;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        if (spent < 0) {
            throw new IllegalArgumentException("Spent cannot be negative");
        }
        this.spent = spent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be 1-12");
        }
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ExpenseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ExpenseCategory> categories) {
        this.categories = categories;
    }

    // ================= BUSINESS METHODS =================

    // remaining = limit - spent
    public double calculateRemaining() {
        return limit - spent;
    }

    // alert لو صرف 80% أو أكتر
    public boolean isAlertTriggered() {
        if (limit == 0) return false;
        double percent = (spent / limit) * 100;
        return percent >= 80; // تقدر تغيرها
    }

    // ================= UTILS =================
    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", limit=" + limit +
                ", spent=" + spent +
                ", month=" + month +
                ", year=" + year +
                ", remaining=" + calculateRemaining() +
                '}';
    }
}