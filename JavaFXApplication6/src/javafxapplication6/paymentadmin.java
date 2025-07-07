package javafxapplication6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.mysql.cj.xdevapi.Client;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date; // Add this import for Date
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox; // Add this import for HBox
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.application.Platform; // Add this import for Platform
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class paymentadmin {
    @FXML
    private Pane contentArea;
    @FXML
    private TableView <Payment> tblview;

    @FXML
    private TableColumn <Payment, Integer> columnid;

    @FXML
    private TableColumn <Payment, String> columnName;

    @FXML
    private TableColumn <Payment, Integer> columnHouseid;

    @FXML
    private TableColumn <Payment, Date> columnDate;

    @FXML
    private TableColumn <Payment, Double> columnRate;

    @FXML
    private TableColumn <Payment, String> columnStats;

    private User loggedinUser;
    
    public void getLoggedInUser(User user){
        this.loggedinUser = user;
    }

    @FXML
    public void initialize() {
        // Initialize columns
        columnid.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        columnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        columnHouseid.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHouseid()).asObject());
        columnDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        columnRate.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMonthly_rate()).asObject());
        columnStats.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStats()));
        

       List <Payment> payment = getpaymentsondatabase.getPaymentsFromDatabase();
       tblview.getItems().addAll(payment);
    }

    @FXML
    private void gotoHVL(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientHouse.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
    
        ClientHouseControll CHL = loader.getController();
        CHL.getLoggedInUser(loggedinUser);
    }
    @FXML
    private void gotoTenant(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tenants.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
        TenantController TCH = loader.getController();
        TCH.getLoggedInUser(loggedinUser);
    }
    @FXML
    private  void gotoDashboard(ActionEvent event) throws Exception {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
          Pane pane = loader.load();  
          contentArea.getChildren().setAll(pane);
      
      
          DashboardControll DBC = loader.getController();
         DBC.setLoggedInUser(loggedinUser);
      }

      @FXML
      private void btnlogout(ActionEvent event) throws IOException {
          Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
          confirmationDialog.setTitle("Confirmation");
          confirmationDialog.setHeaderText(null);
          confirmationDialog.setContentText("Are you sure you want to exit, " + loggedinUser.getFname() + "?");
      
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
              loggedinUser.LogCredential();
          } else {
              // User clicked Cancel, do nothing
          }
      }
      @FXML
      private void gotoAccount(ActionEvent event) throws Exception{
      accountcontrolleradmin acc = new accountcontrolleradmin(loggedinUser);
  
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Account(Admin).fxml"));
      loader.setController(acc);
  
      Pane pane = loader.load();  
  
      contentArea.getChildren().setAll(pane);
  
      System.out.println("loggedInUser is not null: " + (loggedinUser != null));
  
      
      }
}


class getpaymentsondatabase {

    final static String Duser = "root";
    final static String Dpass = "Raizen092103";
     final static String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

 
 private static final String SELECT_ALL_PAYMENTS = "SELECT * FROM payment";

 // Method to retrieve tenants from the database
 public static List <Payment> getPaymentsFromDatabase() {
    List <Payment> payments = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass);
         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PAYMENTS);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("Name");
            int houseNumber = resultSet.getInt("houseid");
            double monthlyRate = resultSet.getDouble("monthly_rate");
            Date lastPayment = resultSet.getDate("Paydate");
            String stats = resultSet.getString("stats");
            Payment payment = new Payment(id, houseNumber, name, stats, monthlyRate, lastPayment);
            payments.add(payment);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return payments;
}

}

