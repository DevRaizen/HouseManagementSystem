package javafxapplication6;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.mysql.cj.xdevapi.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication6.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import java.io.ByteArrayInputStream;
import javafxapplication6.House;
import java.io.IOException;

public class viewhousecontroll {
    @FXML
    private Pane contentArea;
    @FXML
    private ImageView imgview;
    @FXML
    private Label lblbed;
    @FXML
    private Label lblbat;
    @FXML
    private Label lblunit;
    @FXML
    private Label lblprice;
    @FXML
    private Label lblimgname;
    @FXML
    private Label lbladdress;
    @FXML
    private Button btnrent;
    @FXML
    private Label lblname;
    @FXML
    private Label lbladdr;
    @FXML
    private Label lblcon;
    @FXML
    private Label lblemail;
    @FXML
    private Label lblappdate;

    @FXML
    private Label lblhousemem;
    
    private House loghouse;
    private User loggedInUser;

    public void getUser(User user){
        this.loggedInUser = user;
    }
    public void gethouse(House user){
        this.loghouse = user;
        loadImageFromDatabase();
    }

      int house_num;
       int clientID;
       private void loadImageFromDatabase() {
                String Duser = "root";
                String Dpass = "Raizen092103";
                String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
                house_num = loghouse.Gethouse_num();
                System.out.println(house_num);
           try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass)) {
               String query = "SELECT * FROM House WHERE id = ?";
               
               PreparedStatement stmt = conn.prepareStatement(query);
               stmt.setInt(1, house_num); 
               ResultSet rs = stmt.executeQuery();
               if (rs.next()) {
                  
                 clientID = rs.getInt("CLientID");
                   byte[] imageData = rs.getBytes("image_data");
                  
                   Image image = new Image(new ByteArrayInputStream(imageData));
                   
                   imgview.setImage(image);
                   imgview.setPreserveRatio(true);

                   if (house_num == 2) {
                    double prefWidth = 260.0;
                    double prefHeight = 250.0;

                    imgview.setFitWidth(prefWidth);
                    imgview.setFitHeight(prefHeight);
                    imgview.setLayoutX(22);
                    imgview.setLayoutY(16);
                   }if(house_num == 1){
                    double prefWidth = 265;
                    double prefHeight = 200;

                    imgview.setFitWidth(prefWidth);
                    imgview.setFitHeight(prefHeight);
                    imgview.setLayoutX(22);
                    imgview.setLayoutY(14);
                   } if (house_num == 3) {
                    double prefWidth = 260.0;
                    double prefHeight = 250.0;

                    imgview.setFitWidth(prefWidth);
                    imgview.setFitHeight(prefHeight);
                    imgview.setLayoutX(22);
                    imgview.setLayoutY(16);
                   }

                   String imgname = rs.getString("image_name");
                   String address = rs.getString("address");
                    int bedrooms = rs.getInt("bed");
                    int bathrooms = rs.getInt("bath");
                    String unit = rs.getString("unit");
                    double price = rs.getDouble("price");
                    int houseperson = rs.getInt("housePerson");
                    
                    // Now you can use this data to display in labels or any other UI components
                    lblbed.setText(String.valueOf(bedrooms));
                    lblbat.setText(String.valueOf(bathrooms));
                    lblunit.setText(unit);
                    lblprice.setText(String.valueOf(price)+"0");
                   lbladdress.setText(address);
                   lblimgname.setText(imgname);
                   if(lblhousemem != null){
                    lblhousemem.setText(String.valueOf(houseperson));
                   }
                   
                   if (btnrent != null) {
                    // Your code to set btnrent disable or enable based on conditions
                    boolean hasClient = rs.getBoolean("has_client");
                    if (hasClient) {
                        btnrent.setDisable(true); // or set text to "Not available"
    
                           
                    } else {
                        btnrent.setDisable(false); // or set text to "Rent"

                    }
                }else{
                    System.out.println("Your in Admin u dont have that button");
                    getTenantInfo(clientID);
                }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }


       }


       public void getTenantInfo(int tenantID) {
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        
        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass)) {
            String query = "SELECT * FROM tenant WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, tenantID); 
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String tenantName = rs.getString("Name");
                String address = rs.getString("add_ress");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                java.sql.Date appDate = rs.getDate("appdate");

    
                lblname.setText(tenantName);
                lbladdr.setText(address);
                lblemail.setText(email);
                lblcon.setText(contact);
                lblappdate.setText(String.valueOf(appDate));

                // You can also return the retrieved values or use them in any other way
            } else {
                // Handle the case where no matching tenant is found
                System.out.println("No tenant found with ID: " + tenantID);
                // You can display an error message, log the issue, or take any other appropriate action
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

       @FXML
     private  void gotoDashboard(ActionEvent event) throws Exception {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
           Pane pane = loader.load();  
           contentArea.getChildren().setAll(pane);
       
       
           DashboardControll DBC = loader.getController();
          DBC.setLoggedInUser(loggedInUser);
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

    // client side

    @FXML
    private void renthouse (ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration form.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
        
        
        Regisformcontroll rfc = loader.getController();
        rfc.getUser(loggedInUser);
        rfc.gethouse(loghouse);
    }

    @FXML
    private void ManageHouseClient(ActionEvent event) throws Exception{
        ManageHouseCLient acc = new ManageHouseCLient(loggedInUser, null);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Manage House(CLient).fxml"));
        loader.setController(acc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        
        
    }

    @FXML
    private void PaymentsClient(ActionEvent event) throws Exception {
        
        System.out.print("napindot");
        House house = new House(0);
        ManageHouseCLient pmc = new ManageHouseCLient(loggedInUser, house);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments(Client).fxml"));
     
        loader.setController(pmc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
       
    }

    @FXML
    private void DashboardClient(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
        ClientHouseControll DBC = loader.getController();
        DBC.getLoggedInUser(loggedInUser);
        DBC.getHouse(null);
    }
    

    @FXML
    private void AccountClient (ActionEvent event) throws Exception{
    accountcontroller acc = new accountcontroller(loggedInUser, null);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Client).fxml"));
    loader.setController(acc);

    Pane pane = loader.load();  

    contentArea.getChildren().setAll(pane);

    System.out.println("loggedInUser is not null: " + (loggedInUser != null));
    
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

