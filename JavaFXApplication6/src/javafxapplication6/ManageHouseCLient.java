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
import java.sql.Date;
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

public class ManageHouseCLient {
    
    @FXML
    private Pane contentArea;
    
    @FXML
    private Pane imgcon1;
    
    @FXML
    private ImageView img1;
    
    @FXML
    private Pane imgcon2;
    
    @FXML
    private ImageView img2;
    
    @FXML
    private Pane imgcon3;
    
    @FXML
    private ImageView img3;

    @FXML
    private Label lblunit1;
    
    @FXML
    private Label lblroom1;
    
    @FXML
    private Label lblroom2;
    
    @FXML
    private Label lblunit2;
    
    @FXML
    private Label lblroom3;
    
    @FXML
    private Label lblunit3;

    private User loggedInUser;
    private House logHouse;

    public ManageHouseCLient(User user, House house){
        this.loggedInUser = user;
        this.logHouse = house;
    }
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void setHouse(House house) {
        this.logHouse = house;
    }

    public void initialize() {
        // Load image and label data from database when the controller is initialized
        loadHouseData();
    }

    private void loadHouseData() {
        try {
            loadHouseImageAndLabels(1, imgcon1, img1, lblunit1, lblroom1);
            loadHouseImageAndLabels(2, imgcon2, img2, lblunit2, lblroom2);
            loadHouseImageAndLabels(3, imgcon3, img3, lblunit3, lblroom3);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }
    }

    private void loadHouseImageAndLabels(int houseId, Pane imgCon, ImageView imageView, Label lblUnit, Label lblRoom) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        try {
            // Establish database connection
            connection = DriverManager.getConnection(Durl, Duser, Dpass);

            // Prepare SQL query
            String query = "SELECT image_data, unit, bed FROM house WHERE ClientID = ? AND id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, loggedInUser.getID());
            statement.setInt(2, houseId);

            // Execute query
            resultSet = statement.executeQuery();

            // Check if result set has data
            if (resultSet.next()) {
                // Retrieve image data from the result set and set it to the ImageView
                byte[] imageData = resultSet.getBytes("image_data");
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);

                // Set label texts
                lblUnit.setText(resultSet.getString("unit"));
                lblRoom.setText(resultSet.getString("bed"));

                // Make the imgCon pane visible
                imgCon.setVisible(true);
            }
        } finally {
            // Close resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    @FXML
    private void gotoextend1(ActionEvent event) throws Exception{
        int house_num = 1;
        House house = new House(house_num);
     FXMLLoader loader = new FXMLLoader(getClass().getResource("Client Manage house.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
       

       ClientManExtend TCH = loader.getController();
        TCH.getLoggedInUser(loggedInUser);
        TCH.getHouse(logHouse);
        TCH.gethouse(house);
        System.out.print(house != null);
    }
    @FXML
    private void gotoextend2(ActionEvent event) throws Exception{
        int house_num = 2;
     FXMLLoader loader = new FXMLLoader(getClass().getResource("Client Manage house.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
        House house = new House(house_num);

       ClientManExtend TCH = loader.getController();
        TCH.getLoggedInUser(loggedInUser);
        TCH.getHouse(logHouse);
        TCH.gethouse(house);
    
    }
    @FXML
    private void gotoextend3(ActionEvent event) throws Exception{
        int house_num = 3;
     FXMLLoader loader = new FXMLLoader(getClass().getResource("Client Manage house.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
        House house = new House(house_num);

       ClientManExtend TCH = loader.getController();
        TCH.getLoggedInUser(loggedInUser);
        TCH.getHouse(logHouse);
        TCH.gethouse(house);
        System.out.print(house != null);
    
    }
    @FXML
    private void btnpay1(ActionEvent event) throws Exception{
        int house_num = 1;
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        String query = "SELECT * FROM tenant WHERE id = ? and house_num = ?"; // Modified query to select from the 'tenant' table

        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            int tenantId = loggedInUser.getID(); // Assuming getId() returns the ID of the tenant
            ps.setInt(1, tenantId);
            ps.setInt(2, house_num); // Setting the value for the placeholder
            ResultSet rs = ps.executeQuery(); // Executing the query
            
            if (rs.next()) {
                String name = rs.getString("Name");
                int houseNum = rs.getInt("house_num");
                double monthlyRate = rs.getDouble("monthly_rate");
                Date lastPay = rs.getDate("LastPay");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                String address = rs.getString("add_ress");
                Date appDate = rs.getDate("appdate");
                

                Tenant tenant = new Tenant(tenantId, name, houseNum, monthlyRate, lastPay, appDate);


                paymentsclient pmc = new paymentsclient(loggedInUser, logHouse,tenant);
        
                FXMLLoader loader = new FXMLLoader(getClass().getResource("payment method(client).fxml"));
             
                loader.setController(pmc);
            
                Pane pane = loader.load();  
            
                contentArea.getChildren().setAll(pane);
            
                System.out.println("loggedInUser is not null: " + (loggedInUser != null));
                System.out.println("logHouse is not null: " + (logHouse != null));
            
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @FXML
    private void btnpay2(ActionEvent event) throws Exception{
        int house_num = 2;
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        String query = "SELECT * FROM tenant WHERE id = ? and house_num = ?"; // Modified query to select from the 'tenant' table

        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            int tenantId = loggedInUser.getID(); // Assuming getId() returns the ID of the tenant
            ps.setInt(1, tenantId);
            ps.setInt(2, house_num); // Setting the value for the placeholder
            ResultSet rs = ps.executeQuery(); // Executing the query
            
            if (rs.next()) {
                String name = rs.getString("Name");
                int houseNum = rs.getInt("house_num");
                double monthlyRate = rs.getDouble("monthly_rate");
                Date lastPay = rs.getDate("LastPay");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                String address = rs.getString("add_ress");
                Date appDate = rs.getDate("appdate");

                Tenant tenant = new Tenant(tenantId, name, houseNum, monthlyRate, lastPay, appDate);

                House payhouse = new House(house_num);
                paymentsclient pmc = new paymentsclient(loggedInUser, logHouse,tenant);
        
                FXMLLoader loader = new FXMLLoader(getClass().getResource("payment method(client).fxml"));
             
                loader.setController(pmc);
            
                Pane pane = loader.load();  
            
                contentArea.getChildren().setAll(pane);
            
                System.out.println("loggedInUser is not null: " + (loggedInUser != null));
                System.out.println("logHouse is not null: " + (logHouse != null));
            
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void btnpay3(ActionEvent event) throws Exception{
        int house_num = 3;
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        String query = "SELECT * FROM tenant WHERE id = ? and house_num = ?"; // Modified query to select from the 'tenant' table

        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            int tenantId = loggedInUser.getID(); // Assuming getId() returns the ID of the tenant
            ps.setInt(1, tenantId);
            ps.setInt(2, house_num); // Setting the value for the placeholder
            ResultSet rs = ps.executeQuery(); // Executing the query
            
            if (rs.next()) {
                String name = rs.getString("Name");
                int houseNum = rs.getInt("house_num");
                double monthlyRate = rs.getDouble("monthly_rate");
                Date lastPay = rs.getDate("LastPay");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                String address = rs.getString("add_ress");
                Date appDate = rs.getDate("appdate");

                Tenant tenant = new Tenant(tenantId, name, houseNum, monthlyRate, lastPay, appDate);

                paymentsclient pmc = new paymentsclient(loggedInUser, logHouse,tenant);
        
                FXMLLoader loader = new FXMLLoader(getClass().getResource("payment method(client).fxml"));
             
                loader.setController(pmc);
            
                Pane pane = loader.load();  
            
                contentArea.getChildren().setAll(pane);
            
                System.out.println("loggedInUser is not null: " + (loggedInUser != null));
                System.out.println("logHouse is not null: " + (logHouse != null));
            
            
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