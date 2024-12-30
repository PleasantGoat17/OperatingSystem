package com.operating.controller;


import com.operating.Start_The_Application;
import com.operating._process_manage.ProcessManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void goToFunction1() {
        FXMLLoader fxmlLoader = new FXMLLoader(Start_The_Application.class.getResource("fxml/process_manage.fxml"));
        // 创建 Scene
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取 MainController 实例
        _Process_Manage_Controller controller = fxmlLoader.getController();

        // 创建 ProcessManager，并将 controller 传递给它
        ProcessManager processManager = new ProcessManager(controller);

        // 将 processManager 设置到 controller 中（假设你有方法将 processManager 设置到 controller）
        controller.setProcessManager(processManager);

        // 设置 Stage
        Stage stage = new Stage();
        stage.setTitle("操作系统课程设计-Lau_Zega ©2024-进程调度");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToFunction2() {
        FXMLLoader fxmlLoader = new FXMLLoader(Start_The_Application.class.getResource("fxml/memory_allocator.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("操作系统课程设计-Lau_Zega ©2024-存贮器管理");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToFunction3() {
        FXMLLoader fxmlLoader = new FXMLLoader(Start_The_Application.class.getResource("fxml/virtual_memory_manage.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("操作系统课程设计-Lau_Zega ©2024-虚拟存储器管理");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToFunction4() {
        FXMLLoader fxmlLoader = new FXMLLoader(Start_The_Application.class.getResource("fxml/disk_scheduling.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("操作系统课程设计-Lau_Zega ©2024-文件管理");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }
}
