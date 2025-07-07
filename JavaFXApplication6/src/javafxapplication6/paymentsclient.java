package javafxapplication6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.Alert.AlertType;
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

public class paymentsclient implements Initializable {

    @FXML
    private Pane contentArea;

    @FXML
    private Pane paypane;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txthouseid;

    @FXML
    private TextField txtprice;

    @FXML
    private Label lblrate;
    private User loggedInUser;
    private House logHouse;
    private Tenant tenant;
    
    public paymentsclient() {
    }

    public paymentsclient(User user, House house ,Tenant tenant) {
        this.loggedInUser = user;
        this.logHouse = house;
    
        this.tenant = tenant;
    }
    public paymentsclient(User user, House house) {
        this.loggedInUser = user;
        this.logHouse = house;
       
    }

    public void getLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void getHouse(House house) {
        this.logHouse = house;
    }
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

            if (tenant != null ) {
               
                        LocalDate curdate = LocalDate.now();
                        Date applyDate = tenant.getApplyDate();
                        Date lastpay = tenant.getLastPayment();
                        long daysSincepay =-1;

                        if (lastpay != null) {
                            daysSincepay = java.time.temporal.ChronoUnit.DAYS.between(lastpay.toLocalDate(), curdate);
                        } else {
                            
                            System.out.println("lastpay is null");
                            daysSincepay = -1;
                        }

                        long daysSinceApply = java.time.temporal.ChronoUnit.DAYS.between(applyDate.toLocalDate(), curdate);


                        if (daysSinceApply >= 0 || daysSincepay >= 30) {
                            txtname.setText(tenant.getName());
                            txthouseid.setText(String.valueOf(tenant.getHouseNumber()));
                            lblrate.setText(String.valueOf(tenant.getMonthlyRate()));
                            paypane.setVisible(true); 
                        } else {
                            paypane.setVisible(false);
                        }
                    }else {
            
                paypane.setVisible(false);
                }
    }

     @FXML
      private void gotoDashboard(ActionEvent event) throws Exception {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
           Pane pane = loader.load();  
           contentArea.getChildren().setAll(pane);
       
       
          ClientHouseControll DBC = loader.getController();
          DBC.getLoggedInUser(loggedInUser);
          DBC.getHouse(logHouse);
       }
    
       @FXML
    private void gotoAccount (ActionEvent event) throws Exception{
    accountcontroller acc = new accountcontroller(loggedInUser, logHouse);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Client).fxml"));
    loader.setController(acc);

    Pane pane = loader.load();  

    contentArea.getChildren().setAll(pane);

    System.out.println("loggedInUser is not null: " + (loggedInUser != null));
    System.out.println("logHouse is not null: " + (logHouse != null));
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
}@FXML
private void btnpay(ActionEvent event) throws Exception{
    final String Duser = "root";
    final String Dpass = "Raizen092103";
    final String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
    LocalDate currentDate = LocalDate.now();
    Date sqlDate = Date.valueOf(currentDate);
    Double price = Double.parseDouble(txtprice.getText());

    if(price < tenant.getMonthlyRate()) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Insufficient Payment");
        alert.setHeaderText(null);
        alert.setContentText("Payment amount is less than the monthly rate. Please input the correct amount.");
        alert.showAndWait();

    } else if (price > tenant.getMonthlyRate()) {
        // Show alert for excess payment
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Excess Payment");
        alert.setHeaderText(null);
        alert.setContentText("Your change: " + String.valueOf(price - tenant.getMonthlyRate()));

        alert.showAndWait();
        String query = "INSERT INTO payment (Name, houseid, Paydate, monthly_rate, stats) VALUES (?, ?, ?, ?, ?)";
        String query2 = "UPDATE tenant SET LastPay = ?, appdate = ? WHERE id = ? and house_num = ?";
        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
             PreparedStatement ps = conn.prepareStatement(query);
             PreparedStatement ps2 = conn.prepareStatement(query2)) {

            // Set parameters for the PreparedStatement
            ps.setString(1, tenant.getName());
            ps.setInt(2, tenant.getHouseNumber());
            ps.setDate(3, sqlDate);
            ps.setDouble(4, price);
            ps.setString(5, "Paid");

            // Execute the query
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new payment record was inserted successfully.");
                
                ps2.setDate(1, sqlDate);
                ps2.setDate(2, sqlDate);
                ps2.setInt(3, tenant.getId());
                ps2.setInt(4, tenant.getHouseNumber());
                // Execute the second query
                int rowsUpdated = ps2.executeUpdate();
                if (rowsUpdated > 0) {
                    Payment payment = new Payment(tenant.getName(),tenant.getHouseNumber(),tenant.getMonthlyRate(),sqlDate);
                    System.out.println("LastPay field in tenant table updated successfully.");
                    qrcontroller qr = new qrcontroller(loggedInUser, payment);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("QR.fxml"));
                    loader.setController(qr);

                    Pane pane = loader.load();  

                    contentArea.getChildren().setAll(pane);

                    System.out.println("loggedInUser is not null: " + (loggedInUser != null));
                    System.out.println("logHouse is not null: " + (logHouse != null));
                } else {
                    System.out.println("Failed to update LastPay field in tenant table.");
                }

            } else {
                System.out.println("Failed to insert payment record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    else {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("!");
        alert.setHeaderText(null);
        alert.setContentText("Payment Successful!");
        alert.showAndWait();

        String query = "INSERT INTO payment (Name, houseid, Paydate, monthly_rate, stats) VALUES (?, ?, ?, ?, ?)";
        String query2 = "UPDATE tenant SET LastPay = ? WHERE id = ? and house_num = ?";
        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
             PreparedStatement ps = conn.prepareStatement(query);
             PreparedStatement ps2 = conn.prepareStatement(query2)) {

            // Set parameters for the PreparedStatement
            ps.setString(1, tenant.getName());
            ps.setInt(2, tenant.getHouseNumber());
            ps.setDate(3, sqlDate);
            ps.setDouble(4, price);
            ps.setString(5, "Paid");

            // Execute the query
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new payment record was inserted successfully.");
                
                ps2.setDate(1, sqlDate);
                ps2.setInt(2, tenant.getId());
                ps2.setInt(3, tenant.getHouseNumber());
                // Execute the second query
                int rowsUpdated = ps2.executeUpdate();
                if (rowsUpdated > 0) {
                    Payment payment = new Payment(tenant.getName(),tenant.getHouseNumber(),tenant.getMonthlyRate(),sqlDate);
                    System.out.println("LastPay field in tenant table updated successfully.");
                    qrcontroller qr = new qrcontroller(loggedInUser, payment);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("QR.fxml"));
                    loader.setController(qr);

                    Pane pane = loader.load();  

                    contentArea.getChildren().setAll(pane);

                    System.out.println("loggedInUser is not null: " + (loggedInUser != null));
                    System.out.println("logHouse is not null: " + (logHouse != null));
                } else {
                    System.out.println("Failed to update LastPay field in tenant table.");
                }

            } else {
                System.out.println("Failed to insert payment record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    
    @FXML
    private void gotoManageHouse(ActionEvent event) throws Exception{
        ManageHouseCLient acc = new ManageHouseCLient(loggedInUser, logHouse);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Manage House(CLient).fxml"));
        loader.setController(acc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        System.out.println("logHouse is not null: " + (logHouse != null));
        
    }
    @FXML
    private void gotoPayments(ActionEvent event) throws Exception {
       
        ManageHouseCLient pmc = new ManageHouseCLient(loggedInUser, logHouse);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments(Client).fxml"));
     
        loader.setController(pmc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        System.out.println("logHouse is not null: " + (logHouse != null));
    }
}
