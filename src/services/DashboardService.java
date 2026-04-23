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
import databaseAccess.DashboardDAO;
import java.util.List;
import transaction.Transaction;

public class DashboardService {

    private static int getUserId() {
        return Session.currentUser.getId();
    }

    public static double getTotalBalance() {
        return DashboardDAO.totalBalance(getUserId());
    }

    public static List<Transaction> getTopFiveTransactions() {
        return DashboardDAO.topFiveTransactions(getUserId());
    }

    public static int getActiveBudgets() {
        return DashboardDAO.activeBudgets(getUserId());
    }

    public static int getAllGoals() {
        return DashboardDAO.allGoals(getUserId());
    }

    public static int getAchievedGoals() {
        return DashboardDAO.achievedGoals(getUserId());
    }
}