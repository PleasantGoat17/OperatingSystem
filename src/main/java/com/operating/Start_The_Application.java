package com.operating;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Start_The_Application extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // 加载 FXML 文件
        FXMLLoader fxmlLoader = new FXMLLoader(Start_The_Application.class.getResource("fxml/menu.fxml"));
        // 创建 Scene
        Scene scene = new Scene(fxmlLoader.load());
        // 设置 Stage
        stage.setTitle("操作系统课程设计-Lau_Zega ©2024");
        stage.setScene(scene);
        stage.show();
    }
}
