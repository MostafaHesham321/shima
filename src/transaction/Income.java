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

public class Income extends Transaction {

    private IncomeSource source;

    public Income(int id, double amount, Date date,
                  String description, IncomeSource source) {
        super(id, amount, date, description);
        this.source = source;
    }

    public IncomeSource getSource() {
        return source;
    }

    public void setSource(IncomeSource source) {
        this.source = source;
    }
}
