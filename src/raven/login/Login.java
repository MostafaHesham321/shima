package raven.login;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.menu.FormManager;
import raven.model.ModelUser;
import services.AuthService;
import core.Session;
import model.User;
import databaseAccess.UserDAO;
        
/**
 *
 * @author Raven
 */

public class Login extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;
    private JLabel lblRegister;
    private JLabel lblMessage;
    
    public Login() {
        init();
    }
    
    private void clearMessage() {
        lblMessage.setText("");
    }
    private void init() {

        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));

        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;"
                        + "[light]background:darken(@background,3%);"
                        + "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE,
                "showRevealButton:true");

        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background,10%);"
                        + "[dark]background:lighten(@background,10%);"
                        + "borderWidth:0;focusWidth:0;innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Enter your username or email");

        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");

        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        description.putClientProperty(FlatClientProperties.STYLE,
                "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%)");
        
        //invalid Message
        lblMessage = new JLabel("");
        lblMessage.setForeground(java.awt.Color.RED);
        lblMessage.setFont(lblMessage.getFont().deriveFont(11f));
        lblMessage.setForeground(new java.awt.Color(220, 50, 50));   
        
        // 🔥 Register link
        lblRegister = new JLabel("<html><a href=''>Create new account</a></html>");
        lblRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegister.putClientProperty(FlatClientProperties.STYLE,
                "foreground:#4A90E2;font:12");

        // ================= ADD UI =================
        panel.add(lbTitle);
        panel.add(description);

        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);

        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(lblMessage, "gapy 5");
        
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(lblRegister, "gapy 5");

        add(panel);

        // ================= EVENTS =================

        cmdLogin.addActionListener(e -> {

            lblMessage.setText(""); // reset message

            String email = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            String result = AuthService.login(email, password);

            // ================= SUCCESS =================
            if (result.equals("SUCCESS")) {
                
                User user = UserDAO.login(email,password);
                
                if(user != null){
                    Session.currentUser = user;
                    
                    FormManager.login(new ModelUser(email, true)); //delete ModelUser
                    
                }else{
                    lblMessage.setText("Invalid email or password");
                }

            }else{
                 lblMessage.setText(result);
            }
            
        });
        
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                FormManager.gotoRegister();
            }
        });

        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                clearMessage();
            }
        });

        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                clearMessage();
            }
        });
    }
}
