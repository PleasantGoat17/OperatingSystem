package com.operating.controller;

import com.operating._memory_manage.FixedPartition;
import com.operating._memory_manage.MemoryManager;
import com.operating._memory_manage.PartitionType;
import com.operating._memory_manage.VariablePartition;
import com.operating.model.DialogUtils;
import com.operating.model.TableViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Optional;

public class _Memory_Allocator_Controller {

    private final MemoryManager memoryManager = new MemoryManager(); // 内存管理器
    @FXML
    private BorderPane rootPane; // 主视图
    @FXML
    private Label WAY; // 分区方式显示标签
    private PartitionType partitionType = PartitionType.FIXED; // 默认分区方式为固定分区

    @FXML
    public void initialize() {
        handleSetFixed_Partition(null); // 初始化默认显示固定分区
    }

    /**
     * 处理内存分配请求
     */
    public void handleAllocateMemory(ActionEvent actionEvent) {
        Optional<Pair<String, Integer>> result = DialogUtils.showAllocateDialog(memoryManager, partitionType);
        result.ifPresent(pair -> {
            // 获取作业ID和分区ID
            String[] jobAndPartition = pair.getKey().split(" ");  // 分割作业ID和分区ID
            String jobId = jobAndPartition[0];
            String partitionId = jobAndPartition[1];
            int size = pair.getValue();
            boolean success;

            // 根据分区方式分配内存
            if (partitionType == PartitionType.FIXED) {
                success = memoryManager.allocateFixedPartition(partitionId, jobId, size);  // 使用分区ID
            } else {
                success = memoryManager.allocateVariablePartition(jobId, size);  // 可变分区不需要传递分区ID
            }

            // 根据结果弹出提示
            showResultDialog(success, "分配成功", "作业 " + jobId + " 成功分配 " + size + "K 内存！",
                    "分配失败！没有足够的可用分区。");

            // 刷新当前视图
            refreshView();
        });
    }


    /**
     * 处理内存释放请求
     */
    public void handleReleaseMemory(ActionEvent actionEvent) {
        Optional<String> result = DialogUtils.showReleaseDialog(memoryManager);
        result.ifPresent(jobId -> {
            boolean success = memoryManager.releaseMemory(jobId);

            // 根据结果弹出提示
            showResultDialog(success, "释放成功", "作业 " + jobId + " 成功释放内存！",
                    "释放失败！找不到该作业ID。");

            // 刷新当前视图
            refreshView();
        });
    }

    /**
     * 切换到固定分区视图
     */
    public void handleSetFixed_Partition(ActionEvent actionEvent) {
        partitionType = PartitionType.FIXED;
        WAY.setText("固定分区");

        // 构建固定分区的表格
        TableView<FixedPartition> fixedPartitionTable = new TableView<>();
        fixedPartitionTable.getColumns().addAll(
                TableViewUtils.createTableColumn("分区号", "partitionId", 100),
                TableViewUtils.createTableColumn("大小", "size", 100),
                TableViewUtils.createTableColumn("状态", "status", 100)
        );
        fixedPartitionTable.getItems().addAll(memoryManager.getFixedPartitions());
        fixedPartitionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 设置表格到主视图中心
        rootPane.setCenter(fixedPartitionTable);
    }

    /**
     * 切换到可变分区视图
     */
    public void handleSetVariable_Partition(ActionEvent actionEvent) {
        partitionType = PartitionType.VARIABLE;
        WAY.setText("可变分区");

        // 创建已分配的分区表
        TableView<VariablePartition> allocatedTable = new TableView<>();
        allocatedTable.getColumns().addAll(
                TableViewUtils.createTableColumn("作业号", "jobId", 150),
                TableViewUtils.createTableColumn("分配大小", "allocatedSize", 150)
        );
        allocatedTable.getItems().addAll(memoryManager.getAllocatedPartitions());
        allocatedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 创建未分配的分区表
        TableView<VariablePartition> unallocatedTable = new TableView<>();
        unallocatedTable.getColumns().addAll(
                TableViewUtils.createTableColumn("起始地址", "startAddress", 150),
                TableViewUtils.createTableColumn("大小", "size", 150)
        );
        unallocatedTable.getItems().addAll(memoryManager.getUnallocatedPartitions());
        unallocatedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 使用 VBox 布局组合两个表格
        VBox box = new VBox(10, new Label("已分配表"), allocatedTable, new Label("未分配表"), unallocatedTable);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        rootPane.setCenter(box);
    }

    /**
     * 刷新视图
     */
    private void refreshView() {
        if (partitionType == PartitionType.FIXED) {
            handleSetFixed_Partition(null);
        } else {
            handleSetVariable_Partition(null);
        }
    }

    /**
     * 显示操作结果的对话框
     */
    private void showResultDialog(boolean success, String successTitle, String successMessage, String errorMessage) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? successTitle : "操作失败");
        alert.setHeaderText(null);
        alert.setContentText(success ? successMessage : errorMessage);
        alert.showAndWait();
    }
}
