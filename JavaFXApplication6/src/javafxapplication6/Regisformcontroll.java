package javafxapplication6;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

import javafx.collections.FXCollections;
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
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.scene.control.ComboBox;

public class Regisformcontroll implements Initializable {
   
    @FXML
    private ComboBox <String> txtcsex;
    @FXML
    private Pane contentArea;
    @FXML
    private TextField txtCfname;
    @FXML
    private TextField txtCadd;
    @FXML
    private TextField txtCcon;
    @FXML
    private TextField txtEFname;
    @FXML
    private TextField txtEAdd;
    @FXML
    private TextField txtEcon;
    @FXML
    private TextField txtCemail;
    @FXML
    private TextField txtErel;

    private User loggedInUser;
    private House loghouse;
    @FXML
    void Select(ActionEvent event) {
        String s = txtcsex.getSelectionModel().getSelectedItem().toString();
    }   

    public void initialize(URL url, ResourceBundle rb) {
        if (txtcsex == null) {
            txtcsex= new ComboBox<>();
        }

        txtcsex.setItems(FXCollections.observableArrayList("Male","Female"));
    }

    public void getUser(User user){
        this.loggedInUser = user;
        txtCemail.setText(loggedInUser.getEmail());
        txtCfname.setText(loggedInUser.getFname());
    }
    public void gethouse(House user){
        this.loghouse = user;
    }
    

    @FXML
    private void btnback(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Client Dashboard house1.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
    
        viewhousecontroll vhc = loader.getController();
        vhc.getUser(loggedInUser);
        vhc.getUser(loggedInUser);
    }
    @FXML
    private void btnsub(ActionEvent event) throws Exception{
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        String cfname = txtCfname.getText();
        String cadd = txtCadd.getText();
        String ccon = txtCcon.getText();
        String efname = txtEFname.getText();
        String eadd = txtEAdd.getText();
        String econ = txtEcon.getText();
        String cemail = txtCemail.getText();
        String erel = txtErel.getText();
        String sex = txtcsex.getValue();
        LocalDate curDate = LocalDate.now();
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection(Durl, Duser, Dpass);
    
            // Insert data into the Client table
            String updateClientSQL = "UPDATE Client SET Fname = ?, Add_ress = ?, contact = ?, Email = ?, sex = ?, ApplyDate = ?, house_num = ? WHERE clientId = ?";
            PreparedStatement updateClientStmt = conn.prepareStatement(updateClientSQL);
            updateClientStmt.setString(1, cfname);
            updateClientStmt.setString(2, cadd);
            updateClientStmt.setString(3, ccon);
            updateClientStmt.setString(4, cemail);
            updateClientStmt.setString(5, sex);
            updateClientStmt.setDate(6, java.sql.Date.valueOf(curDate));
            updateClientStmt.setInt(7, loghouse.Gethouse_num());
            updateClientStmt.setInt(8, loggedInUser.getID());
            updateClientStmt.executeUpdate();
            updateClientStmt.close();
    
            // Insert data into the EmergencyContact table
            String insertEmergencyContactSQL = "INSERT INTO emer_contact (ID, Fname, Add_ress, Contact,Relationship) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertEmergencyContactStmt = conn.prepareStatement(insertEmergencyContactSQL);
            insertEmergencyContactStmt.setInt(1, loggedInUser.getID()); 
            insertEmergencyContactStmt.setString(2, efname);
            insertEmergencyContactStmt.setString(3, eadd);
            insertEmergencyContactStmt.setString(4, econ);
            insertEmergencyContactStmt.setString(5, erel);
            insertEmergencyContactStmt.executeUpdate();
            insertEmergencyContactStmt.close(); 
    
            
            String updateHouseSQL = "UPDATE House SET has_client = ?, ClientID  = ?, housePerson = ? WHERE id = ?";
            PreparedStatement updateHouseStmt = conn.prepareStatement(updateHouseSQL);
            updateHouseStmt.setBoolean(1, true);
            updateHouseStmt.setInt(2, loggedInUser.getID());
            updateHouseStmt.setInt(3, 1);
            updateHouseStmt.setInt(4, loghouse.Gethouse_num());
            updateHouseStmt.executeUpdate();
            updateHouseStmt.close();

            String insertTenants = "Insert into Tenant (id,Name,house_num,monthly_rate,email,contact,add_ress,appdate) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement tentantstmt= conn.prepareStatement(insertTenants);

            tentantstmt.setInt(1, loggedInUser.getID());
            tentantstmt.setString(2,loggedInUser.getFname());
            tentantstmt.setInt(3, loghouse.Gethouse_num());
            tentantstmt.setDouble(4,loghouse.getprice());
            tentantstmt.setString(5, cemail);
            tentantstmt.setString(6, ccon);
            tentantstmt.setString(7, cadd);
            tentantstmt.setDate(8, java.sql.Date.valueOf(curDate));

            tentantstmt.executeUpdate();
            tentantstmt.close();

            conn.close();
            System.out.println("Data inserted successfully.");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();

            backtoDashboard();
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void backtoDashboard() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(Client).fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
        ClientHouseControll DBC = loader.getController();
        DBC.getLoggedInUser(loggedInUser);
        DBC.getHouse(loghouse);

    }
    @FXML
    private void gotoPayments(ActionEvent event) throws Exception {
       
        ManageHouseCLient pmc = new ManageHouseCLient(loggedInUser, loghouse);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments(Client).fxml"));
     
        loader.setController(pmc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        System.out.println("logHouse is not null: " + (loghouse != null));
    }
    
    @FXML
    private void gotoAccount (ActionEvent event) throws Exception{
        accountcontroller acc = new accountcontroller(loggedInUser, loghouse);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Client).fxml"));
        loader.setController(acc);
    
        Pane pane = loader.load();  
    
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        System.out.println("logHouse is not null: " + (loghouse != null));
    }
    @FXML
    private void gotoManageHouse(ActionEvent event) throws Exception{
        ManageHouseCLient acc = new ManageHouseCLient(loggedInUser, loghouse);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Manage House(CLient).fxml"));
        loader.setController(acc);
        
        Pane pane = loader.load();  
        
        contentArea.getChildren().setAll(pane);
    
        System.out.println("loggedInUser is not null: " + (loggedInUser != null));
        System.out.println("logHouse is not null: " + (loghouse != null));
        
    }
    @FXML
    void gotoDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(CLient).fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
        ClientHouseControll DBC = loader.getController();
        DBC.getLoggedInUser(loggedInUser);
        DBC.getHouse(loghouse);
    }

    }
