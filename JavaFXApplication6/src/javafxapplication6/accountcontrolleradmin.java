package javafxapplication6;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.w3c.dom.UserDataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafxapplication6.User;
import javafxapplication6.House;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class accountcontrolleradmin implements Initializable {
    @FXML
    private Pane contentArea;
    @FXML
    private Label lblname;
    @FXML 
    private Label lbladd;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtuname;
    @FXML
    private TextField txtcontact;
    @FXML
    private TextField txtemail;

    @FXML
    private Button btncancel;
    @FXML
    private Button btnsave;
    @FXML
    private Button btnaccount;

    private User loggedInUser;
    
    public void getLoggedInUser(User user){
        this.loggedInUser = user;
    }

    public accountcontrolleradmin(User user){
        this.loggedInUser = user;
    }

    @FXML
    private void btnedit(ActionEvent event) throws Exception{

        txtname.setEditable(true);
        txtuname.setEditable(true);
        txtcontact.setEditable(true);
        txtemail.setEditable(true);

        btncancel.setVisible(true);
        btnsave.setVisible(true);

        btnaccount.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(loggedInUser != null){
        txtuname.setText(loggedInUser.getUsername());
        txtemail.setText(loggedInUser.getEmail());
        txtname.setText(loggedInUser.getFname());
        txtcontact.setText(loggedInUser.getContact());
        
        lblname.setText(loggedInUser.getUsername());
        lbladd.setText(loggedInUser.getAdd_ress());

        txtname.setEditable(false);
        txtuname.setEditable(false);
        txtcontact.setEditable(false);
        txtemail.setEditable(false);
        String selectQuery = "SELECT AdminID FROM Admin WHERE Username = ? AND Password = ?";
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
    
        try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, loggedInUser.getUsername());
            preparedStatement.setString(2, loggedInUser.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int adminID = resultSet.getInt("AdminID");
                    System.out.println("AdminID: " + adminID);
                    loggedInUser.setID(adminID);
                } else {
                    System.out.println("Admin not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }else{
            System.out.println("loggin user is null");
        }
    }

    @FXML
    private void btnsaved(ActionEvent event) throws Exception {

        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        // Save the updated values to the database
        if (loggedInUser != null) {
            // Update user information in the database
            try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
                String query = "UPDATE Admin SET Fname = ?, Username = ?, contact = ?, Email = ? WHERE AdminID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, txtname.getText());
                preparedStatement.setString(2, txtuname.getText());
                preparedStatement.setString(3, txtcontact.getText());
                preparedStatement.setString(4, txtemail.getText());
                preparedStatement.setInt(5, loggedInUser.getID());

                preparedStatement.executeUpdate();

                loggedInUser.setFname(txtname.getText());
                loggedInUser.setUsername(txtuname.getText());
                loggedInUser.setContact(txtcontact.getText());
                loggedInUser.setEmail(txtemail.getText());
                lblname.setText(loggedInUser.getUsername());
                lbladd.setText(loggedInUser.getAdd_ress());
                
                System.out.println("success" + loggedInUser.getID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        txtname.setEditable(false);
        txtuname.setEditable(false);
        txtcontact.setEditable(false);
        txtemail.setEditable(false);

        btnaccount.setVisible(true);
        btncancel.setVisible(false);
        btnsave.setVisible(false);

    }

    @FXML
    private void btncanceled(ActionEvent event) throws Exception {
        
        if (loggedInUser != null) {
            txtname.setText(loggedInUser.getFname());
            txtuname.setText(loggedInUser.getUsername());
            txtcontact.setText("");
            txtemail.setText(loggedInUser.getEmail());

            
        }

        // Disable text fields
        txtname.setEditable(false);
        txtuname.setEditable(false);
        txtcontact.setEditable(false);
        txtemail.setEditable(false);

        btnaccount.setVisible(true);
        btncancel.setVisible(false);
        btnsave.setVisible(false);
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
        void gotoDashboard(ActionEvent event) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Pane pane = loader.load();  
            contentArea.getChildren().setAll(pane);
        
        
            DashboardControll DBC = loader.getController();
           DBC.setLoggedInUser(loggedInUser);
        }
}
