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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardControll {
    @FXML
    private Pane contentArea;
    private User loggedInUser;
    @FXML
    private Hyperlink HVL;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
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
