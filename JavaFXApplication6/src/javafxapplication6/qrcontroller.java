package javafxapplication6;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel; // Import this if you want to set error correction level
import com.google.zxing.client.j2se.MatrixToImageWriter; // Import this for MatrixToImageWriter

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class qrcontroller {

    @FXML 
    private Pane contentArea;

    @FXML
    private ImageView imgqrview;

    private User loggedInUser;
    private Payment payment_details;

    public qrcontroller (User user, Payment payment){
        this.loggedInUser = user;
        this.payment_details = payment;
    }

    public void getPayment(Payment payment){
        this.payment_details = payment;
    }
    public void getLoggedInUser(User user){
        this.loggedInUser = user;
    }
    public void initialize() {
        generateQRCode();
    }

    // Method to generate the QR code
    private void generateQRCode() {
        // Generate the QR code content
        String qrCodeText = "SLDR APARTMENT\n\n" + "Tenant Name: " +payment_details.getName() + "\nTenant House Number: " + String.valueOf(payment_details.getHouseid()) + "\nTenant Monthly Rate: " + String.valueOf(payment_details.getMonthly_rate()) + "0\nTransaction Date: " + String.valueOf(payment_details.getDate()) + "\n\nThank you for Payment!";

        // Set QR code width and height
        int width = 300;
        int height = 300;

        // Create QR code writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Encode the QR code
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);

            // Convert bitMatrix to bufferedImage
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Convert byte array to JavaFX Image
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image qrCodeImage = new Image(inputStream);

            // Set the generated QR code image to the ImageView
            imgqrview.setImage(qrCodeImage);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void gotoPayments(ActionEvent event) throws Exception {
       
        ManageHouseCLient pmc = new ManageHouseCLient(loggedInUser, null);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments(Client).fxml"));
     
        loader.setController(pmc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
       
    }
    
    @FXML
    private void gotoAccount (ActionEvent event) throws Exception{
        accountcontroller acc = new accountcontroller(loggedInUser, null);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Client).fxml"));
        loader.setController(acc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        
    }
    @FXML
    private void gotoManageHouse(ActionEvent event) throws Exception{
        ManageHouseCLient acc = new ManageHouseCLient(loggedInUser, null);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Manage House(CLient).fxml"));
        loader.setController(acc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
       
        
    }
    @FXML
    void gotoDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
        ClientHouseControll DBC = loader.getController();
        DBC.getLoggedInUser(loggedInUser);
        
    }
    @FXML
    void btnlogout(ActionEvent event) throws IOException {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to exit, " + loggedInUser.getFname() + "?");
    
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, proceed with logout and exit
            Stage dashboard = (Stage) contentArea.getScene().getWindow();
            dashboard.close();
    
            // Load the Login.fxml
            Pane dashboardPane = FXMLLoader.load(getClass().getResource("Login.fxml"));
            double prefWidth = dashboardPane.prefWidth(-1);
            double prefHeight = dashboardPane.prefHeight(prefWidth);
    
            Scene dashboardScene = new Scene(dashboardPane, prefWidth, prefHeight);
    
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(dashboardScene);
            dashboardStage.setTitle("Dashboard");
            dashboardStage.show();
    
            // Call the method to log out the user
            loggedInUser.LogCredential();
        } else {
            // User clicked Cancel, do nothing
        }
    }
}
