package com.operating.controller;

import com.operating._virtual_memory_manage.*;
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

public class _Virtual_Memory_Manage_Controller {
    private SchedulingAlgorithm algorithm;

    @FXML
    private Label Current_Algorithm;
    @FXML
    private TextArea PageSequenceTextArea;
    @FXML
    private TextArea EvictedPagesTextArea;
    @FXML
    private Label PageFaultCountLabel;

    private List<Integer> pageSequence = new ArrayList<>();

    public void initialize() {
        this.algorithm = new FIFOAlgorithm();// 默认算法为 FIFO
        Current_Algorithm.setText("FIFO");
    }

    @FXML
    public void handleSetFIFO(ActionEvent actionEvent) {
        algorithm = new FIFOAlgorithm();
        Current_Algorithm.setText("FIFO");
    }

    @FXML
    public void handleSetLRU(ActionEvent actionEvent) {
        algorithm = new LRUAlgorithm();
        Current_Algorithm.setText("LRU");
    }

    @FXML
    public void handleSetLFU(ActionEvent actionEvent) {
        algorithm = new LFUAlgorithm();
        Current_Algorithm.setText("LFU");
    }

    @FXML
    public void handleOpenFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择页面序列文件");

        // 设置初始目录为 "com/operating/virtual_memory_file"
        File initialDirectory = new File("src/main/resources/com/operating/virtual_memory_file");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                pageSequence.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String num : line.split("\\s+")) {
                        pageSequence.add(Integer.parseInt(num));
                    }
                }
                PageSequenceTextArea.setText(pageSequence.toString());
            } catch (IOException | NumberFormatException e) {
                showError("文件加载失败，请确保文件内容格式正确！");
            }
        }
    }


    @FXML
    public void handleRunAlgorithm(ActionEvent actionEvent) {
        if (pageSequence.isEmpty()) {
            showError("页面序列为空，请先加载文件！");
            return;
        }

        // 假设内存容量为 4
        int capacity = 4;
        SchedulingResult result = algorithm.execute(pageSequence, capacity);

        // 显示淘汰的页面号
        EvictedPagesTextArea.setText("淘汰的页面号：" + result.getEvictedPages().toString());

        // 显示内存状态
        StringBuilder memoryStateBuilder = new StringBuilder("原始序列：\n" + PageSequenceTextArea.getText() + "\n内存状态：\n");
        List<List<Integer>> memoryStates = result.getMemoryStates();

        if (memoryStates != null) {
            for (int i = 0; i < memoryStates.size(); i++) {
                memoryStateBuilder.append("步骤 ").append(i + 1).append(": ")
                        .append(memoryStates.get(i).toString()).append("\n");
            }
        } else {
            memoryStateBuilder.append("内存状态不可用！");
        }

        PageSequenceTextArea.setText(memoryStateBuilder.toString());

        // 显示缺页次数
        PageFaultCountLabel.setText("缺页次数：" + result.getPageFaultCount());
    }


    @FXML
    public void handleClear(ActionEvent actionEvent) {
        pageSequence.clear();
        PageSequenceTextArea.clear();
        EvictedPagesTextArea.clear();
        PageFaultCountLabel.setText("0");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
