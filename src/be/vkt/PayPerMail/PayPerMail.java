package be.vkt.PayPerMail;

import javafx.application.Application;
import javafx.application.HostServices;
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
     * A reference to this Application
     */
    private static PayPerMail appInstance;


    /**
     * Constructor
     */
    public PayPerMail() {
        mailPayment = new SimpleObjectProperty<>(this, "mailPayment", new MailPayment());
        appInstance = this;
    }

    /**
     * Returns the data as a POJO
     * @return the data as a POJO
     */
    public MailPayment getMailPayment() {
        return mailPayment.get();
    }

    /**
     * Returns the data as a JavaFX property
     * @return the data as a JavaFX property
     */
    public SimpleObjectProperty<MailPayment> mailPaymentProperty() {
        return mailPayment;
    }

    /**
     * Static method to get the isntance of the view
     * @return appInstance (this)
     */
    public static PayPerMail getAppInstance () {
        return appInstance;
    }

    /**
     * @return the HostServices (Application.getHostServices())
     */
    public HostServices getServices() {
        return getHostServices();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("In PayPerMail.start(), before Parent root = FXMLLoader.load");
        Parent root = FXMLLoader.load(getClass().getResource("PayPerMail.fxml"));
        primaryStage.setTitle("Betalen via E-mail");
        primaryStage.setScene(new Scene(root));
        System.out.println("In PayPerMail.start(), before primaryStage.show()");
        primaryStage.show();
        System.out.println("In PayPerMail.start(), after primaryStage.show()");

        // Give the controller access to the main app.
        ///PpmController controller = root
    }


    public static void main(String[] args) {
        System.out.println("In PayPerMail.main(), before launch()");
        launch(args);
        System.out.println("In PayPerMail.main(), after launch()");
    }
}
