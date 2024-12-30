package com.operating.controller;

import com.operating._process_manage.PCB;
import com.operating._process_manage.ProcessManager;
import com.operating._process_manage.SchedulingAlgorithm;
import com.operating.model.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.util.Optional;


public class _Process_Manage_Controller {


    private final ObservableList<PCB> processData;
    @FXML
    private TableView<PCB> processTable;
    @FXML
    private TableColumn<PCB, String> colProcessID;
    @FXML
    private TableColumn<PCB, Integer> colBurstTime;
    @FXML
    private TableColumn<PCB, Integer> colRemainingTime;
    @FXML
    private TableColumn<PCB, Integer> colArrivalTime;
    @FXML
    private TableColumn<PCB, Integer> colRemainingArrivalTime;
    @FXML
    private TableColumn<PCB, Integer> colPriority;
    @FXML
    private TableColumn<PCB, String> colState;
    @FXML
    private TextArea outputArea;
    private ProcessManager processManager;
    @FXML
    private Label Algorithm;

    public _Process_Manage_Controller() {
        processManager = new ProcessManager();
        processData = FXCollections.observableArrayList();
    }

    // 设置 ProcessManager
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @FXML
    public void initialize() {
        // 初始化表格列与 PCB 属性的绑定
        colProcessID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBurstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        colRemainingTime.setCellValueFactory(new PropertyValueFactory<>("remainingTime"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colRemainingArrivalTime.setCellValueFactory(new PropertyValueFactory<>("remainingArrivalTime"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        // 设置表格数据源
        processTable.setItems(processData);
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        appendText("当前使用的调度算法为：" + Algorithm.getText() + "\n");
    }

    // 创建进程
    @FXML
    private void handleCreateProcess() {
        // 弹出自定义对话框
        Optional<Pair<String, Pair<Pair<Integer, Integer>, Integer>>> result = DialogUtils.showCreateProcessDialog(processData);

        result.ifPresent(input -> {
            try {
                // 从对话框获取输入
                String id = input.getKey(); // 进程 ID
                int burstTime = input.getValue().getKey().getKey(); // 运行时间
                int priority = input.getValue().getKey().getValue(); // 优先级
                int arrivalTime = input.getValue().getValue(); // 到达时间

                // 创建进程
                PCB process = new PCB(id, burstTime, priority, arrivalTime); // 包含到达时间
                process.setState("READY");
                processManager.createProcess(process);

                // 更新表格
                processData.add(process);

                // 日志输出
                appendText("创建进程成功: " + process);
            } catch (Exception e) {
                appendText("创建进程失败: 输入有误！");
            }
        });
    }


    @FXML
    private void handleClearProcesses() {
        // 调用 DialogUtils 来弹出确认对话框
        boolean confirmed = DialogUtils.showConfirmationDialog(
                "你确定要清空所有进程吗？",
                "所有进程将被删除！"
        );

        if (confirmed) {
            // 清空 ProcessManager 中的进程列表
            processManager.clearAllProcesses();

            // 清空 ObservableList 数据
            processData.clear();

            // 更新表格显示
            processTable.refresh();

            // 输出日志
            appendText("所有进程已清空！");
        } else {
            // 用户取消了操作
            appendText("清空操作已取消！");
        }
    }

    // 新增重置进程按钮的处理方法，增加确认弹窗
    @FXML
    private void handleResetProcesses() {
        // 调用 DialogUtils 来弹出确认对话框
        boolean confirmed = DialogUtils.showConfirmationDialog(
                "你确定要重置所有进程吗？",
                "所有进程将恢复到刚创建的状态！"
        );

        if (confirmed) {
            // 遍历所有进程，重置它们的状态和剩余时间
            for (PCB process : processData) {
                // 重置进程状态和剩余时间
                process.setState("READY");
                process.setRemainingTime(process.getBurstTime()); // 假设 burstTime 为初始的执行时间

                // 更新表格
                processTable.refresh();
            }

            // 输出日志
            appendText("所有进程已重置到刚创建的状态！");
        } else {
            // 用户取消了操作
            appendText("重置操作已取消！");
        }
    }


    @FXML
    private void handleSimulateTime() {
        // 调用 SimulateTimeDialog 类获取时间片大小
        Optional<String> result = DialogUtils.showSimulateTimeDialog();
        result.ifPresent(input -> {
            try {
                int timeSlice = Integer.parseInt(input);
                processManager.simulateTime(timeSlice); // 传递给模拟方法
                // 更新表格
                processData.clear();
                processData.addAll(processManager.getProcessList());
                appendText("模拟时间进度完成。");
            } catch (Exception e) {
                appendText("模拟时间进度失败: 输入格式错误！");
            }
        });
    }


    @FXML
    private void handleSetFCFS() {
        processManager.setSchedulingAlgorithm(SchedulingAlgorithm.FCFS);
        appendText("切换到算法：先来先服务 (FCFS)");
        Algorithm.setText("FCFS");
    }

    // 设置 RR 算法
    @FXML
    private void handleSetRR() {
        processManager.setSchedulingAlgorithm(SchedulingAlgorithm.RR);
        appendText("切换到算法：时间片轮转 (RR)");
        Algorithm.setText("RR");
    }

    // 设置 SJF 算法
    @FXML
    private void handleSetSJF() {
        processManager.setSchedulingAlgorithm(SchedulingAlgorithm.SJF);
        appendText("切换到算法：最短作业优先 (SJF)");
        Algorithm.setText("SJF");
    }

    // 设置 优先级调度 算法
    @FXML
    private void handleSetPriorityScheduling() {
        processManager.setSchedulingAlgorithm(SchedulingAlgorithm.PRIORITY_SCHEDULING);
        appendText("切换到算法：优先级调度(Priority Scheduling)");
        Algorithm.setText("Priority Scheduling");
    }


    // 追加日志
    public void appendText(String text) {
        outputArea.appendText(text + "\n");
    }
}
