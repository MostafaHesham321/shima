/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MH
 */
public class Goal {

    private int id;
    private int userId;

    private String name;
    private double targetAmount;
    private double currentAmount;
    private String deadline;
    private GoalStatus status;

    public Goal() {}

    public Goal(int userId, String name, double targetAmount,
                double currentAmount, String deadline) {
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = GoalStatus.ACTIVE;
    }

    // ================= SETTERS =================

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }
    
    
    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public String getDeadline() {
        return deadline;
    }

    public GoalStatus getStatus() {
        return status;
    }
}