/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseAccess;


/**
 *
 * @author MH
 */

import model.Goal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.GoalStatus;

public class GoalDAO {

    // ================= INSERT =================
    public void insertGoal(Goal goal) {

        String sql = "INSERT INTO goals(user_id, name, target_amount, current_amount, deadline, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, goal.getUserId());
            ps.setString(2, goal.getName());
            ps.setDouble(3, goal.getTargetAmount());
            ps.setDouble(4, goal.getCurrentAmount());
            ps.setString(5, goal.getDeadline());
            ps.setString(6, goal.getStatus().name());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Insert Goal Error: " + e.getMessage());
        }
    }

    // ================= GET ALL =================
    public List<Goal> getGoalsByUser(int userId) {

        List<Goal> list = new ArrayList<>();

        String sql = "SELECT * FROM goals WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Goal g = new Goal();
                g.setId(rs.getInt("goal_id"));
                g.setUserId(rs.getInt("user_id"));
                g.setName(rs.getString("name"));
                g.setTargetAmount(rs.getDouble("target_amount"));
                g.setCurrentAmount(rs.getDouble("current_amount"));
                g.setDeadline(rs.getString("deadline"));
                g.setStatus(GoalStatus.valueOf(rs.getString("status")));
                list.add(g);
            }

        } catch (Exception e) {
            System.out.println("Fetch Goals Error: " + e.getMessage());
        }

        return list;
    }
}