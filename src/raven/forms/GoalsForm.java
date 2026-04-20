/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.forms;

import raven.components.SimpleForm;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
public class GoalsForm extends SimpleForm {

    private JPanel container;
    private JButton btnAdd;

    public GoalsForm() {
        init();
        loadGoals();
    }

    private void init() {
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Savings Goals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd = new JButton("New Goal");

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        // ===== CONTAINER =====
        container = new JPanel();
        container.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(container), BorderLayout.CENTER);

        // ===== EVENT =====
        btnAdd.addActionListener(e -> openDialog());
    }
    
    
    private void loadGoals() {

        container.removeAll();

        // 🔥 مؤقت (بعد كده تجيب من DB)
        ArrayList<String> names = new ArrayList<>();
        names.add("Car");
        names.add("Emergency Fund");

        for (int i = 0; i < names.size(); i++) {

            JPanel card = createGoalCard(
                    names.get(i),
                    300 + i * 100,
                    1000,
                    i % 2 == 0 ? Color.CYAN : Color.PINK
            );

            container.add(card);
        }

        container.revalidate();
        container.repaint();
    }
    



    private JPanel createGoalCard(String name, double current, double target, Color color) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setBackground(new Color(30, 30, 40));

        JLabel lblName = new JLabel(name);
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblAmount = new JLabel("$" + current + " / $" + target);
        lblAmount.setForeground(Color.LIGHT_GRAY);

        int percent = (int) ((current / target) * 100);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setForeground(color);

        JLabel lblPercent = new JLabel(percent + "% achieved");
        lblPercent.setForeground(color);

        card.add(lblName);
        card.add(Box.createVerticalStrut(10));
        card.add(lblAmount);
        card.add(Box.createVerticalStrut(10));
        card.add(bar);
        card.add(Box.createVerticalStrut(5));
        card.add(lblPercent);

        return card;
    }
    



    
//    private void openDialog() {
//
//        JDialog dialog = new JDialog();
//        dialog.setTitle("Create Goal");
//        dialog.setSize(400, 400);
//        dialog.setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(0, 1, 10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        JTextField txtName = new JTextField();
//        JTextField txtTarget = new JTextField();
//        JTextField txtCurrent = new JTextField();
//        JTextField txtDate = new JTextField();
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
//        panel.add(new JLabel("Deadline"));
//        panel.add(txtDate);
//
//        panel.add(btnSave);
//
//        dialog.add(panel);
//
//        // ===== SAVE =====
//        btnSave.addActionListener(e -> {
//
//            String name = txtName.getText();
//            double target = Double.parseDouble(txtTarget.getText());
//            double current = Double.parseDouble(txtCurrent.getText());
//
//            container.add(createGoalCard(name, current, target, Color.GREEN));
//
//            container.revalidate();
//            container.repaint();
//
//            dialog.dispose();
//        });
//
//        dialog.setVisible(true);
//    }
//}
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

        panel.setPreferredSize(new Dimension(400, 350));

        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:25;"
              + "background:@background;"
        );

        // ================= INPUTS =================
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

        panel.add(new JLabel("Deadline"));
        panel.add(txtDate);

        panel.add(btnSave);

        // ================= CLOSE WHEN CLICK OUTSIDE =================
        overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                overlay.dispose();
            }
        });

        // ================= SAVE =================
        btnSave.addActionListener(e -> {

            String name = txtName.getText();
            double target = Double.parseDouble(txtTarget.getText());
            double current = Double.parseDouble(txtCurrent.getText());

            container.add(createGoalCard(name, current, target, Color.GREEN));

            container.revalidate();
            container.repaint();

            overlay.dispose();
        });

        // ================= SHOW CENTER =================
        overlay.add(panel);
        overlay.setVisible(true);
    }
}
    