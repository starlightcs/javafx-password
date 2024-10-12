package com.example.demo.controller;

import com.example.demo.scene.SceneLoader;
import com.example.demo.domain.PasswordRecord;
import com.example.demo.utils.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class PasswordController {

    @FXML
    private TableView<PasswordRecord> passwordTable;
    @FXML
    private TableColumn<PasswordRecord, String> groupColumn;
    @FXML
    private TableColumn<PasswordRecord, String> siteColumn;
    @FXML
    private TableColumn<PasswordRecord, String> siteUrlColumn;
    @FXML
    private TableColumn<PasswordRecord, String> usernameColumn;
    @FXML
    private TableColumn<PasswordRecord, String> passwordColumn;
    @FXML
    private TableColumn<PasswordRecord, String> notesColumn;
    @FXML
    private TableColumn<PasswordRecord, Void> actionsColumn;
    @FXML
    private Pagination pagination;
    @FXML
    private Label totalCountLabel;

    private ObservableList<PasswordRecord> passwordList = FXCollections.observableArrayList();
    private static final int RECORDS_PER_PAGE = 10;

    @FXML
    public void initialize() {
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        siteColumn.setCellValueFactory(new PropertyValueFactory<>("site"));
        siteUrlColumn.setCellValueFactory(new PropertyValueFactory<>("siteUrl"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        addActionsColumn();
        loadPasswordRecords();
    }

    public void loadPasswordRecords() {
        passwordList.clear();
        passwordList.addAll(DatabaseHelper.getAllPasswordRecords());
        totalCountLabel.setText("Total: " + passwordList.size());

        pagination.setPageCount((int) Math.ceil((double) passwordList.size() / RECORDS_PER_PAGE));
        pagination.setCurrentPageIndex(0);
        updateTableView(0);

        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableView(newValue.intValue());
        });
    }

    private void updateTableView(int pageIndex) {
        int fromIndex = pageIndex * RECORDS_PER_PAGE;
        int toIndex = Math.min(fromIndex + RECORDS_PER_PAGE, passwordList.size());
        passwordTable.setItems(FXCollections.observableArrayList(passwordList.subList(fromIndex, toIndex)));
    }

    private void addActionsColumn() {
        Callback<TableColumn<PasswordRecord, Void>, TableCell<PasswordRecord, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<PasswordRecord, Void> call(final TableColumn<PasswordRecord, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox buttons = new HBox(5, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> {
                            PasswordRecord selectedRecord = getTableView().getItems().get(getIndex());
                            onEditButtonClick(selectedRecord);
                        });

                        deleteButton.setOnAction(event -> {
                            PasswordRecord selectedRecord = getTableView().getItems().get(getIndex());
                            onDeleteButtonClick(selectedRecord);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttons);
                    }
                };
            }
        };
        actionsColumn.setCellFactory(cellFactory);
    }

    @FXML
    private void onCreateButtonClick(ActionEvent event) {
        SceneLoader.loadEditPasswordScene(null, this);
    }

    @FXML
    private void onEditButtonClick(PasswordRecord selectedRecord) {
        if (selectedRecord != null) {
            SceneLoader.loadEditPasswordScene(selectedRecord, this);
        } else {
            showAlert("Error", "No password record selected.");
        }
    }

    @FXML
    private void onDeleteButtonClick(PasswordRecord selectedRecord) {
        if (selectedRecord != null) {
            DatabaseHelper.deletePasswordRecord(selectedRecord);
            loadPasswordRecords();
        } else {
            showAlert("Error", "No password record selected.");
        }
    }

    @FXML
    private void onManageGroupsClick() {
        GroupManagementController groupController = SceneLoader.loadGroupManagementScene();
        groupController.setPasswordController(this); // 传递当前 PasswordController 实例
    }

    // 获取选定的密码记录
    public PasswordRecord getSelectedPasswordRecord() {
        return passwordTable.getSelectionModel().getSelectedItem();
    }

    // 显示警告提示框
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
