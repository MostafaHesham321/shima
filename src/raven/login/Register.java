/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.login;


import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.menu.FormManager;
import services.AuthService;
import core.Session;
import model.User;
import databaseAccess.UserDAO;

/**
 *
 * @author MH
 */
public class Register extends JPanel  {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton cmdCreate;
    private JLabel lblBackToLogin;
    private JLabel lblMessage;
    private JTextField txtEmail;
    
    public Register() {
        init();
    }

private void init() {

        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        // ================= FIELDS =================
        txtUsername = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        cmdCreate = new JButton("Create Account");

        JPanel panel = new JPanel(new MigLayout(
                "wrap,fillx,insets 35 45 30 45",
                "fill,250:280"
        ));

        // ================= STYLE =================
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;"
                        + "[light]background:darken(@background,3%);"
                        + "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter username");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm password");

        lblMessage = new JLabel("");
        lblMessage.setForeground(new java.awt.Color(220, 50, 50));

        JLabel title = new JLabel("Create Account");
        JLabel desc = new JLabel("Register to start using the system");

        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        desc.putClientProperty(FlatClientProperties.STYLE,
                "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%)");

        cmdCreate.putClientProperty(FlatClientProperties.STYLE,
                "borderWidth:0;focusWidth:0;innerFocusWidth:0");

        // ================= BACK LINK =================
        lblBackToLogin = new JLabel("<html><a href=''>Back to login</a></html>");
        lblBackToLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBackToLogin.putClientProperty(FlatClientProperties.STYLE,
                "foreground:#4A90E2;font:12");

        // ================= UI =================
        panel.add(title);
        panel.add(desc);

        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);

        panel.add(new JLabel("Email"), "gapy 8");
        panel.add(txtEmail);

        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);

        panel.add(new JLabel("Confirm Password"), "gapy 8");
        panel.add(txtConfirmPassword);

        panel.add(cmdCreate, "gapy 10");
        panel.add(lblMessage, "gapy 5");
        panel.add(lblBackToLogin, "gapy 5");

        add(panel);

        // ================= EVENTS =================
        cmdCreate.addActionListener(e -> {

            lblMessage.setText("");

            String name = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String confirm = new String(txtConfirmPassword.getPassword()).trim();

            String result = AuthService.register(name, email, password, confirm);

            if (result.equals("SUCCESS")) {

                User user = UserDAO.register(name, email, password);
                
                Session.currentUser = user;

            } else {
                lblMessage.setText(result);
            }
        });

        lblBackToLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                FormManager.gotoLogin();
            }
        });
    }
}