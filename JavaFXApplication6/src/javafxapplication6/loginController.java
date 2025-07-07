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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



public class loginController {
@FXML
 private Pane contentArea; 
@FXML
private TextField uname;
@FXML
private PasswordField psw;
@FXML
private Hyperlink createAccount;
@FXML
private Label lbluser;
@FXML
private Label lblpsw;
@FXML
void btnlog(ActionEvent event) throws IOException {

    String User = uname.getText();
    String pass = psw.getText();
    String Duser = "root";
    String Dpass = "Raizen092103";
    String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

    try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
        String selectQuery = "SELECT 'Client' AS UserType, clientID, Fname, Username, Email, Password, has_house, ApplyDate,Add_ress,contact " +
                     "FROM Client WHERE Username = ? AND Password = ? " +
                     "UNION " +
                     "SELECT 'Admin' AS UserType, AdminID, Fname, Username, Email, Password, NULL AS has_house, NULL AS ApplyDate, NULL AS Add_ress, contact " +
                     "FROM Admin WHERE Username = ? AND Password = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                        preparedStatement.setString(1, User);
                        preparedStatement.setString(2, pass);
                        preparedStatement.setString(3, User);
                        preparedStatement.setString(4, pass);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                String  usertype = resultSet.getString("UserType");
                               String currentUserName = resultSet.getString("Username");
                                String  currentPassword = resultSet.getString("Password");
                                String  currentEmail = resultSet.getString("Email");
                                String  currentFname = resultSet.getString("Fname");
                                Boolean currhas_house = resultSet.getBoolean("has_house");
                              
                                System.out.println("Current Email: " + currentUserName);
                                System.out.println("Current Password: " + currentPassword);
                                System.out.println("Current Usertype: " + usertype);
                                
                                if(usertype.equals("Admin")){
                                    
                                            String con = resultSet.getString("contact");
                                            Stage loginStage = (Stage) contentArea.getScene().getWindow();
                                            loginStage.close(); 

                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                                            Pane dashboardPane = loader.load();
                                            DashboardControll dashboardController = loader.getController();

                                            User user = new User(currentUserName,currentFname,currentEmail,currentPassword,currhas_house,con);
                                            dashboardController.setLoggedInUser(user);

                                            Scene dashboardScene = new Scene(dashboardPane);
                                            Stage dashboardStage = new Stage();
                                            dashboardStage.setScene(dashboardScene);
                                            dashboardStage.setTitle("Dashboard");
                                            dashboardStage.show();
                                                    
                                 } if(usertype.equals("Client")){
                                    int curID = resultSet.getInt("clientId");
                                    Date sqlDate = resultSet.getDate("ApplyDate");
                                    String Add_ress = resultSet.getString("Add_ress");
                                    String Contact = resultSet.getString("contact");
                                    System.out.println("Client Id: " + curID);
                                    Stage loginStage = (Stage) contentArea.getScene().getWindow();
                                    loginStage.close();
                                
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard(Client).fxml"));
                                    Pane dashboardPane = loader.load();
                                    ClientHouseControll chc = loader.getController();
                                
                                    User user = new User(currentUserName, currentFname, currentEmail, currentPassword, currhas_house, curID,Add_ress,Contact);
                                    chc.getLoggedInUser(user);
                                    chc.getHouse(null);
                                
                                    Scene dashboardScene = new Scene(dashboardPane);
                                    Stage dashboardStage = new Stage();
                                    dashboardStage.setScene(dashboardScene);
                                    dashboardStage.setTitle("Dashboard");
                                    dashboardStage.show();
                                    user.LogCredential();
                                    
                                 }
                                    } else {
                                                System.out.println(User);
                                                System.out.println(pass);
                                                System.out.println("Login failed. Incorrect email or password.");
                                                if (uname.getLength() == 0 && psw.getLength() > 0) {
                                                    lbluser.setText("Invalid Username");
                                                    lbluser.setVisible(true);
                                                    lblpsw.setVisible(false);
                                                }
                                                else if (psw.getLength() == 0 && uname.getLength() > 0) {
                                                    lblpsw.setText("Password does not match");
                                                    lblpsw.setVisible(true);
                                                    lbluser.setVisible(false);
                                                }
                                                else if(uname.getLength() > 0 && psw.getLength() > 0){
                                                    lblpsw.setText("Password does not match");
                                                    lblpsw.setVisible(true);
                                                    lbluser.setVisible(false);
                                                }else{
                                                    lblpsw.setText("Fill up the blanks");
                                                    lbluser.setText("Fill up the blanks");
                                                    lblpsw.setVisible(true);
                                                    lbluser.setVisible(true);
                                                }
                                             }
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        System.err.println("Failed to connect to the database!");
                                    }
}

@FXML
private void signUp (ActionEvent event) throws IOException {
     Pane pane = FXMLLoader.load(getClass().getResource("Register.fxml"));  
     contentArea.getChildren().setAll(pane);                                                                                                                                  
   
}

public void setStage(Stage stage) {
  
    throw new UnsupportedOperationException("Unimplemented method 'setStage'");
}
@FXML
void gotofpsw (ActionEvent event) throws Exception{
    Pane pane = FXMLLoader.load(getClass().getResource("Forgot Password.fxml"));  
    contentArea.getChildren().setAll(pane); 
}

}
