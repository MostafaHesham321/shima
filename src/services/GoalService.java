/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author MH
 */
import databaseAccess.GoalDAO;
import model.Goal;
import core.Session;
import java.util.Date;
import java.util.List;

public class GoalService {
    private static int userId = Session.currentUser.getId();
    
    
    // ================= CREATE =================
    public static String createGoal(Goal goal) {

        String validation = validateGoal(goal);
        if (validation != null) {
            return validation; // ❌ error message للـ UI
        }

        GoalDAO.insertGoal(userId, goal);
        return "SUCCESS";
    }

    // ================= UPDATE =================
    public static String updateGoal(Goal goal) {

        String validation = validateGoal(goal);
        if (validation != null) {
            return validation;
        }

        GoalDAO.editGoal(userId, goal);
        return "SUCCESS";
    }

    // ================= ADD MONEY =================
    public static String addProgress(Goal goal, int amount) {
        
        int goalId = goal.getGoalId();

        if (amount <= 0) {
            return "Amount must be greater than 0";
        }

        Double remaining = GoalDAO.getRemainingAmount(userId, goalId);

        if (amount > remaining) {
            return "Amount exceeds remaining: " + remaining;
        }

        GoalDAO.updateProgress(userId, goalId, amount);

        return "SUCCESS";
    }

    
    
    // ================= DELETE =================
    public static void deleteGoal(int goalId) {
        GoalDAO.deleteGoal(userId, goalId);
    }

    // ================= GET ALL =================
    public static List<Goal> getGoals() {
        return GoalDAO.getGoalsByUser(userId);
    }
    
    
    // ================= VALIDATION =================
    private static String validateGoal(Goal goal) {

        // Name
        if (goal.getName() == null || goal.getName().trim().isEmpty()) {
            return "Goal name cannot be empty";
        }

        if (goal.getName().length() > 100) {
            return "Goal name must be less than 100 characters";
        }

        // Target
        if (goal.getTargetAmount() <= 0) {
            return "Target amount must be greater than 0";
        }

        // Current
        if (goal.getCurrentAmount() < 0) {
            return "Current amount cannot be negative";
        }

        if (goal.getCurrentAmount() > goal.getTargetAmount()) {
            return "Current amount cannot exceed target";
        }

        // Deadline
        if (goal.getDeadline() != null) {
            Date now = new Date();
            if (goal.getDeadline().before(now)) {
                return "Deadline must be in the future";
            }
        }

        return null; // ✔ no error
    }
}