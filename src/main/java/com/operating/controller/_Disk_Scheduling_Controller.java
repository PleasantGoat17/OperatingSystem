package com.operating.controller;

import com.operating._disk_scheduling.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class _Disk_Scheduling_Controller {
    private DiskSchedulingAlgorithm algorithm;

    @FXML
    private Label Current_Algorithm;
    @FXML
    private TextArea TrackSequenceTextArea;
    @FXML
    private TextArea ServiceSequenceTextArea;
    @FXML
    private Label TotalMovementLabel;

    private List<Integer> trackSequence = new ArrayList<>();

    public void initialize() {
        this.algorithm = new FCFSAlgorithm(); // 默认算法为 FCFS
        Current_Algorithm.setText("FCFS");
    }

    @FXML
    public void handleSetFCFS(ActionEvent actionEvent) {
        algorithm = new FCFSAlgorithm();
        Current_Algorithm.setText("FCFS");
    }

    @FXML
    public void handleSetSSTF(ActionEvent actionEvent) {
        algorithm = new SSTFAlgorithm();
        Current_Algorithm.setText("SSTF");
    }

    @FXML
    public void handleSetSCAN(ActionEvent actionEvent) {
        algorithm = new SCANAlgorithm();
        Current_Algorithm.setText("SCAN");
    }

    @FXML
    public void handleOpenFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择磁道请求序列文件");

        // 设置初始目录
        File initialDirectory = new File("src/main/resources/com/operating/disk_scheduling_file");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                trackSequence.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String num : line.split("\\s+")) {
                        trackSequence.add(Integer.parseInt(num));
                    }
                }
                TrackSequenceTextArea.setText(trackSequence.toString());
            } catch (IOException | NumberFormatException e) {
                showError("文件加载失败，请确保文件内容格式正确！");
            }
        }
    }

    @FXML
    public void handleRunAlgorithm(ActionEvent actionEvent) {
        if (trackSequence.isEmpty()) {
            showError("磁道请求序列为空，请先加载文件！");
            return;
        }

        // 假设磁盘起始位置为 0
        int startPosition = 0;
        DiskSchedulingResult result = algorithm.execute(trackSequence, startPosition);

        // 显示磁道服务顺序
        ServiceSequenceTextArea.setText("磁道服务顺序：" + result.getServiceSequence().toString());

        // 显示总移动道数
        TotalMovementLabel.setText("总移动道数：" + result.getTotalMovement());
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        trackSequence.clear();
        TrackSequenceTextArea.clear();
        ServiceSequenceTextArea.clear();
        TotalMovementLabel.setText("0");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
