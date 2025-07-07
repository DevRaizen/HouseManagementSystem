package javafxapplication6;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javax.swing.JOptionPane;
import javafx.scene.Node;

public class RegController implements Initializable{
     @FXML
    private ComboBox <String> uaccess;
    @FXML
    private TextField txtpname,txtuname,txtemail;
    @FXML
    private PasswordField txtpsw,txtconpsw;
    @FXML
    private CheckBox terms;
    @FXML
    private Pane contentArea;
    @FXML
    private Hyperlink signin;
    @FXML
    private Hyperlink T_con;
    @FXML
    private Hyperlink privacy;

    @FXML
    void Select(ActionEvent event) {
        String s = uaccess.getSelectionModel().getSelectedItem().toString();
    }   

    public void initialize(URL url, ResourceBundle rb) {
        if (uaccess == null) {
            uaccess = new ComboBox<>();
        }

        uaccess.setItems(FXCollections.observableArrayList("Client","Admin"));
    }
    @FXML
    void btnTexit (ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.close();
    }
    @FXML
    void btnreg (ActionEvent event) throws IOException{
        String use_acc,uname,pname,email,pass,conpass;
        use_acc = uaccess.getValue();
        pname = txtpname.getText();
        uname = txtuname.getText();
        email = txtemail.getText();
        pass = txtpsw.getText();
        conpass = txtconpsw.getText();

        if(terms.isSelected() && pass.equals(conpass) && pass.length() >= 8 ){
            if(pname.equals("") || uname.equals("") || email.equals("")||use_acc == null){
                JOptionPane.showMessageDialog(null, "Please Fill the requirements");
            }else{
                person pr = new person(use_acc, pname, uname, email, conpass,false);
                pr.savedata();
                txtconpsw.setText("");
                txtpname.setText("");
                txtpsw.setText("");
                txtuname.setText("");
                txtemail.setText("");
                
                Pane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
                contentArea.getChildren().setAll(pane);
            }
        }else if(!terms.isSelected() && pass.equals(conpass)){
            JOptionPane.showMessageDialog(null, "Please Check Terms and Condition Below!");
        }else if(terms.isSelected() && !pass.equals(conpass)){
            JOptionPane.showMessageDialog(null, "Invalid Password!");
        }else if(terms.isSelected() && pass.equals(conpass) && pass.length() < 8){
            JOptionPane.showMessageDialog(null, "Password must contain 8 characters");
        }else if(uaccess == null){
            JOptionPane.showMessageDialog(null, "Please Select Access Control");
        }

        System.out.println("qwer");
    }

    @FXML
    void btngotoSignin (ActionEvent event) throws IOException{
        Pane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));  
        contentArea.getChildren().setAll(pane);  
    }

    // hyperlink
    @FXML
    private void term (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Terms Of Service.fxml"));
        Pane ToS = loader.load();  
        Scene TosScene = new Scene(ToS);
        Stage TosStage = new Stage();
        TosStage.setScene(TosScene);
        TosStage.setTitle("Dashboard");
        TosStage.show();
   
    }
    @FXML
    private void T_priv (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Privacy Policy.fxml"));
        Pane PoP = loader.load();                                                                                                                                  
        Scene PoPScene = new Scene(PoP);
        Stage PoPStage = new Stage();
        PoPStage.setScene(PoPScene);
        PoPStage.setTitle("Dashboard");
        PoPStage.show();
    }
   


    
}