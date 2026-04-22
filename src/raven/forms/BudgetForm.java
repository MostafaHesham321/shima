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

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

public class BudgetForm extends SimpleForm {

    private JPanel container;
    private JButton btnAdd;

    public BudgetForm() {
        init();
        loadBudgets();
    }

    private void init() {

        setLayout(new BorderLayout());

        // ================= TOP =================
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Budgets");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd = new JButton("New Budget");

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        // ================= CONTAINER =================
        container = new JPanel(new GridLayout(0, 2, 10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(container), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> openDialog());
    }

    // dummy load
    private void loadBudgets() {
        container.removeAll();
        container.revalidate();
        container.repaint();
    }

    // ================= CARD =================
    private JPanel createBudgetCard(String category, double budget, double spent, int alert) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setBackground(new Color(30, 30, 40));

        JLabel lblCat = new JLabel(category);
        lblCat.setForeground(Color.WHITE);
        lblCat.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblAmount = new JLabel("Spent: " + spent + " / Budget: " + budget);
        lblAmount.setForeground(Color.LIGHT_GRAY);

        int percent = (int) ((spent / budget) * 100);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setStringPainted(true);

        if (percent >= alert) {
            bar.setForeground(Color.RED);
        } else {
            bar.setForeground(new Color(0, 180, 255));
        }

        JLabel lblAlert = new JLabel("Alert at " + alert + "%");
        lblAlert.setForeground(Color.GRAY);

        card.add(lblCat);
        card.add(Box.createVerticalStrut(8));
        card.add(lblAmount);
        card.add(Box.createVerticalStrut(8));
        card.add(bar);
        card.add(Box.createVerticalStrut(5));
        card.add(lblAlert);

        return card;
    }

    // ================= DIALOG =================
    private void openDialog() {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog overlay = new JDialog(frame);
        overlay.setUndecorated(true);
        overlay.setSize(frame.getSize());
        overlay.setLocationRelativeTo(frame);
        overlay.setBackground(new Color(0, 0, 0, 180));
        overlay.setLayout(new GridBagLayout());

        // ================= CARD =================
        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, fillx, insets 20",
                "[grow,fill]"
        ));

        panel.setPreferredSize(new Dimension(420, 420));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:25; background:$Panel.background;");

        // ================= INPUTS =================

        JTextField txtCategory = new JTextField();

        JTextField txtBudget = new JTextField();

        JTextField txtStartDate = new JTextField(
                new SimpleDateFormat("yyyy-MM-dd").format(new Date())
        );

        JTextField txtEndDate = new JTextField();

        JTextField txtAlert = new JTextField();

        JButton btnSave = new JButton("Save Budget");

        panel.add(new JLabel("Category"));
        panel.add(txtCategory);

        panel.add(new JLabel("Budget Amount"));
        panel.add(txtBudget);

        panel.add(new JLabel("Start Date (yyyy-MM-dd)"));
        panel.add(txtStartDate);

        panel.add(new JLabel("End Date (yyyy-MM-dd)"));
        panel.add(txtEndDate);

        panel.add(new JLabel("Alert Threshold %"));
        panel.add(txtAlert);

        panel.add(btnSave);

        // ================= CLOSE OUTSIDE =================
        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                overlay.dispose();
            }
        });

        // prevent closing when clicking inside
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                e.consume();
            }
        });

        // ================= SAVE =================
        btnSave.addActionListener(e -> {

            String category = txtCategory.getText();
            double budget = Double.parseDouble(txtBudget.getText());
            int alert = Integer.parseInt(txtAlert.getText());

            // dummy spent
            double spent = budget * 0.4;

            container.add(createBudgetCard(category, budget, spent, alert));

            container.revalidate();
            container.repaint();

            overlay.dispose();
        });

        overlay.add(panel);
        overlay.setVisible(true);
    }
}