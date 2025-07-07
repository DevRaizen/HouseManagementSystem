package javafxapplication6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class ClientManExtend {
    @FXML
    private Pane contentArea;

    @FXML
    private ImageView imgview;

    @FXML
    private Label lblimgname;

    @FXML
    private Label lbladd;

    @FXML
    private Text txtimgnamee;

    @FXML
    private Label lblbed;

    @FXML
    private Label lblbath;

    @FXML
    private Label lblunit;

    @FXML
    private Label lblprice;
    @FXML
    private TextField txthousemem;
    @FXML
    private Label lblname;
    @FXML 
    private Label lbldate;
    @FXML 
    private Label lblhousemem;
    private User loggedInUser;
    private House logHouse;
    private House house;
   
    public ClientManExtend() {
      
    }

    public void gethouse(House user){
        this.house = user;
        loadImageFromDatabase() ;
    }

    public void getLoggedInUser(User user){
        this.loggedInUser = user;
    }
    public void getHouse(House user){
        this.logHouse = user;
    }

    private void loadImageFromDatabase() {
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        int houseID = house.Gethouse_num();

        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass)) {
            // First query to retrieve house information
            String houseQuery = "SELECT * FROM House WHERE id = ?";
            PreparedStatement houseStmt = conn.prepareStatement(houseQuery);
            houseStmt.setInt(1, houseID);
            ResultSet houseRs = houseStmt.executeQuery();
            
            // Second query to retrieve tenant information
            String tenantQuery = "SELECT Name, appdate FROM tenant WHERE id = ?";
            PreparedStatement tenantStmt = conn.prepareStatement(tenantQuery);
            tenantStmt.setInt(1, loggedInUser.getID()); // Assuming you have a tenantID variable
            ResultSet tenantRs = tenantStmt.executeQuery();
            
            if (houseRs.next()) {
                byte[] imageData = houseRs.getBytes("image_data");
                Image image = new Image(new ByteArrayInputStream(imageData));
                imgview.setImage(image);
                imgview.setPreserveRatio(true);
        
                String imgname = houseRs.getString("image_name");
                String address = houseRs.getString("address");
                int bedrooms = houseRs.getInt("bed");
                int bathrooms = houseRs.getInt("bath");
                String unit = houseRs.getString("unit");
                double price = houseRs.getDouble("price");
                int housemem = houseRs.getInt("housePerson");
        
                // Update the UI with the retrieved house data
                lblimgname.setText(imgname);
                lbladd.setText(address);
                lblbed.setText(String.valueOf(bedrooms));
                lblbath.setText(String.valueOf(bathrooms));
                lblunit.setText(unit);
                lblprice.setText(String.valueOf(price) + "0");
                txtimgnamee.setText(imgname);
                lblhousemem.setText(String.valueOf(housemem));
            } else {
                // House not found in the database
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("House not found");
                alert.setContentText("The house with ID " + houseID + " does not exist.");
                alert.showAndWait();
            }
            
            // Process tenant data if needed
            if (tenantRs.next()) {
                String tenantName = tenantRs.getString("Name");
                Date appDate = tenantRs.getDate("appdate");
                
                lblname.setText(tenantName);
               // Format the Date object to a desired string representation
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You can use any desired date format
                String formattedDate = dateFormat.format(appDate);

// Set the formatted date string to the lbldate label
lbldate.setText(formattedDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    @FXML
    void gotoDashboard(ActionEvent event) throws Exception {
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

        loggedInUser.LogCredential();
    } else {
        
    }
}

@FXML
    private void btnend (ActionEvent event) throws Exception{
        final String Duser = "root";
        final String Dpass = "Raizen092103";
        final String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
 
     
        String DELETE_TENANT = "DELETE FROM tenant WHERE id = ? And house_num = ?";
        String UPDATE_HOUSE = "UPDATE house SET has_client = NULL, ClientID = NULL, housePerson = ? WHERE ClientID = ? AND id = ?";
        String UPDATE_CLIENT = "UPDATE client set ApplyDate = NULL, Add_ress = NULL, contact = NULL, sex = NULL, house_num = NULL Where ClientID = ?";
        try (
            Connection connection = DriverManager.getConnection(Durl, Duser, Dpass);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TENANT);
            PreparedStatement updateHouseStatement = connection.prepareStatement(UPDATE_HOUSE);
            PreparedStatement updateClientStatement = connection.prepareStatement(UPDATE_CLIENT);
        ) {
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, loggedInUser.getID());
            preparedStatement.setInt(2,house.Gethouse_num());
            // Execute the delete statement for tenant
            preparedStatement.executeUpdate();
            
            // Execute the update statement for house
            updateHouseStatement.setInt(1, 0);
            updateHouseStatement.setInt(2, loggedInUser.getID());
            updateHouseStatement.setInt(3, house.Gethouse_num());
            updateHouseStatement.executeUpdate();

            updateClientStatement.setInt(1,loggedInUser.getID());
            updateClientStatement.executeUpdate();
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Thank You for Using our System.");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
            Pane pane = loader.load();  
            contentArea.getChildren().setAll(pane);
            ClientHouseControll DBC = loader.getController();
            DBC.getLoggedInUser(loggedInUser);
            DBC.getHouse(logHouse);

        } catch (SQLException e) {
            e.printStackTrace();
        }
}
@FXML
private void btnsavehousemem(ActionEvent event) throws Exception {
    int housemem = Integer.parseInt(txthousemem.getText());
    final String Duser = "root";
    final String Dpass = "Raizen092103";
    final String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

    try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass)) {
        String houseQuery = "UPDATE house SET housePerson = ? WHERE id = ?";
        PreparedStatement houseStmt = conn.prepareStatement(houseQuery);
        houseStmt.setInt(1, housemem);
        houseStmt.setInt(2, house.Gethouse_num()); // Assuming loggedInUser is an instance of some class with a method getID() that returns the user's ID
        int rowsAffected = houseStmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("House membership updated successfully!");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
            Pane pane = loader.load();  
            contentArea.getChildren().setAll(pane);
            ClientHouseControll DBC = loader.getController();
            DBC.getLoggedInUser(loggedInUser);
            DBC.getHouse(logHouse);
        } else {
            System.out.println("No rows were updated. Check if the ID exists.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 
}



