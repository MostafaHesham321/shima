/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.forms;

import java.util.Date;
import raven.components.SimpleForm;
import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
//import services.GoalService;
import net.miginfocom.swing.MigLayout;
import model.Goal;
import model.GoalStatus;
import databaseAccess.GoalDAO;
import core.Session;
import java.util.List;

public class GoalsForm extends SimpleForm {
    private int userId = Session.currentUser.getId();
    private JPanel container;
    private JButton btnAdd;

    
    public GoalsForm() {
        init();
        loadGoals();
    }

    private void init() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Savings Goals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd = new JButton("New Goal");

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        container = new JPanel(new GridLayout(0, 2, 10, 10));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(container, BorderLayout.NORTH); // مهم

        JScrollPane scroll = new JScrollPane(centerWrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER); // 🔥 ده اللي يملى الشاشة

        btnAdd.addActionListener(e -> openDialog());
    }

    // ================= LOAD FROM DB =================
    private void loadGoals() {

        container.removeAll();

        List<Goal> goals = GoalDAO.getGoalsByUser(userId);

        for (Goal g : goals) {
            container.add(createGoalCard(g)); 
        }

        container.revalidate();
        container.repaint();
    }

    // ================= CARD =================
    private JPanel createGoalCard(Goal goal) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(30, 30, 40));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ================= CENTER =================
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel lblName = new JLabel(goal.getName());
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblAmount = new JLabel("$" + goal.getCurrentAmount() + " / $" + goal.getTargetAmount());
        lblAmount.setForeground(Color.LIGHT_GRAY);

        int percent = goal.getTargetAmount() == 0 ? 0 :
                (int) ((goal.getCurrentAmount() / goal.getTargetAmount()) * 100);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setForeground(goal.getStatus() == GoalStatus.COMPLETED ? Color.GREEN : Color.ORANGE);

        JLabel lblPercent = new JLabel(percent + "% achieved");
        lblPercent.setForeground(bar.getForeground());

        center.add(lblName);
        center.add(Box.createVerticalStrut(8));
        center.add(lblAmount);
        center.add(Box.createVerticalStrut(8));
        center.add(bar);
        center.add(Box.createVerticalStrut(5));
        center.add(lblPercent);

        // ================= ADD MONEY BUTTON =================
        JButton btnAddMoney = new JButton("Add Money");
        btnAddMoney.addActionListener(e -> openAddMoneyDialog(goal));

        if (goal.getStatus() == GoalStatus.COMPLETED) {
            btnAddMoney.setEnabled(false);
            btnAddMoney.setText("Completed");
        }

        center.add(Box.createVerticalStrut(10));
        center.add(btnAddMoney);

        // ================= MENU BUTTON (⋮) =================
        JButton menuBtn = new JButton("⋮");
        menuBtn.setFocusPainted(false);
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setForeground(Color.LIGHT_GRAY);
        menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ================= POPUP =================
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(34, 38, 57));

        JMenuItem edit = new JMenuItem("Edit");
        JMenuItem delete = new JMenuItem("Delete");

        edit.setForeground(Color.WHITE);
        delete.setForeground(Color.RED);

        menu.add(edit);
        menu.add(delete);

        menuBtn.addActionListener(e -> menu.show(menuBtn, 0, menuBtn.getHeight()));

        // ================= ACTIONS =================

        // 🔧 EDIT
        edit.addActionListener(e -> {
            openEditDialog(goal);
        });

        // 🗑 DELETE
        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Delete this goal?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                
                
                GoalDAO.deleteGoal(userId,goal.getGoalId());
                loadGoals();
            }
        });

        // ================= RIGHT =================
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(menuBtn, BorderLayout.NORTH);

        // ================= LAYOUT =================
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private void openDialog() {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog overlay = new JDialog(frame);
        overlay.setUndecorated(true);
        overlay.setSize(frame.getSize());
        overlay.setLocationRelativeTo(frame);
        overlay.setBackground(new Color(0, 0, 0, 200));
        overlay.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, fillx, insets 20",
                "[grow,fill]"
        ));

        panel.setPreferredSize(new Dimension(400, 350));

        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:25;background:@background;"
        );

        JTextField txtName = new JTextField();
        JTextField txtTarget = new JTextField();
        JTextField txtCurrent = new JTextField();
        JTextField txtDate = new JTextField();

        JButton btnSave = new JButton("Create");

        panel.add(new JLabel("Goal Name"));
        panel.add(txtName);

        panel.add(new JLabel("Target Amount"));
        panel.add(txtTarget);

        panel.add(new JLabel("Current Amount"));
        panel.add(txtCurrent);

        panel.add(new JLabel("Deadline (yyyy-MM-dd)"));
        panel.add(txtDate);

        panel.add(btnSave);

        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                overlay.dispose();
            }
        });

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText();
                double target = Double.parseDouble(txtTarget.getText());
                double current = Double.parseDouble(txtCurrent.getText());
                Date deadline = parseDate(txtDate.getText());

                Goal g = new Goal();
                g.setName(name);
                g.setTargetAmount(target);
                g.setCurrentAmount(current);
                g.setDeadline(deadline);
                g.setStatus(GoalStatus.ACTIVE);

                GoalDAO.insertGoal(userId, g);

                loadGoals();

                overlay.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        overlay.add(panel);
        overlay.setVisible(true);
    }

    private void openAddMoneyDialog(Goal goal) {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(frame, "Add Money", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtAmount = new JTextField();
        JButton btnSave = new JButton("Add");

        panel.add(new JLabel("Amount to add"));
        panel.add(txtAmount);
        panel.add(btnSave);

        dialog.add(panel);

        btnSave.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(txtAmount.getText());

                GoalDAO.updateProgress(
                        userId,
                        goal.getGoalId(),
                        amount
                );

                loadGoals();

                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void openEditDialog(Goal goal) {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(frame, "Edit Goal", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField(goal.getName());
        JTextField txtTarget = new JTextField(String.valueOf(goal.getTargetAmount()));

        JButton btnSave = new JButton("Update");

        panel.add(new JLabel("Name"));
        panel.add(txtName);

        panel.add(new JLabel("Target"));
        panel.add(txtTarget);

        panel.add(btnSave);

        dialog.add(panel);

        btnSave.addActionListener(e -> {
            try {
                goal.setName(txtName.getText());
                goal.setTargetAmount(Double.parseDouble(txtTarget.getText()));

                GoalDAO.editGoal(userId, goal);

                loadGoals();

                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
    
    
    
    
//    private void openDialog() {
//
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//
//        JDialog overlay = new JDialog(frame);
//        overlay.setUndecorated(true);
//        overlay.setSize(frame.getSize());
//        overlay.setLocationRelativeTo(frame);
//        overlay.setBackground(new Color(0, 0, 0, 200));
//        overlay.setLayout(new GridBagLayout());
//
//        JPanel panel = new JPanel(new MigLayout(
//                "wrap 1, fillx, insets 20",
//                "[grow,fill]"
//        ));
//
//        panel.setPreferredSize(new Dimension(400, 350));
//
//        panel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE,
//                "arc:25;background:@background;"
//        );
//
//        // ================= INPUTS =================
//        JTextField txtName = new JTextField();
//        JTextField txtTarget = new JTextField();
//        JTextField txtCurrent = new JTextField();
//        JTextField txtDate = new JTextField();
//
//        JLabel lblMsg = new JLabel(" ");
//        lblMsg.setForeground(Color.RED);
//
//        JButton btnSave = new JButton("Create");
//
//        panel.add(new JLabel("Goal Name"));
//        panel.add(txtName);
//
//        panel.add(new JLabel("Target Amount"));
//        panel.add(txtTarget);
//
//        panel.add(new JLabel("Current Amount"));
//        panel.add(txtCurrent);
//
//        panel.add(new JLabel("Deadline (yyyy-MM-dd)"));
//        panel.add(txtDate);
//
//        panel.add(lblMsg);
//        panel.add(btnSave);
//
//        // ================= CLOSE =================
//        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                overlay.dispose();
//            }
//        });
//
//        // ================= SAVE (SERVICE ONLY) =================
//        btnSave.addActionListener(e -> {
//
//            try {
//
//                Goal g = new Goal();
//                g.setName(txtName.getText());
//                g.setTargetAmount(Double.parseDouble(txtTarget.getText()));
//                g.setCurrentAmount(Double.parseDouble(txtCurrent.getText()));
//                g.setDeadline(parseDate(txtDate.getText()));
//
//                // 🔥 كل logic هنا في service
//                String result = GoalService.createGoal(g);
//
//                if ("SUCCESS".equals(result)) {
//
//                    lblMsg.setForeground(new Color(0, 180, 0));
//                    lblMsg.setText("Goal created successfully");
//
//                    loadGoals();
//
//                    new javax.swing.Timer(500, ev -> overlay.dispose()).start();
//
//                } else {
//                    lblMsg.setForeground(Color.RED);
//                    lblMsg.setText(result);
//                }
//
//            } catch (Exception ex) {
//                lblMsg.setForeground(Color.RED);
//                lblMsg.setText("Invalid input");
//            }
//        });
//
//        overlay.add(panel);
//        overlay.setVisible(true);
//    }
//    private void openAddMoneyDialog(Goal goal) {
//
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//
//        JDialog dialog = new JDialog(frame, "Add Progress", true);
//        dialog.setSize(320, 220);
//        dialog.setLocationRelativeTo(frame);
//
//        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        JTextField txtAmount = new JTextField();
//        JLabel lblMsg = new JLabel(" ");
//        JButton btnSave = new JButton("Add");
//
//        panel.add(new JLabel("Amount"));
//        panel.add(txtAmount);
//        panel.add(lblMsg);
//        panel.add(btnSave);
//
//        dialog.add(panel);
//
//        btnSave.addActionListener(e -> {
//
//            try {
//
//                int amount = Integer.parseInt(txtAmount.getText());
//
//                // 🔥 Business logic في Service
//                String result = GoalService.addProgress(goal, amount);
//
//                lblMsg.setText(result);
//
//                if (result.startsWith("SUCCESS")) {
//                    lblMsg.setForeground(new Color(0, 180, 0));
//
//                    loadGoals();
//
//                    new javax.swing.Timer(500, ev -> dialog.dispose()).start();
//                } else {
//                    lblMsg.setForeground(Color.RED);
//                }
//
//            } catch (NumberFormatException ex) {
//                lblMsg.setForeground(Color.RED);
//                lblMsg.setText("Invalid number");
//            }
//        });
//
//        dialog.setVisible(true);
//    }
//    private void openEditDialog(Goal goal) {
//
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//
//        JDialog dialog = new JDialog(frame, "Edit Goal", true);
//        dialog.setSize(350, 280);
//        dialog.setLocationRelativeTo(frame);
//
//        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        JTextField txtName = new JTextField(goal.getName());
//        JTextField txtTarget = new JTextField(String.valueOf(goal.getTargetAmount()));
//        JTextField txtCurrent = new JTextField(String.valueOf(goal.getCurrentAmount()));
//        JTextField txtDate = new JTextField(goal.getDeadline() != null ? goal.getDeadline().toString() : "");
//
//        JLabel lblMsg = new JLabel(" ");
//        JButton btnSave = new JButton("Update");
//
//        panel.add(new JLabel("Name"));
//        panel.add(txtName);
//
//        panel.add(new JLabel("Target"));
//        panel.add(txtTarget);
//
//        panel.add(new JLabel("Current"));
//        panel.add(txtCurrent);
//
//        panel.add(new JLabel("Deadline (yyyy-MM-dd)"));
//        panel.add(txtDate);
//
//        panel.add(lblMsg);
//        panel.add(btnSave);
//
//        dialog.add(panel);
//
//        btnSave.addActionListener(e -> {
//
//            try {
//
//                goal.setName(txtName.getText());
//                goal.setTargetAmount(Double.parseDouble(txtTarget.getText()));
//                goal.setCurrentAmount(Double.parseDouble(txtCurrent.getText()));
//                goal.setDeadline(parseDate(txtDate.getText()));
//
//                String result = GoalService.updateGoal(goal);
//
//                lblMsg.setText(result);
//
//                if (result.startsWith("SUCCESS")) {
//                    lblMsg.setForeground(new Color(0, 180, 0));
//
//                    loadGoals();
//
//                    new javax.swing.Timer(500, ev -> dialog.dispose()).start();
//                } else {
//                    lblMsg.setForeground(Color.RED);
//                }
//
//            } catch (Exception ex) {
//                lblMsg.setForeground(Color.RED);
//                lblMsg.setText(ex.getMessage());
//            }
//        });
//
//        dialog.setVisible(true);
//    }
    
    private Date parseDate(String text) {
    try {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(text);
    } catch (Exception e) {
        return new Date();
    }
}
}
