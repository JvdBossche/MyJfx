package be.vkt.PayPerMail;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class PpmController {
    public void clearForm(ActionEvent actionEvent) {
        System.out.println("Called clearForm()");
    }

    public void surfToPayPage(ActionEvent actionEvent) {
        System.out.println("Called surfToPayPage()");
    }

    public void generateQrCode(ActionEvent actionEvent) {
        System.out.println("Called generateQrCode()");
    }

    public void closeApp(ActionEvent actionEvent) {
        System.out.println("closeApp()");
        Platform.exit();
        System.exit(0);
    }

    public void showAbout(ActionEvent actionEvent) {
        System.out.println("Called showAbout()");
    }
}
