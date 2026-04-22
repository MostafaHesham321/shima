/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseAccess;


/**
 *
 * @author MH
 */

import java.text.SimpleDateFormat;
import model.Goal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.GoalStatus;

public class GoalDAO {

    // ================= INSERT =================
    public static void insertGoal(int userId, Goal goal) {

        updateStatus(goal); // 🔥 important

        String sql = "INSERT INTO goals (user_id, name, target_amount, current_amount, deadline, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, goal.getName());
            ps.setDouble(3, goal.getTargetAmount());
            ps.setDouble(4, goal.getCurrentAmount());

            if (goal.getDeadline() != null) {
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .format(goal.getDeadline());
                ps.setString(5, date);
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }

            ps.setString(6, goal.getStatus().name());
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("Insert Goal Error: " + e.getMessage());
        }
    }

    // ================= GET BY USER =================
    public static List<Goal> getGoalsByUser(int userId) {

        List<Goal> list = new ArrayList<>();

        String sql = "SELECT * FROM goals WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Goal g = new Goal();

                g.setGoalId(rs.getInt("goal_id"));
                g.setUserId(rs.getInt("user_id"));
                g.setName(rs.getString("name"));
                g.setTargetAmount(rs.getDouble("target_amount"));
                g.setCurrentAmount(rs.getDouble("current_amount"));

                String dateStr = rs.getString("deadline");
                if (dateStr != null) {
                    g.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
                }

                g.setStatus(GoalStatus.valueOf(rs.getString("status")));

                list.add(g);
            }

        } catch (Exception e) {
            System.out.println("Get Goals Error: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    
    public static void updateProgress(int userId, int goalId, int amount) {

        String selectSQL = "SELECT current_amount, target_amount FROM goals WHERE goal_id = ? AND user_id = ?";
        String updateSQL = "UPDATE goals SET current_amount = ?, status = ? WHERE goal_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {

            selectStmt.setInt(1, goalId);
            selectStmt.setInt(2, userId);

            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {

                double current = rs.getDouble("current_amount");
                double target = rs.getDouble("target_amount");


                double newAmount = current + amount;


                GoalStatus status;
                if (newAmount >= target) {
                    status = GoalStatus.COMPLETED;
                    newAmount = target; 
                } else {
                    status = GoalStatus.ACTIVE;
                }


                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

                    updateStmt.setDouble(1, newAmount);
                    updateStmt.setString(2, status.name());
                    updateStmt.setInt(3, goalId);
                    updateStmt.setInt(4, userId);

                    updateStmt.executeUpdate();
                }
            }
            
        } catch (Exception e) {
            System.out.println("Update Goal Error: " + e.getMessage());
        }

    }
  
  
    public static void deleteGoal(int userId, int goalId) {

        String sql = "DELETE FROM goals WHERE goal_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, goalId);
            ps.setInt(2, userId);

            ps.executeUpdate();



        } catch (Exception e) {
            System.out.println("Delete Goal Error: " + e.getMessage());

        }
    }

    
    public static void editGoal(int userId, Goal goal) {

        updateStatus(goal); // 🔥 same logic reused

        String sql = "UPDATE goals SET name = ?, target_amount = ?, current_amount = ?, deadline = ?, status = ? " +
                     "WHERE goal_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, goal.getName());
            ps.setDouble(2, goal.getTargetAmount());
            ps.setDouble(3, goal.getCurrentAmount());

            if (goal.getDeadline() != null) {
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .format(goal.getDeadline());
                ps.setString(4, date);
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            ps.setString(5, goal.getStatus().name());

            ps.setInt(6, goal.getGoalId());
            ps.setInt(7, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Update Goal Error: " + e.getMessage());

        }
    }

   
    
    private static void updateStatus(Goal goal) {

        if (goal.getCurrentAmount() >= goal.getTargetAmount()) {
            goal.setStatus(GoalStatus.COMPLETED);
        } else {
            goal.setStatus(GoalStatus.ACTIVE);
        }
    }
    
    
    
    
    // ================= REMAINING AMOUNT =================
    public static Double getRemainingAmount(int userId, int goalId) {

        String sql = "SELECT target_amount, current_amount FROM goals WHERE goal_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, goalId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double target = rs.getDouble("target_amount");
                double current = rs.getDouble("current_amount");

                return target - current;
            }

        } catch (Exception e) {
            System.out.println("Get Remaining Error: " + e.getMessage());
        }

        return null;
    }
}