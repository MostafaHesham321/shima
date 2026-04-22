/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.forms;

/**
 *
 * @author MH
 */
import core.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import transaction.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;
import raven.components.SimpleForm;
import databaseAccess.TransactionDAO;

public class TransactionForm extends SimpleForm {

    private JPanel container;
    private JButton btnAdd;

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public TransactionForm() {
        init();
        loadTransactions();
    }

    private void init() {

        setLayout(new BorderLayout());

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd = new JButton("Add Transaction");

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        // ===== CONTAINER =====
        container = new JPanel(new GridLayout(0, 2, 10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(container), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> openDialog());
    }

    // ================= Transactions DATA =================
    private void loadTransactions() {

        int userId = Session.currentUser.getId();

        transactions.clear();
        transactions.addAll(TransactionDAO.getAll(userId));

        renderTransactions();
    }

    // ================= RENDER =================
    private void renderTransactions() {

        container.removeAll();

        for (Transaction t : transactions) {
            container.add(createCard(t));
        }

        container.revalidate();
        container.repaint();
    }

    // ================= CARD UI =================
    private JPanel createCard(Transaction t) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(30, 30, 40));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ================= LEFT ICON =================
        JLabel icon = new JLabel("💳"); // هنطورها لاحقًا SVG
        icon.setForeground(Color.WHITE);
        icon.setPreferredSize(new Dimension(40, 40));

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setPreferredSize(new Dimension(45, 45));
        iconBox.setBackground(t instanceof Income
                ? new Color(34, 197, 94, 40)
                : new Color(239, 68, 68, 40)
        );
        iconBox.add(icon);

        // ================= CENTER INFO =================
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel name = new JLabel(t.getDescription());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel meta = new JLabel(
                "Category · " + String.valueOf(t.getDate())
        );
        meta.setForeground(new Color(140, 140, 160));
        meta.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        center.add(name);
        center.add(meta);

        // ================= RIGHT AMOUNT =================
        JLabel amount = new JLabel(
                (t instanceof Income ? "+" : "-") + t.getAmount()
        );
        amount.setForeground(t instanceof Income
                ? new Color(34, 197, 94)
                : new Color(239, 68, 68)
        );
        amount.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ================= OPTIONS BUTTON (⋮) =================
        JButton menuBtn = new JButton("⋮");
        menuBtn.setFocusPainted(false);
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setForeground(Color.LIGHT_GRAY);
        menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ================= MENU =================
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
        edit.addActionListener(e -> {
            System.out.println("EDIT: " + t.getDescription());
            // بعدين نربطه بـ DB update
        });

        delete.addActionListener(e -> {
            System.out.println("DELETE: " + t.getId());
            // بعدين TransactionDAO.delete(t.getId())
        });

        // ================= LAYOUT =================
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(iconBox, BorderLayout.WEST);

        JPanel middle = new JPanel(new BorderLayout());
        middle.setOpaque(false);
        middle.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(amount, BorderLayout.NORTH);
        right.add(menuBtn, BorderLayout.SOUTH);

        card.add(left, BorderLayout.WEST);
        card.add(middle, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    
    
    
    
    
    
    
    
    
    
    // ================= DIALOG =================
    private void openDialog() {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // ================= OVERLAY =================
        JDialog overlay = new JDialog(frame);
        overlay.setUndecorated(true);
        overlay.setSize(frame.getSize());
        overlay.setLocationRelativeTo(frame);
        overlay.setBackground(new Color(0, 0, 0, 200));
        overlay.setLayout(new GridBagLayout());

        // ================= CARD =================
        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, fillx, insets 20",
                "[grow,fill]"
        ));

        panel.setPreferredSize(new Dimension(420, 420));

        panel.setBackground(new Color(30, 30, 40));

        // ================= INPUTS =================
        JComboBox<String> cbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        JTextField txtDesc = new JTextField();
        JTextField txtAmount = new JTextField();
        JTextField txtDate = new JTextField();

        // optional switches
        JComboBox<IncomeSource> cbSource = new JComboBox<>(IncomeSource.values());
        JComboBox<ExpenseCategory> cbCategory = new JComboBox<>(ExpenseCategory.values());
        JComboBox<PaymentMethod> cbPayment = new JComboBox<>(PaymentMethod.values());

        JButton btnSave = new JButton("Save Transaction");

        // ================= UI =================
        panel.add(new JLabel("Type"));
        panel.add(cbType);

        panel.add(new JLabel("Description"));
        panel.add(txtDesc);

        panel.add(new JLabel("Amount"));
        panel.add(txtAmount);

        panel.add(new JLabel("Date (yyyy-MM-dd)"));
        panel.add(txtDate);

        panel.add(new JLabel("Source / Category"));
        panel.add(cbSource);

        panel.add(cbCategory);
        panel.add(cbPayment);

        panel.add(btnSave);

        // ================= TOGGLE LOGIC =================
        Runnable toggle = () -> {
            boolean income = cbType.getSelectedItem().equals("INCOME");

            cbSource.setEnabled(income);
            cbCategory.setEnabled(!income);
            cbPayment.setEnabled(!income);
        };

        cbType.addActionListener(e -> toggle.run());
        toggle.run();

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
            saveTransaction(
                    cbType,
                    txtDesc,
                    txtAmount,
                    txtDate,
                    cbSource,
                    cbCategory,
                    cbPayment,
                    overlay
            );
        });

        overlay.add(panel);
        overlay.setVisible(true);
    }

 
    
    
    private Date parseDate(String text) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (Exception e) {
            return new Date(); // fallback
        }
    }
    
    
    private void saveTransaction(
            JComboBox<String> cbType,
            JTextField txtDesc,
            JTextField txtAmount,
            JTextField txtDate,
            JComboBox<IncomeSource> cbSource,
            JComboBox<ExpenseCategory> cbCategory,
            JComboBox<PaymentMethod> cbPayment,
            JDialog overlay
    ) {

        try {

            String type = cbType.getSelectedItem().toString();
            String desc = txtDesc.getText();
            double amount = Double.parseDouble(txtAmount.getText());

            Transaction t;

            if (type.equals("INCOME")) {

                t = new Income(
                        0,
                        amount,
                        parseDate(txtDate.getText()),
                        desc,
                        (IncomeSource) cbSource.getSelectedItem()
                );

            } else {

                t = new Expense(
                        0,
                        amount,
                        parseDate(txtDate.getText()),
                        desc,
                        (PaymentMethod) cbPayment.getSelectedItem(),
                        (ExpenseCategory) cbCategory.getSelectedItem()
                );
            }

            // user id
            t.setUserId(Session.currentUser.getId());

            // DB insert
            boolean ok = TransactionDAO.insert(t);

            if (ok) {
                transactions.add(t);
                renderTransactions();
                overlay.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Insert Failed!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}