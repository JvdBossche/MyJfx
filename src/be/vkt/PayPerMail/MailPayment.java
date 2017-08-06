package be.vkt.PayPerMail;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *  This is the model class for E-Mail payments
 */
public class MailPayment {

    private final StringProperty amount;
    private final StringProperty name;
    private final StringProperty email;

    /**
     * Constructor with minimal initial data
     *
     * @param amount
     * @param email
     */
    public MailPayment(String amount, String email) {
        this.amount = new SimpleStringProperty(amount);
        this.email = new SimpleStringProperty(email);
        this.name =  new SimpleStringProperty("");
    }

    /**
     * Constructor with initial data
     *
     * @param amount
     * @param name
     * @param email
     */
    public MailPayment(String amount, String name, String email) {
        this.amount = new SimpleStringProperty(amount);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
    }

    /**
     * Default constructor
     */
    public MailPayment() {
        this("0.0", "Niemand@VlaamseKampeertoeristen.be");
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
