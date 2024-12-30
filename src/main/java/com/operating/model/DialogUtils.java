package com.operating.model;

import com.operating._memory_manage.MemoryManager;
import com.operating._memory_manage.PartitionType;
import com.operating._process_manage.PCB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Pair;

import java.util.Optional;

public class DialogUtils {

    /**
     * 创建进程输入对话框
     *
     * @return 包含 ID, 运行时间, 优先级的 Optional
     */
    public static Optional<Pair<String, Pair<Pair<Integer, Integer>, Integer>>> showCreateProcessDialog(ObservableList<PCB> processData) {
        // 创建自定义对话框
        Dialog<Pair<String, Pair<Pair<Integer, Integer>, Integer>>> dialog = new Dialog<>();
        dialog.setTitle("创建进程");
        dialog.setHeaderText("请输入进程参数");

        // 设置按钮
        ButtonType createButtonType = new ButtonType("创建", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // 创建输入字段
        TextField idField = new TextField();
        idField.setPromptText("P" + (processData.size() + 1));
        idField.setEditable(false);
        idField.setMouseTransparent(true);  // 禁用鼠标交互
        idField.setFocusTraversable(false);  // 禁用焦点事件

        TextField burstTimeField = new TextField();
        burstTimeField.setPromptText("运行时间");

        // 替换为 ComboBox，用于选择优先级
        ComboBox<Integer> priorityComboBox = new ComboBox<>();
        priorityComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        priorityComboBox.setPromptText("选择优先级");

        TextField arrivalTimeField = new TextField();
        arrivalTimeField.setPromptText("到达时间");

        // 布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("进程 ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("运行时间:"), 0, 1);
        grid.add(burstTimeField, 1, 1);
        grid.add(new Label("优先级:"), 0, 2);
        grid.add(priorityComboBox, 1, 2);
        grid.add(new Label("到达时间:"), 0, 3);
        grid.add(arrivalTimeField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // 禁用按钮，直到所有字段都有输入
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        burstTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(
                    burstTimeField.getText().trim().isEmpty() ||
                            priorityComboBox.getValue() == null ||
                            arrivalTimeField.getText().trim().isEmpty()
            );
        });

        priorityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(
                    burstTimeField.getText().trim().isEmpty() ||
                            priorityComboBox.getValue() == null ||
                            arrivalTimeField.getText().trim().isEmpty()
            );
        });

        arrivalTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(
                    burstTimeField.getText().trim().isEmpty() ||
                            priorityComboBox.getValue() == null ||
                            arrivalTimeField.getText().trim().isEmpty()
            );
        });

        // 将结果转换为所需格式
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    String id = "P" + (processData.size() + 1);
                    int burstTime = Integer.parseInt(burstTimeField.getText().trim());
                    int priority = priorityComboBox.getValue();
                    int arrivalTime = Integer.parseInt(arrivalTimeField.getText().trim());
                    return new Pair<>(id, new Pair<>(new Pair<>(burstTime, priority), arrivalTime));
                } catch (NumberFormatException e) {
                    return null; // 如果输入非数字，直接返回 null
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<String> showSimulateTimeDialog() {
        // 创建自定义对话框
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("模拟时间进度");
        dialog.setHeaderText("请输入时间片大小：");

        // 设置按钮
        ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 创建输入框和按钮
        TextField timeSliceField = new TextField("10");
        timeSliceField.setPromptText("时间片");

        // 创建加减按钮
        Button btnIncrease = new Button("+10");
        btnIncrease.setOnAction(event -> {
            try {
                int currentValue = Integer.parseInt(timeSliceField.getText().trim());
                timeSliceField.setText(String.valueOf(currentValue + 10));  // 加 10
            } catch (NumberFormatException e) {
                timeSliceField.setText("1");  // 如果输入非数字，重置为 1
            }
        });

        Button btnDecrease = new Button("-10");
        btnDecrease.setOnAction(event -> {
            try {
                int currentValue = Integer.parseInt(timeSliceField.getText().trim());
                // 如果当前时间片大于 10，则减去 10，否则不改变
                if (currentValue > 10) {
                    timeSliceField.setText(String.valueOf(currentValue - 10));  // 减 10
                }
            } catch (NumberFormatException e) {
                timeSliceField.setText("1");  // 如果输入非数字，重置为 1
            }
        });

        // 使用 HBox 来布局时间片输入框和加减按钮
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(timeSliceField, btnIncrease, btnDecrease);

        // 设置对话框内容
        dialog.getDialogPane().setContent(hBox);

        // 确认按钮操作
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return timeSliceField.getText().trim();
            }
            return null;
        });

        // 显示对话框并获取用户输入
        return dialog.showAndWait();
    }

    // 弹出确认对话框
    public static boolean showConfirmationDialog(String headerText, String contentText) {
        // 创建确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认操作");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // 显示对话框并等待用户的响应
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static Optional<Pair<String, Integer>> showAllocateDialog(MemoryManager memoryManager, PartitionType partitionType) {
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("分配空间");
        dialog.setHeaderText("请输入分配大小");

        // 创建输入框
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // 自动生成作业ID
        String jobId = "Job" + (memoryManager.getJobSize() + 1); // 基于当前作业数生成唯一ID

        TextField jobIdField = new TextField(jobId); // 将自动生成的ID赋值到文本框
        jobIdField.setPromptText("作业ID");
        jobIdField.setEditable(false); // 禁止编辑
        TextField sizeField = new TextField();
        sizeField.setPromptText("分配大小（K）");

        ComboBox<String> partitionComboBox = new ComboBox<>();
        partitionComboBox.getItems().addAll(memoryManager.getPartitionIds(partitionType)); // 获取分区ID
        partitionComboBox.setPromptText("选择分区");

        grid.add(new Label("作业ID:"), 0, 0);
        grid.add(jobIdField, 1, 0);
        grid.add(new Label("分配大小:"), 0, 1);
        grid.add(sizeField, 1, 1);
        grid.add(new Label("选择分区:"), 0, 2);
        grid.add(partitionComboBox, 1, 2);

        GridPane.setHgrow(jobIdField, Priority.ALWAYS);
        GridPane.setHgrow(sizeField, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);

        // 设置按钮
        ButtonType okButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        // 获取“确定”按钮
        Button ok = (Button) dialog.getDialogPane().lookupButton(okButton);
        ok.setDisable(true);  // 初始化时禁用“确定”按钮

        // 监听ComboBox变化，确保选择了分区后才启用“确定”按钮
        partitionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // 如果选择了一个分区，启用“确定”按钮；否则禁用
            ok.setDisable(newValue == null || newValue.isEmpty());
        });

        // 输入框验证分配大小
        sizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 如果分配大小无效，禁用“确定”按钮
            try {
                Integer.parseInt(newValue.trim());
                ok.setDisable(partitionComboBox.getValue() == null);  // 如果未选择分区，禁用
            } catch (NumberFormatException e) {
                ok.setDisable(true);  // 如果输入无效，禁用“确定”按钮
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                try {
                    int size = Integer.parseInt(sizeField.getText().trim());
                    String selectedPartitionId = partitionComboBox.getValue();  // 获取选中的分区ID
                    return new Pair<>(jobId + " " + selectedPartitionId, size);  // 组合作业ID和分区ID
                } catch (NumberFormatException e) {
                    return null; // 输入非法
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    // 弹出释放空间的对话框
    public static Optional<String> showReleaseDialog(MemoryManager memoryManager) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("释放空间");
        dialog.setHeaderText("请选择需要释放的作业ID");

        // 创建ComboBox
        ComboBox<String> jobIdComboBox = new ComboBox<>();
        jobIdComboBox.getItems().addAll(memoryManager.getAllJobIds()); // 获取所有作业ID

        jobIdComboBox.setPromptText("选择作业ID");

        // 将ComboBox添加到布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("作业ID:"), 0, 0);
        grid.add(jobIdComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // 设置按钮
        ButtonType okButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return jobIdComboBox.getValue();  // 返回选中的作业ID
            }
            return null;  // 用户点击取消
        });

        return dialog.showAndWait();
    }


}
