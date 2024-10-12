package com.example.demo.controller;

import com.example.demo.domain.PasswordRecord;
import com.example.demo.utils.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditPasswordController {

    @FXML
    private TextField siteField;
    @FXML
    private TextField siteUrlField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea notesField;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> groupComboBox;

    private PasswordRecord currentRecord;
    private PasswordController passwordController;

    public void setPasswordController(PasswordController controller) {
        this.passwordController = controller;
    }

    @FXML
    public void initialize() {
        groupComboBox.setItems(FXCollections.observableArrayList(DatabaseHelper.getAllGroupNames()));
    }

    public void setRecord(PasswordRecord record) {
        this.currentRecord = record;
        if (record != null) {
            siteField.setText(record.getSite());
            siteUrlField.setText(record.getSiteUrl());
            usernameField.setText(record.getUsername());
            passwordField.setText(record.getPassword());
            notesField.setText(record.getNotes());
            groupComboBox.setValue(record.getGroupName());
        }
    }

    @FXML
    private void onSaveButtonClick() {
        String site = siteField.getText().trim();
        String siteUrl = siteUrlField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String notes = notesField.getText().trim();
        String group = groupComboBox.getValue();

        if (site.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Site, Username, and Password cannot be empty.");
            return;
        }

        if (currentRecord == null) {
            PasswordRecord newRecord = new PasswordRecord(0, site, siteUrl, username, password, notes, group);
            DatabaseHelper.addPasswordRecord(newRecord);
        } else {
            currentRecord.setSite(site);
            currentRecord.setSiteUrl(siteUrl);
            currentRecord.setUsername(username);
            currentRecord.setPassword(password);
            currentRecord.setNotes(notes);
            currentRecord.setGroupName(group);
            DatabaseHelper.updatePasswordRecord(currentRecord);
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        if (passwordController != null) {
            passwordController.loadPasswordRecords();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
