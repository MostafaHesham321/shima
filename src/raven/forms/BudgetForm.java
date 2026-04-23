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
import transaction.ExpenseCategory;
import model.Budget;
import java.util.ArrayList;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import services.BudgetService;

public class BudgetForm extends SimpleForm {

    private JPanel container;
    private JButton btnAdd;

    public BudgetForm() {
        init();
        loadBudgets();
    }

    
    
    private void init() {

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Budgets");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd = new JButton("New Budget");

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        container = new JPanel(new GridLayout(0, 2, 10, 10));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(container, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> openDialog());
    }

    // ================= LOAD =================
    private void loadBudgets() {

        container.removeAll();

        List<Budget> list = BudgetService.getAllBudgets();

        for (Budget b : list) {
            container.add(createCard(b));
        }

        container.revalidate();
        container.repaint();
    }

    // ================= CARD =================
    private JPanel createCard(Budget b) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(30, 30, 40));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel lblCat = new JLabel(b.getCategory().name());
        lblCat.setForeground(Color.WHITE);
        lblCat.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblAmount = new JLabel("$" + b.getSpent() + " / $" + b.getBudgetLimit());
        lblAmount.setForeground(Color.LIGHT_GRAY);

        int percent = (int) ((b.getSpent() / b.getBudgetLimit()) * 100);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);

        if (percent >= b.getAlertThreshold()) {
            bar.setForeground(Color.RED);
        } else {
            bar.setForeground(Color.ORANGE);
        }

        JLabel lblPercent = new JLabel(percent + "% used");
        lblPercent.setForeground(bar.getForeground());

        center.add(lblCat);
        center.add(Box.createVerticalStrut(8));
        center.add(lblAmount);
        center.add(Box.createVerticalStrut(8));
        center.add(bar);
        center.add(Box.createVerticalStrut(5));
        center.add(lblPercent);

        // ================= MENU =================
        JButton menuBtn = new JButton("⋮");
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setForeground(Color.LIGHT_GRAY);

        JPopupMenu menu = new JPopupMenu();

        JMenuItem edit = new JMenuItem("Edit");
        JMenuItem delete = new JMenuItem("Delete");

        edit.setForeground(Color.WHITE);
        delete.setForeground(Color.RED);

        menu.add(edit);
        menu.add(delete);

        menuBtn.addActionListener(e -> menu.show(menuBtn, 0, menuBtn.getHeight()));

        delete.addActionListener(e -> {
            BudgetService.deleteBudget(b.getBudgetId());
            loadBudgets();
        });

        edit.addActionListener(e -> {
            openEditDialog(b);
        });
        
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(menuBtn, BorderLayout.NORTH);

        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    // ================= DIALOG =================
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

        panel.setPreferredSize(new Dimension(400, 400));
        panel.setBackground(new Color(30, 30, 40));

        // ================= INPUTS =================
        JComboBox<ExpenseCategory> cbCategory =
                new JComboBox<>(ExpenseCategory.values());

        JTextField txtLimit = new JTextField();
        JTextField txtStart = new JTextField();
        JTextField txtEnd = new JTextField();
        JTextField txtAlert = new JTextField();

        JLabel lblMsg = new JLabel(" ");
        JButton btnSave = new JButton("Create Budget");

        panel.add(new JLabel("Category"));
        panel.add(cbCategory);

        panel.add(new JLabel("Budget Limit"));
        panel.add(txtLimit);

        panel.add(new JLabel("Start Date (yyyy-MM-dd)"));
        panel.add(txtStart);

        panel.add(new JLabel("End Date (yyyy-MM-dd)"));
        panel.add(txtEnd);

        panel.add(new JLabel("Alert % (1-100)"));
        panel.add(txtAlert);

        panel.add(lblMsg);
        panel.add(btnSave);

        // ================= SAVE =================
        btnSave.addActionListener(e -> {
            try {
                Budget b = new Budget();

                b.setCategory((ExpenseCategory) cbCategory.getSelectedItem());
                b.setBudgetLimit(Double.parseDouble(txtLimit.getText()));
                b.setStartDate(parseDate(txtStart.getText()));
                b.setEndDate(parseDate(txtEnd.getText()));
                b.setAlertThreshold(Integer.parseInt(txtAlert.getText()));

                BudgetService.createBudget( b);

                
                lblMsg.setForeground(new Color(0, 180, 0));
                lblMsg.setText("Created successfully");


                loadBudgets();

                new Timer(500, ev -> overlay.dispose()).start();

            } catch (Exception ex) {
                lblMsg.setForeground(Color.RED);
                lblMsg.setText("Error: " + ex.getMessage());
            }
        });



        // ================= CLOSE OUTSIDE =================
        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                overlay.dispose();
            }
        });

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                e.consume();
            }
        });
        
        
        
        
        overlay.add(panel);
        overlay.setVisible(true);
    }

    // ================= DATE =================
    private java.util.Date parseDate(String text) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (Exception e) {
            return new java.util.Date();
        }
    }
    
    
    private String formatDate(Date date) {
        if (date == null) return "";
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    
    private void openEditDialog(Budget budget) {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // ================= OVERLAY =================
        JDialog overlay = new JDialog(frame);
        overlay.setUndecorated(true);
        overlay.setSize(frame.getSize());
        overlay.setLocationRelativeTo(frame);
        overlay.setBackground(new Color(0, 0, 0, 200));
        overlay.setLayout(new GridBagLayout());

        // ================= PANEL =================
        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, fillx, insets 20",
                "[grow,fill]"
        ));

        panel.setPreferredSize(new Dimension(400, 400));
        panel.setBackground(new Color(30, 30, 40));

        // ================= INPUTS (FILLED 🔥) =================
        JComboBox<ExpenseCategory> cbCategory =
                new JComboBox<>(ExpenseCategory.values());
        cbCategory.setSelectedItem(budget.getCategory());

        JTextField txtLimit =
                new JTextField(String.valueOf(budget.getBudgetLimit()));

        JTextField txtStart =
                new JTextField(formatDate(budget.getStartDate()));

        JTextField txtEnd =
                new JTextField(formatDate(budget.getEndDate()));

        JTextField txtAlert =
                new JTextField(String.valueOf(budget.getAlertThreshold()));

        JLabel lblMsg = new JLabel(" ");
        JButton btnSave = new JButton("Update Budget");

        // ================= UI =================
        panel.add(new JLabel("Category"));
        panel.add(cbCategory);

        panel.add(new JLabel("Budget Limit"));
        panel.add(txtLimit);

        panel.add(new JLabel("Start Date (yyyy-MM-dd)"));
        panel.add(txtStart);

        panel.add(new JLabel("End Date (yyyy-MM-dd)"));
        panel.add(txtEnd);

        panel.add(new JLabel("Alert % (1-100)"));
        panel.add(txtAlert);

        panel.add(lblMsg);
        panel.add(btnSave);

        // ================= CLOSE OUTSIDE =================
        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                overlay.dispose();
            }
        });

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                e.consume();
            }
        });

        // ================= SAVE =================
        btnSave.addActionListener(e -> {
            try {

                budget.setCategory((ExpenseCategory) cbCategory.getSelectedItem());
                budget.setBudgetLimit(Double.parseDouble(txtLimit.getText()));
                budget.setStartDate(parseDate(txtStart.getText()));
                budget.setEndDate(parseDate(txtEnd.getText()));
                budget.setAlertThreshold(Integer.parseInt(txtAlert.getText()));

                BudgetService.updateBudget(budget);

                lblMsg.setForeground(new Color(0, 180, 0));
                lblMsg.setText("Updated successfully");
                

                
                loadBudgets();

                new Timer(500, ev -> overlay.dispose()).start();

            } catch (Exception ex) {
                lblMsg.setForeground(Color.RED);
                lblMsg.setText("Error: " + ex.getMessage());
            }
        });

        overlay.add(panel);
        overlay.setVisible(true);
    }

}  
