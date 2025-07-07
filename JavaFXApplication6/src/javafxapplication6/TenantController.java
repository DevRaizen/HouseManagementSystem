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

public class TenantController {
    @FXML
    private Pane contentArea;
    @FXML
    private TableView <Tenant> tenantTableView;

    @FXML
    private TableColumn<Tenant, Integer> idColumn;

    @FXML
    private TableColumn<Tenant, String> nameColumn;

    @FXML
    private TableColumn<Tenant,Integer> houseColumn;

    @FXML
    private TableColumn<Tenant, Double> monthlyRateColumn;

    @FXML
    private TableColumn<Tenant, Date> lastPayColumn;

    @FXML
    private TableColumn<Tenant, Void> actionColumn;
    
    private User loggedInUser;
    private House logHouse;

    public void  getLoggedInUser(User user){
        this.loggedInUser = user;
    }

    public void getHouse(House user){
        this.logHouse = user;
    }
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        houseColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHouseNumber()).asObject());;
        monthlyRateColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMonthlyRate()).asObject());
        lastPayColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastPayment()));
        idColumn.setStyle("-fx-alignment: CENTER;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        houseColumn.setStyle("-fx-alignment: CENTER;");
        monthlyRateColumn.setStyle("-fx-alignment: CENTER;");
        lastPayColumn.setStyle("-fx-alignment: CENTER;");

        setupActionColumn();

        List<Tenant> tenants = DatabaseHandler.getTenantsFromDatabase();
        tenantTableView.getItems().addAll(tenants);
    }

    private void setupActionColumn() {
        Callback<TableColumn<Tenant, Void>, TableCell <Tenant, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Tenant, Void> call(final TableColumn<Tenant, Void> param) {
                final TableCell<Tenant, Void> cell = new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    
                    {
                        editButton.setStyle("-fx-background-color:  #0F969C; -fx-text-fill: #ffffff;");
                        deleteButton.setStyle("-fx-background-color:  #0F969C; -fx-text-fill: #ffffff;");

                        editButton.setOnAction(event -> {
                            // Get the selected tenant from the table view
                            Tenant tenant = tenantTableView.getSelectionModel().getSelectedItem();
                            
                            if (tenant != null) {
                                // Create and show the edit dialog
                                EditDialog editDialog = new EditDialog(tenant);
                                editDialog.showAndWait(); // Show the dialog and wait for it to be closed
                                
                                // Update the table view if changes were made
                                tenantTableView.refresh();
                            } else {
                                // Handle case where no tenant is selected
                                System.out.println("No tenant selected for editing.");
                                Alert confirmationDialog = new Alert(AlertType.WARNING);
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Edit Tenant");
                                confirmationDialog.setContentText("Please select a row to Edit Tenant");
                                confirmationDialog.showAndWait();
                            }
                        });
                        
                        deleteButton.setOnAction(event -> {
                            // Get the selected Tenant
                            Tenant tenant = getTableView().getItems().get(getIndex());
                            
                            // Confirm deletion with a dialog
                            Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
                            confirmationDialog.setTitle("Confirmation");
                            confirmationDialog.setHeaderText("Delete Tenant");
                            confirmationDialog.setContentText("Are you sure you want to delete " + tenant.getName() + "?");
                        
                            // Handle the user's response
                            confirmationDialog.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    // Delete the Tenant from the TableView
                                    tenantTableView.getItems().remove(tenant);
                                    
                                    // Delete the Tenant record from the database
                                    DatabaseHandler.deleteTenant(tenant.getId(),tenant.getHouseNumber());
                                }
                            });
                        });
                        
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Add buttons to the cell only for non-empty rows
                            HBox buttonsBox = new HBox(editButton, deleteButton);
        
                            // Set spacing between buttons
                            buttonsBox.setSpacing(5);
                            buttonsBox.setPadding(new Insets(2, 16, 2, 0));
                            
                            // Center align buttons within the HBox
                            buttonsBox.setAlignment(Pos.CENTER);
                            setGraphic(buttonsBox);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
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
    private void gotoHVL(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientHouse.fxml"));
        Pane pane = loader.load();  
        contentArea.getChildren().setAll(pane);
    
    
        ClientHouseControll CHL = loader.getController();
        CHL.getLoggedInUser(loggedInUser);
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


class DatabaseHandler {

       final static String Duser = "root";
       final static String Dpass = "Raizen092103";
        final static String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";

    
    private static final String SELECT_ALL_TENANTS = "SELECT * FROM tenant";

    // Method to retrieve tenants from the database
    public static List<Tenant> getTenantsFromDatabase() {
        List<Tenant> tenants = new ArrayList<>();
        try (
            
            Connection connection = DriverManager.getConnection(Durl, Duser, Dpass);
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TENANTS);

            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            
            while (resultSet.next()) {
                
                int id = resultSet.getInt("id");
                String name = resultSet.getString("Name");
                int houseNumber = resultSet.getInt("house_num");
                double monthlyRate = resultSet.getDouble("monthly_rate");
                Date lastPayment = resultSet.getDate("LastPay");
                Date appdate = resultSet.getDate("appdate");
                Tenant tenant = new Tenant(id, name, houseNumber, monthlyRate, lastPayment,appdate);
                tenants.add(tenant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return tenants;
    }
    public static void deleteTenant(int tenantId, int house_id) {
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
            preparedStatement.setInt(1, tenantId);
            preparedStatement.setInt(2, house_id);
            // Execute the delete statement for tenant
            preparedStatement.executeUpdate();
            
            // Execute the update statement for house
            updateHouseStatement.setInt(1, 0);
            updateHouseStatement.setInt(2, tenantId);
            updateHouseStatement.setInt(3, house_id);
            updateHouseStatement.executeUpdate();

            updateClientStatement.setInt(1, tenantId);
            updateClientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean updateTenant(Tenant tenant) {
        // SQL statement to update a tenant
        String updateQuery = "UPDATE tenant SET Name = ?, monthly_rate = ?, LastPay = ? WHERE id = ?";
    
        try (
            Connection connection = DriverManager.getConnection(Durl, Duser, Dpass);
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        ) {
            // Set parameters for the prepared statement
            preparedStatement.setString(1, tenant.getName());
            preparedStatement.setDouble(2, tenant.getMonthlyRate());
            preparedStatement.setDate(3, tenant.getLastPayment());
            preparedStatement.setInt(4, tenant.getId());
    
            // Execute the update statement
            int rowsAffected = preparedStatement.executeUpdate();
    
            // Check if any rows were affected (i.e., if the update was successful)
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Update failed
        }
    }


}
