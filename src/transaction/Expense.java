/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction;

/**
 *
 * @author MH
 */
import java.util.Date;

public class Expense extends Transaction {

    private PaymentMethod paymentMethod;
    private ExpenseCategory category;

    public Expense(int id, double amount,Date date,
                   String description,
                   PaymentMethod paymentMethod,
                   ExpenseCategory category) {
        super(id, amount, date, description);
        this.paymentMethod = paymentMethod;
        this.category = category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
}