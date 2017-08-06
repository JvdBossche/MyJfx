package be.vkt.PayPerMail;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PayPerMail extends Application {

    /**
     * The data (as a JavaFX property)
     */
    //private MailPayment mailPayment = new MailPayment();  //what I Started out with
    //private SimpleObjectProperty<MailPayment> mailPayment = new SimpleObjectProperty<>(this, "mailPayment", new MailPayment());  //after "convert to JavaFX property"
    private SimpleObjectProperty<MailPayment> mailPayment;  //after "move initializer to constructor"

    /**
     * Constructor
     */
    public PayPerMail() {
        mailPayment = new SimpleObjectProperty<>(this, "mailPayment", new MailPayment());
    }

    /**
     * Returns the data as a POJO
     * @return
     */
    public MailPayment getMailPayment() {
        return mailPayment.get();
    }

    /**
     * Returns the data as a JavaFX property
     * @return
     */
    public SimpleObjectProperty<MailPayment> mailPaymentProperty() {
        return mailPayment;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PayPerMail.fxml"));
        primaryStage.setTitle("Betalen via E-mail");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
