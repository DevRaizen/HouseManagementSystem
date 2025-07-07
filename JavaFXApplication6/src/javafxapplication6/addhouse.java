
package javafxapplication6;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.w3c.dom.UserDataHandler;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafxapplication6.User;
import javafxapplication6.House;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class addhouse {
    private User loggedInUser;
    private House logHouse;
    
    @FXML
    private Pane contentArea;
    @FXML
    private TextField txthousename;
    @FXML
    private TextField txthouseadd;
    @FXML
    private TextField txthousenum;
    @FXML
    private TextField txtprice;
    @FXML
    private TextField txtbath;
    @FXML
    private TextField txtbed;
    @FXML
    private TextField txtunit;
    @FXML
    private ImageView imgview;

    public void getLoggedInUser(User user){
        this.loggedInUser = user;
    }
    public void getHouse(House user){
        this.logHouse = user;
    }

    @FXML
    private void upimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Load the selected image into ImageView
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imgview.setImage(selectedImage);
        } else {
            // No file selected
            showAlert(Alert.AlertType.ERROR, "No File Selected", "Please select an image file.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void btnadd(ActionEvent event)throws Exception{
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        String houseName = txthousename.getText();
        String houseAdd = txthouseadd.getText();
        int houseNum = Integer.parseInt(txthousenum.getText());
        Double price = Double.parseDouble(txtprice.getText());
        int bath = Integer.parseInt(txtbath.getText());
        int bed = Integer.parseInt(txtbed.getText());
        String unit = txtunit.getText();
       
        // Retrieve uploaded image
        Image image = imgview.getImage();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(renderedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to convert image to byte array: " + e.getMessage());
            return;
        }
        byte[] imageData = byteArrayOutputStream.toByteArray();
        // Insert values into database table
        try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
            String sql = "INSERT INTO House (id, image_data, address, image_name, unit, bed, bath, price,Vis_house) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,houseNum);
                statement.setBytes(2, imageData);
                statement.setString(3, houseAdd);
                statement.setString(4, houseName);
                statement.setString(5, unit);
                statement.setInt(6, bed);
                statement.setInt(7, bath);
                statement.setDouble(8, price);
                statement.setBoolean(9, true);
                statement.executeUpdate();
    
                showAlert(Alert.AlertType.INFORMATION, "Success", "House added successfully.");
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientHouse.fxml"));
                Pane pane = loader.load();  
                contentArea.getChildren().setAll(pane);
                
            
                ClientHouseControll CHL = loader.getController();
                CHL.getLoggedInUser(loggedInUser);
              CHL.getHouse(logHouse);
              CHL.loadHousePaneVisibility();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add house to the database: " + e.getMessage());
        }
    }

    @FXML
    private void btnlogout(ActionEvent event) throws IOException {
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
    @FXML
    private void gotoHVL(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientHouse.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
    
        ClientHouseControll CHL = loader.getController();
        CHL.getLoggedInUser(loggedInUser);
    }
    @FXML
    private void gotoTenant(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tenants.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
        TenantController TCH = loader.getController();
        TCH.getLoggedInUser(loggedInUser);
    }
    @FXML
    private void gotoPayment(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        paymentadmin pma = loader.getController();
        pma.getLoggedInUser(loggedInUser);
    
    }
    
    @FXML
    private void gotoAccount(ActionEvent event) throws Exception{
    accountcontrolleradmin acc = new accountcontrolleradmin(loggedInUser);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Admin).fxml"));
    loader.setController(acc);

    Pane pane = loader.load();  

    contentArea.getChildren().setAll(pane);

    System.out.println("loggedInUser is not null: " + (loggedInUser != null));

    
    }

}
