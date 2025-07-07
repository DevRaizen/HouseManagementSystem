package javafxapplication6;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

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
public class ClientHouseControll implements Initializable {

    
    @FXML
    private Pane contentArea;
    @FXML
    private Pane imgcon1;
    @FXML
    private Pane imgcon2;
    @FXML
    private Pane imgcon3;
    @FXML
    private Pane imgcon4;
    @FXML
    private Pane imgcon5;
    @FXML
    private Pane imgcon6;

    private User loggedInUser;
    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;
    @FXML
    private ImageView img4;

    @FXML
    private ImageView img5;
    @FXML
    private Text mem1;
    @FXML
    private Text mem2;
    @FXML
    private Text mem3;
    @FXML
    private Text mem4;
    @FXML
    private Text mem5;
    @FXML
    private Text mem6;
    @FXML
    private ImageView img6;
    private House logHouse;


    public void getLoggedInUser(User user){
        this.loggedInUser = user;
    }
    public void getHouse(House user){
        this.logHouse = user;
    }

    
    private Image getImageFromDatabase(int houseNumber) {
        Image image = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
                
        try {
            conn = DriverManager.getConnection(Durl, Duser, Dpass);
            String query = "SELECT image_data FROM House WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, houseNumber);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                byte[] imageData = rs.getBytes("image_data");
                if (imageData != null && imageData.length > 0) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                    image = new Image(bis);
                } else {
                    // Handle the case where image data is empty
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return image;
    }
    
    // Method to set images to ImageView components
    private void setImagesToImageViews() {
        img1.setImage(getImageFromDatabase(1));
        img2.setImage(getImageFromDatabase(2));
        img3.setImage(getImageFromDatabase(3));
        img4.setImage(getImageFromDatabase(4));
        img5.setImage(getImageFromDatabase(5));
        img6.setImage(getImageFromDatabase(6));
    }
    
    // Call setImagesToImageViews() method in your initialize method or wherever appropriate
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImagesToImageViews();
        loadHousePaneVisibility();
    }

    @FXML
    void gotoDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
    
        DashboardControll DBC = loader.getController();
       DBC.setLoggedInUser(loggedInUser);
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
    void btnviewh1 (ActionEvent event)throws Exception{
        int house_num = 1;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        viewhousecontroll vhc = loader.getController();
         House house = new House(house_num);
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
        System.out.println(house.Gethouse_num());


    }
    @FXML
    void btnviewh2 (ActionEvent event)throws Exception{
        int house_num = 2;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        House house = new House(house_num);
        viewhousecontroll vhc = loader.getController();
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
    }
    @FXML
    void btnviewh3 (ActionEvent event)throws Exception{
        int house_num = 3;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        House house = new House(house_num);
        viewhousecontroll vhc = loader.getController();
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
    }
    @FXML
    void btnviewh4 (ActionEvent event)throws Exception{
        int house_num = 3;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        House house = new House(house_num);
        viewhousecontroll vhc = loader.getController();
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
    }
    @FXML
    void btnviewh5 (ActionEvent event)throws Exception{
        int house_num = 3;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        House house = new House(house_num);
        viewhousecontroll vhc = loader.getController();
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
    }
    @FXML
    void btnviewh6 (ActionEvent event)throws Exception{
        int house_num = 3;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View House 1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);

        House house = new House(house_num);
        viewhousecontroll vhc = loader.getController();
        vhc.gethouse(house);
        vhc.getUser(loggedInUser);
    }
    @FXML
    void addhouse (ActionEvent event)throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Add House.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
        addhouse vhc = loader.getController();
        vhc.getHouse(logHouse);
        vhc.getLoggedInUser(loggedInUser);
 
      
    }

     
    // Method to load visibility states of house panes from the database
    public void loadHousePaneVisibility() {
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
        String query = "SELECT id, Vis_house, has_client, housePerson FROM house";

        try (Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int houseNumber = rs.getInt("id");
                boolean isVisible = rs.getBoolean("vis_house");
                int houseperson = rs.getInt("housePerson");
               
                // Set visibility of the corresponding house pane
                switch (houseNumber) {
                    case 1:
                        imgcon1.setVisible(isVisible);
                        
                        if(mem1 != null){
                            mem1.setText(String.valueOf(houseperson));
                        }
                        break;
                    case 2:
                        imgcon2.setVisible(isVisible);
                        if(mem2 != null){
                            mem2.setText(String.valueOf(houseperson));
                        }
                        break;
                    case 3:
                        imgcon3.setVisible(isVisible);
                        if(mem3 != null){
                            mem3.setText(String.valueOf(houseperson));
                        }
                        break;
                    case 4:
                        imgcon4.setVisible(isVisible);
                        if(mem4 != null){
                            mem4.setText(String.valueOf(houseperson));
                        }
                        break;
                    case 5:
                        imgcon5.setVisible(isVisible);
                        if(mem5 != null){
                            mem5.setText(String.valueOf(houseperson));
                        }
                        break;
                    case 6:
                        imgcon6.setVisible(isVisible);
                        if(mem6 != null){
                            mem6.setText(String.valueOf(houseperson));
                        }
                        break;
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // client part
    @FXML
    private void handleImageClick(MouseEvent event) throws IOException {


        if (event.getSource() instanceof ImageView) {
            ImageView clickedImage = (ImageView) event.getSource();
            String imageId = clickedImage.getId();
            int houseid = 0;
            System.out.println("Clicked image ID: " + imageId);
            if (imageId.equals("img1")) {
                houseid = 1;
            } else if (imageId.equals("img2")) {
                houseid = 2;
            } else if (imageId.equals("img3")) {
                houseid = 3;
            } else if (imageId.equals("img4")) {
                houseid = 4;
            } else if (imageId.equals("img5")) {
                houseid = 5;
            } else if (imageId.equals("img6")) {
                houseid = 6;
            }
            
            House house = fetchHouseDetailsFromDatabase(houseid);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client Dashboard house1.fxml"));
            Pane pane = loader.load();  
            contentArea.getChildren().setAll(pane);
            viewhousecontroll vhc = loader.getController();
            vhc.gethouse(house);
            vhc.getUser(loggedInUser);
        }
        
    
    }

    private House fetchHouseDetailsFromDatabase(int houseid) {
        House house = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
    
        try {
            conn = DriverManager.getConnection(Durl, Duser, Dpass);
            String query = "SELECT * FROM House WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, houseid);
            rs = ps.executeQuery();
    
            if (rs.next()) {
                boolean hasClient = rs.getBoolean("has_client");
                int ClientID = rs.getInt("ClientID");
                double price = rs.getDouble("price");
                house = new House(houseid, hasClient,ClientID,price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return house;
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
private void gotoPayment(ActionEvent event) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments.fxml"));
    Pane pane = loader.load();  
    contentArea.getChildren().setAll(pane);

    paymentadmin pma = loader.getController();
    pma.getLoggedInUser(loggedInUser);

}
@FXML
private void gotoAccounts(ActionEvent event) throws Exception{
accountcontrolleradmin acc = new accountcontrolleradmin(loggedInUser);

FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Admin).fxml"));
loader.setController(acc);

Pane pane = loader.load();  

contentArea.getChildren().setAll(pane);

System.out.println("loggedInUser is not null: " + (loggedInUser != null));


}
}
