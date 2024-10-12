package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 入口文件
 *
 * @author allen
 */
public class PasswordApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 加载 FXML 文件
        FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 770, 450);

        // 设置窗口标题和场景
        stage.setTitle("Password Manager");
        stage.setScene(scene);
        stage.show();
    }
}
