/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author MH
 */

import java.util.regex.Pattern;
import databaseAccess.UserDAO;

public class AuthService {
    
    private static UserDAO userDAO = new UserDAO();

    // ================= VALIDATION =================

    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(regex, email);
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*].*");
    }

    // ================= REGISTER =================

    public static String register(String name, String email, String password, String confirmPassword) {

        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }

        if (!isValidEmail(email)) {
            return "Invalid email format";
        }

        if (userDAO.emailExists(email)) {
            return "Email already exists";
        }

        if (password == null || password.isEmpty()) {
            return "Password is required";
        }

        if (!isValidPassword(password)) {
            return "Weak password (8 chars, capital, number, special char)";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        
        return "SUCCESS";
    }
    
    public static String login(String email, String password) {

        // ================= VALIDATION =================
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        else if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        else{
            return "SUCCESS";
        }
    }
}