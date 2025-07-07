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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class confirmpasswordcontroll {
    @FXML
    private PasswordField txtpsw;
    @FXML
    private PasswordField txtconpsw;
    @FXML
    private Pane contentArea;
    private User user;

    public void setLoggedInUser(User user) {
        this.user = user;
    }


    @FXML
    void btnconpass (ActionEvent event) throws Exception{
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        String psw = txtpsw.getText();
        String conpsw = txtconpsw.getText();
        String email = user.getEmail();
        String recKey = user.getRec_key();
        String userType = user.getUsertype();

        if(psw.equals(conpsw) && psw.length() > 7){
            if(userType.equals("Client")){
                try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
                    String updateQuery = "UPDATE Client SET Password = ? WHERE Email = ? AND Req_key = ?";
                
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        
                        preparedStatement.setString(1, psw);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, recKey);
                        
                        int rowsAffected = preparedStatement.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            // Password updated successfully
                            System.out.println("Password updated successfully for email: " + email);
                            JOptionPane.showMessageDialog(null, "Success");
                            Pane pane =  FXMLLoader.load(getClass().getResource("Login.fxml"));
                            contentArea.getChildren().setAll(pane);
                        } else {
                            // No rows were affected, meaning no matching email and recovery key were found
                            System.out.println("No matching email and recovery key found: " + email);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            }else{
                try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
                    String updateQuery = "UPDATE Admin SET Password = ? WHERE Email = ? AND System_key = ?";
                
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        
                        preparedStatement.setString(1, psw);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, recKey);
                        
                        int rowsAffected = preparedStatement.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            // Password updated successfully
                            System.out.println("Password updated successfully for email: " + email);
                            JOptionPane.showMessageDialog(null, "Success");
                            Pane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));  
                            contentArea.getChildren().setAll(pane);  
                        } else {
                            // No rows were affected, meaning no matching email and recovery key were found
                            System.out.println("No matching email and recovery key found: " + email);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            }
        }else{
            JOptionPane.showMessageDialog(null, "Password Does not match");
        }

        
    }
        

}
