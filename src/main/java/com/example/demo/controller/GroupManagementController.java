package com.example.demo.controller;

import com.example.demo.domain.GroupRecord;
import com.example.demo.utils.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class GroupManagementController {

    @FXML
    private TextField groupNameField;

    @FXML
    private ListView<GroupRecord> groupListView;

    private ObservableList<GroupRecord> groupList;

    // 添加 PasswordController 的引用
    private PasswordController passwordController;

    @FXML
    public void initialize() {
        loadGroups();
        setupListView(); // 设置 ListView 的显示格式
    }

    public void setPasswordController(PasswordController passwordController) {
        this.passwordController = passwordController; // 设置 PasswordController
    }

    private void loadGroups() {
        List<GroupRecord> groups = DatabaseHelper.getAllGroups();
        groupList = FXCollections.observableArrayList(groups);
        groupListView.setItems(groupList);
    }

    private void setupListView() {
        // 使用 CellFactory 来定制 ListView 的每一项的显示
        groupListView.setCellFactory(param -> new ListCell<GroupRecord>() {
            @Override
            protected void updateItem(GroupRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // 设置显示格式为 "groupName"
                    setText(item.getGroupName());
                }
            }
        });
    }

    @FXML
    private void onCreateGroupButtonClick() {
        String groupName = groupNameField.getText().trim();
        if (!groupName.isEmpty()) {
            DatabaseHelper.addGroup(new GroupRecord(0, groupName));
            loadGroups();
            groupNameField.clear();
        }
    }

    @FXML
    private void onEditGroupButtonClick() {
        GroupRecord selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            String newGroupName = groupNameField.getText().trim();
            if (!newGroupName.isEmpty()) {
                DatabaseHelper.updateGroup(new GroupRecord(selectedGroup.getId(), newGroupName));
                loadGroups();
                groupNameField.clear();
            }
        }
    }

    @FXML
    private void onDeleteGroupButtonClick() {
        GroupRecord selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            DatabaseHelper.deleteGroup(selectedGroup);
            loadGroups();
            groupNameField.clear();
        }
    }

    @FXML
    private void onGroupSelected() {
        GroupRecord selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            groupNameField.setText(selectedGroup.getGroupName());
        }
    }
}
