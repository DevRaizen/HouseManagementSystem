package javafxapplication6;

import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Date;

import com.gluonhq.charm.glisten.control.Alert;

public class EditDialog extends Stage {
    private TextField nameField;
    
    private TextField monthlyRateField;
    private DatePicker lastPayPicker; // Add DatePicker field for last payment

    public EditDialog(Tenant tenant) {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label nameLabel = new Label("Name:");
        nameField = new TextField(tenant.getName());
        gridPane.addRow(0, nameLabel, nameField);

        Label monthlyRateLabel = new Label("Monthly Rate:");
        monthlyRateField = new TextField(String.valueOf(tenant.getMonthlyRate()));
        gridPane.addRow(2, monthlyRateLabel, monthlyRateField);

        Label lastPayLabel = new Label("Last Payment:");
        if (tenant.getLastPayment() != null) {
            lastPayPicker = new DatePicker(tenant.getLastPayment().toLocalDate());
        } else {
            lastPayPicker = new DatePicker();
        }
        gridPane.addRow(3, lastPayLabel, lastPayPicker);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            // Update the tenant object with the values from the text fields and date picker
            tenant.setName(nameField.getText());
            tenant.setMonthlyRate(Double.parseDouble(monthlyRateField.getText()));
            tenant.setLastPayment(java.sql.Date.valueOf(lastPayPicker.getValue())); // Convert LocalDate to SQL Date
        
            // Call the updateTenant method from DatabaseHandler to update the database
            boolean updated = DatabaseHandler.updateTenant(tenant);
            
            if (updated) {
                // Close the dialog after saving if update was successful
                this.close();
            } else {
                // Show an error message if the update failed
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Failed to update tenant. Please try again.");
                alert.showAndWait();
            }
        });
        
        gridPane.addRow(4, saveButton);

        this.setScene(new Scene(gridPane, 300, 200));
    }
}
