/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.forms;

/**
 *
 * @author MH
 */
import raven.components.SimpleForm;
import services.DashboardService;
import transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardForm extends SimpleForm {

    private JPanel container;

    public DashboardForm() {
        init();
        loadData();
    }

    private void init() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        container = new JPanel(new GridLayout(0, 2, 10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(container), BorderLayout.CENTER);
    }

    private void loadData() {

        container.removeAll();

        // ================= CARDS =================
        container.add(createCard("Balance", DashboardService.getTotalBalance(), new Color(0, 200, 255)));
        container.add(createCard("Active Budgets", DashboardService.getActiveBudgets(), Color.ORANGE));

        container.add(createCard("Goals", DashboardService.getAllGoals(), Color.CYAN));
        container.add(createCard("Achieved Goals", DashboardService.getAchievedGoals(), Color.GREEN));

        // ================= LAST TRANSACTIONS =================
        container.add(createTransactionsPanel());

        container.revalidate();
        container.repaint();
    }

    // ================= CARD =================
    private JPanel createCard(String title, double value, Color color) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(String.valueOf(value));
        lblValue.setForeground(color);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, int value, Color color) {
        return createCard(title, (double) value, color);
    }

    // ================= TRANSACTIONS =================
    private JPanel createTransactionsPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Last Transactions");
        title.setForeground(Color.WHITE);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        List<Transaction> list = DashboardService.getTopFiveTransactions();

        for (Transaction t : list) {

            JLabel item = new JLabel(
                    t.getDescription() + " - " + t.getAmount()
            );

            item.setForeground(
                    t instanceof transaction.Income
                            ? new Color(34, 197, 94)
                            : new Color(239, 68, 68)
            );

            listPanel.add(item);
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);

        return panel;
    }
}