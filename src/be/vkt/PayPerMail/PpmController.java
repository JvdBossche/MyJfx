package be.vkt.PayPerMail;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PpmController {

    @FXML
    private TextField fldAmount;
    @FXML
    private TextField fldCn;
    @FXML
    private TextField fldEmail;
    @FXML
    private ImageView imgQr;

    //Reference to the main application
    private PayPerMail mainApp;

    //The Data (Model)
    private MailPayment mailPayment;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PpmController() {
        System.out.println("In PpmController Constructor()");
    }

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("In PpmController.initialize()");
        fldAmount.setText("");
        fldCn.setText("");
        fldEmail.setText("");
        imgQr.setImage(null);

        mailPayment = PayPerMail.getMailPayment();
        mailPayment.setAmount(fldAmount.getText());
        mailPayment.setEmail(fldEmail.getText());
        mailPayment.setName(fldCn.getText());

        mainApp = PayPerMail.getAppInstance();
    }


    //fieldChange actions

    private void calculateUrl() {
        System.out.println("in PpmController.calculateUrl()");

        //get the field values from the dialog
        mailPayment.setAmount(fldAmount.getText());
        mailPayment.setEmail(fldEmail.getText());
        mailPayment.setName(fldCn.getText());

        mailPayment.calculateUrl();
        fldAmount.setText(mailPayment.getAmount()); //the amount is cleaned by calculateUrl()
    }

    //my actions :)

    public void clearForm(ActionEvent actionEvent) {
        System.out.println("Called clearForm()");
        initialize();
    }

    public void surfToPayPage(ActionEvent actionEvent) {
        System.out.println("Called surfToPayPage()");
        calculateUrl();
        HostServices services = mainApp.getServices();
        services.showDocument(mailPayment.getUrl());
    }

    public void generateQrCode(ActionEvent actionEvent) {
        System.out.println("Called generateQrCode()");
        calculateUrl();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String inputUrl = mailPayment.getUrl();
        int width = 400;
        int height = 400;

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(inputUrl, BarcodeFormat.QR_CODE, width, height);

            //Create a Canvas, with a 2D Graphic
            Canvas canvas = new Canvas(width, height);
            GraphicsContext gc2D = canvas.getGraphicsContext2D();

            //in white, paint a rectangle on it, with the full size
            gc2D.setFill(Color.WHITE);
            gc2D.fillRect(0, 0, width, height);

            //start painting in black: each bit/pixel set in the bitMatix
            gc2D.setFill(Color.BLACK);
            for (int v = 0; v < height; v++) {      // v: Vertical   <=> Y-axis
                for (int h = 0; h < width; h++) {   // h: Horizontal <=> X-axis
                    if (bitMatrix.get(h, v)) {
                        gc2D.fillRect(h, v, 1, 1);
                    }
                }
            }

            //Take a snapshot of the canvas and set it as an image in the ImageView control
            imgQr.setImage(canvas.snapshot(null, null));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void closeApp(ActionEvent actionEvent) {
        System.out.println("closeApp()");
        Platform.exit();
        System.exit(0);
    }

    public void showAbout(ActionEvent actionEvent) {
        System.out.println("Called showAbout()");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Over PayPerMail");
        alert.setHeaderText("Vlaamse Kampeertoeristen V.Z.W.: PayPerMail");
        alert.setContentText("Versie 0.1: de gegenereerde URL is nog niet correct");
        alert.showAndWait();
    }
}
