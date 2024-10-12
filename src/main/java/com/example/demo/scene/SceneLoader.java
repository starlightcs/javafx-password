package com.example.demo.scene;

import com.example.demo.PasswordApplication;
import com.example.demo.controller.EditPasswordController;
import com.example.demo.controller.GroupManagementController; // 引入新的控制器
import com.example.demo.controller.PasswordController;
import com.example.demo.domain.PasswordRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 场景加载器
 *
 * @author allen
 */
public class SceneLoader {

    // 编辑密码的场景加载器
    public static void loadEditPasswordScene(PasswordRecord record, PasswordController passwordController) {
        try {
            FXMLLoader loader = new FXMLLoader(PasswordApplication.class.getResource("edit-password-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(record == null ? "Create Password" : "Edit Password");

            // 获取控制器并设置记录
            EditPasswordController controller = loader.getController();
            controller.setRecord(record);
            controller.setPasswordController(passwordController); // 传递 PasswordController 实例

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载并返回 GroupManagementController 的方法
    public static GroupManagementController loadGroupManagementScene() {
        try {
            FXMLLoader loader = new FXMLLoader(PasswordApplication.class.getResource("group-management-view.fxml")); // 确保路径正确
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Manage Groups");

            // 获取控制器实例
            GroupManagementController controller = loader.getController();
            stage.showAndWait(); // 显示并等待用户操作

            // 返回控制器以便调用处能与之交互
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 如果出现异常，返回 null
        }
    }
}
