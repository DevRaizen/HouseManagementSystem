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

public class forgotpasswordcontroll {
    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtkey;
    @FXML
    private Pane contentArea;
    


    @FXML
    void btngotoSignin (ActionEvent event) throws IOException{
        Pane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));  
        contentArea.getChildren().setAll(pane);  
    }
    @FXML
    void btnconfirm(ActionEvent event) throws Exception{
        String Duser = "root";
        String Dpass = "Raizen092103";
        String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

        String email = txtemail.getText();
        String recKey = txtkey.getText();


        try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
            String selectQuery = "SELECT 'Client' AS UserType, clientID, Fname, Username, Email, Password, Req_key, null AS System_key FROM Client WHERE Email = ? AND Req_key = ? " +
        "UNION " +
        "SELECT 'Admin' AS UserType, AdminID, Fname, Username, Email, Password, null AS Req_key, System_key FROM Admin WHERE Email= ? AND System_key = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, recKey);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, recKey);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String  usertype = resultSet.getString("UserType");
                        String currentUserName = resultSet.getString("Username");
                        String  currentPassword = resultSet.getString("Password");
                        String  currentEmail = resultSet.getString("Email");
                        String  currentFname = resultSet.getString("Fname");
                        if(usertype.equals("Client")){
                            String cur_Key = resultSet.getString("Req_key");

                            System.out.println("Recovery key matched for email: " + email);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Confirm Password.fxml"));
                            Pane pane = loader.load();  
                            contentArea.getChildren().setAll(pane);
                            
                            User user = new User(currentUserName,currentFname,currentEmail,currentPassword,cur_Key,usertype);
        
                           confirmpasswordcontroll cpc = loader.getController();
                            cpc.setLoggedInUser(user); 
                        }else{
                            String cur_Key = resultSet.getString("System_key");

                            System.out.println("Recovery key matched for email: " + email);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Confirm Password.fxml"));
                            Pane pane = loader.load();  
                            contentArea.getChildren().setAll(pane);
                        
                            User user = new User(currentUserName,currentFname,currentEmail,currentPassword,cur_Key,usertype);
        
                            confirmpasswordcontroll cpc = loader.getController();
                            cpc.setLoggedInUser(user); 
                        }


                    } else {
                        // No match found, handle accordingly
                        // For example, display an error message or clear the input fields
                        System.out.println("Recovery key does not match for email: " + email);
                    }
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

}
