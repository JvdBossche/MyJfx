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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
        fldAmount.setText("initial amount value");
        fldCn.setText("initial name value");
        fldEmail.setText("initial email value");

        mainApp = PayPerMail.getAppInstance();
    }


    //my actions :)

    public void clearForm(ActionEvent actionEvent) {
        System.out.println("Called clearForm()");
        initialize();
    }

    public void surfToPayPage(ActionEvent actionEvent) {
        System.out.println("Called surfToPayPage()");
        HostServices services = mainApp.getServices();
        services.showDocument("http://www.vkt.be"); //ToDo make this dynamic, duh
    }

    public void generateQrCode(ActionEvent actionEvent) {
        System.out.println("Called generateQrCode()");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String inputUrl = "https://secure.paypage.be/ncol/test/orderstandard.asp?AMOUNT=1020&CN=Hanne%20Mari%C3%ABn&CURRENCY=EUR&LANGUAGE=nl_BE&ORDERID=Mobicar_1502092092340&OWNERCTY=B&PSPID=kampeertest&TITLE=Betaling%20aan%20Vlaamse%20Kampeertoeristen%20VZW%20op%20Mobicar&SHASIGN=7EA022A20B9A2182373613C0480C2D46E58B4B22";
        int width = 400;
        int height = 400;

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(inputUrl, BarcodeFormat.QR_CODE, width, height);

            //Create a Canvas, with a 2D Graphic
            Canvas canvas = new Canvas(width, height);
            GraphicsContext gc2D = canvas.getGraphicsContext2D();

            //in white, paint a rectangle on it, with the full size
            gc2D.setFill(javafx.scene.paint.Color.WHITE);
            gc2D.fillRect(0, 0, width, height);

            //start painting in black: each bit/pixel set in the bitMatix
            gc2D.setFill(javafx.scene.paint.Color.BLACK);
            for (int v = 0; v < height; v++) {
                for (int h = 0; h < width; h++) {
                    if (bitMatrix.get(v, h)) {
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
    }
}
