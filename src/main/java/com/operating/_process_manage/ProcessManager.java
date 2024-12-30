package com.operating._process_manage;

import com.operating.controller._Process_Manage_Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProcessManager {
    private final List<PCB> processList;
    private SchedulingAlgorithm currentAlgorithm;
    private _Process_Manage_Controller processManageController; // 引用 MainController

    public ProcessManager(_Process_Manage_Controller processManageController) {
        this.processList = new ArrayList<>();
        this.currentAlgorithm = SchedulingAlgorithm.FCFS; // 默认调度算法
        this.processManageController = processManageController; // 初始化引用
    }

    public ProcessManager() {
        this.processList = new ArrayList<>();
        this.currentAlgorithm = SchedulingAlgorithm.FCFS; // 默认调度算法
    }

    public void setSchedulingAlgorithm(SchedulingAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
    }

    // 创建进程
    public void createProcess(PCB process) {
        process.resetToReady(); // 确保初始状态为 READY
        processList.add(process);
    }

    // 模拟时间片
    public void simulateTime(int timeSlice) {
        StringBuilder statusLog = new StringBuilder();

        switch (currentAlgorithm) {
            case FCFS:
                simulateFCFS(statusLog, timeSlice);
                break;
            case RR:
                simulateRR(timeSlice, statusLog, timeSlice);
                break;
            case SJF:
                simulateSJF(timeSlice, statusLog, timeSlice);
                break;
            case PRIORITY_SCHEDULING:
                simulatePriorityScheduling(statusLog, timeSlice);
                break;
        }

        // 每次模拟时间片后，打印所有进程的状态
        printAllProcessesStatus(statusLog);

        // 调用 MainController 的 appendText 输出日志
        processManageController.appendText(statusLog.toString());
    }

    // 打印所有进程的状态
    private void printAllProcessesStatus(StringBuilder statusLog) {
        statusLog.append("\n所有进程的当前状态：\n");
        for (PCB process : processList) {
            statusLog.append("进程ID: ").append(process.getId())
                    .append(", 状态: ").append(process.getState())
                    .append(", 剩余时间: ").append(process.getRemainingTime())
                    .append(", 剩余到达时间: ").append(process.getRemainingArrivalTime())
                    .append("\n");
        }
    }

    //FCFS
    private void simulateFCFS(StringBuilder statusLog, int totalTimeSlices) {
        // 根据到达时间排序进程列表，确保先到达的进程优先执行
        processList.sort(Comparator.comparingInt(PCB::getArrivalTime));

        for (PCB process : processList) {
            if (!"READY".equals(process.getState())) {
                continue; // 跳过非 READY 状态的进程
            }

            // 等待直到当前进程的到达时间
            while (process.getRemainingArrivalTime() > 0 && totalTimeSlices > 0) {
                statusLog.append("时间片: 等待进程 ").append(process.getId())
                        .append(" 到达, 剩余时间片: ").append(totalTimeSlices)
                        .append(", 剩余到达时间: ").append(process.getRemainingArrivalTime()).append("\n");
                process.setRemainingArrivalTime(process.getRemainingArrivalTime() - 1); // 减少剩余到达时间
                totalTimeSlices--; // 总时间片减少

                if (totalTimeSlices <= 0) {
                    statusLog.append("时间片已用完\n");
                    return; // 时间片用完后退出
                }
            }

            // 如果当前进程的到达时间已到，更新其状态为 READY
            if (process.getRemainingArrivalTime() == 0) {
                process.setState("READY");
                statusLog.append("进程 ").append(process.getId()).append(" 到达，状态变为 READY\n");
            }

            // 当前进程开始运行
            process.setState("RUNNING");
            statusLog.append("进程 ").append(process.getId()).append(" 开始运行\n");

            // 持续运行，直到该进程完成
            while (process.getRemainingTime() > 0) {
                process.runForTime(1); // 每次运行一个时间单位
                totalTimeSlices--;    // 总时间片减少

                statusLog.append("时间片: 进程 ").append(process.getId())
                        .append(" 运行中, 剩余时间: ").append(process.getRemainingTime()).append("\n");

                if (totalTimeSlices <= 0) {
                    statusLog.append("时间片已用完\n");
                    return; // 时间片用完后退出
                }
            }

            // 当前进程运行完成
            process.setState("TERMINATED");
            statusLog.append("进程 ").append(process.getId()).append(" 运行完成，状态变为 TERMINATED\n");
        }
    }

    //RR
    private void simulateRR(int baseTimeSlice, StringBuilder statusLog, int totalTimeSlices) {
        for (int slice = 0; slice < totalTimeSlices; slice++) {

            boolean progressMade = false;  // 标志是否有进程执行

            for (PCB process : processList) {
                if (!"TERMINATED".equals(process.getState())) {  // 跳过已终止的进程
                    process.setState("RUNNING");

                    // 等待直到当前进程的到达时间
                    while (process.getRemainingArrivalTime() > 0 && totalTimeSlices > 0) {
                        statusLog.append("时间片: 等待进程 ").append(process.getId())
                                .append(" 到达, 剩余时间片: ").append(totalTimeSlices)
                                .append(", 剩余到达时间: ").append(process.getRemainingArrivalTime()).append("\n");
                        process.setRemainingArrivalTime(process.getRemainingArrivalTime() - 1); // 减少剩余到达时间
                        totalTimeSlices--; // 总时间片减少

                        if (totalTimeSlices <= 0) {
                            statusLog.append("时间片已用完\n");
                            return; // 时间片用完后退出
                        }
                    }

                    // 如果当前进程的到达时间已到，更新其状态为 READY
                    if (process.getRemainingArrivalTime() == 0) {
                        process.setState("READY");
                        statusLog.append("进程 ").append(process.getId()).append(" 到达，状态变为 READY\n");
                    }

                    // 根据优先级调整时间片大小 (优先级越高，时间片越大)
                    int adjustedTimeSlice = baseTimeSlice + (5 - process.getPriority()); // 假设优先级范围是 1~5
                    int actualTimeSlice = Math.min(adjustedTimeSlice, process.getRemainingTime());

                    // 执行调整后的时间片
                    statusLog.append("时间片开始: 进程 ")
                            .append(process.getId())
                            .append(" 运行 (优先级: ")
                            .append(process.getPriority())
                            .append(", 时间片: ")
                            .append(actualTimeSlice)
                            .append("), 当前状态: ")
                            .append(process.getState())
                            .append("\n");

                    // 执行进程的时间片
                    process.runForTime(actualTimeSlice);
                    progressMade = true;  // 标记有进程执行

                    // 检查进程状态
                    if (process.getRemainingTime() <= 0) {
                        process.setRemainingTime(0); // 避免负数
                        process.setState("TERMINATED");
                        statusLog.append("进程 ").append(process.getId())
                                .append(" 完成, 当前状态: ").append(process.getState()).append("\n");
                    } else {
                        process.setState("READY"); // 进程未完成，返回 READY 状态
                    }

                    // 打印时间片结束后的状态
                    statusLog.append("时间片结束: 进程 ")
                            .append(process.getId())
                            .append(" 当前状态: ")
                            .append(process.getState())
                            .append("\n");
                }
            }

            // 如果没有进程执行，表示所有进程都已完成，跳出循环
            if (!progressMade) {
                break;
            }
        }
    }

    //SJF
    private void simulateSJF(int timeSlice, StringBuilder statusLog, int totalTimeSlices) {
        for (int slice = 0; slice < totalTimeSlices; slice++) {
            // 每次时间片开始前重新排序，确保优先调度剩余时间最短的进程
            processList.sort((p1, p2) -> {
                if (p1.getRemainingTime() == p2.getRemainingTime()) {
                    return Integer.compare(p1.getPriority(), p2.getPriority());
                }
                return Integer.compare(p1.getRemainingTime(), p2.getRemainingTime());
            });

            boolean allTerminated = true;  // 假设所有进程都已完成

            for (PCB process : processList) {
                if ("READY".equals(process.getState()) || "RUNNING".equals(process.getState())) {
                    allTerminated = false;  // 如果有进程还在 READY 或 RUNNING 状态，则说明未完成

                    // 如果当前进程的到达时间已到，更新其状态为 READY
                    while (process.getRemainingArrivalTime() > 0 && totalTimeSlices > 0) {
                        statusLog.append("时间片: 等待进程 ").append(process.getId())
                                .append(" 到达, 剩余时间片: ").append(totalTimeSlices)
                                .append(", 剩余到达时间: ").append(process.getRemainingArrivalTime()).append("\n");
                        process.setRemainingArrivalTime(process.getRemainingArrivalTime() - 1); // 减少剩余到达时间
                        totalTimeSlices--; // 总时间片减少

                        if (totalTimeSlices <= 0) {
                            statusLog.append("时间片已用完\n");
                            return; // 时间片用完后退出
                        }
                    }

                    // 如果进程已到达，更新为 READY 状态并开始执行
                    if (process.getRemainingArrivalTime() == 0) {
                        process.setState("READY");
                        statusLog.append("进程 ").append(process.getId()).append(" 到达，状态变为 READY\n");
                    }

                    // 执行时间片
                    statusLog.append("时间片开始: 进程 ")
                            .append(process.getId())
                            .append(" 运行 (优先级: ")
                            .append(process.getPriority())
                            .append(", 剩余时间: ")
                            .append(process.getRemainingTime())
                            .append("), 当前状态: ")
                            .append(process.getState())
                            .append("\n");

                    // 执行当前进程的一个时间片，最多执行到剩余时间
                    process.runForTime(Math.min(timeSlice, process.getRemainingTime()));

                    // 进程是否完成
                    if (process.getRemainingTime() <= 0) {
                        process.setRemainingTime(0); // 避免负数
                        process.setState("TERMINATED");
                        statusLog.append("进程 ").append(process.getId())
                                .append(" 完成, 当前状态: ").append(process.getState()).append("\n");
                    } else {
                        process.setState("READY"); // 未完成，返回 READY
                    }

                    // 打印时间片结束后的状态
                    statusLog.append("时间片结束: 进程 ")
                            .append(process.getId())
                            .append(" 当前状态: ")
                            .append(process.getState())
                            .append(", 剩余时间: ")
                            .append(process.getRemainingTime())
                            .append("\n");
                }
            }

            // 如果所有进程都已完成，跳出循环
            if (allTerminated) {
                statusLog.append("所有进程已完成，模拟结束。\n");
                break;  // 跳出时间片循环
            }
        }
    }

    // 优先级调度算法
    public void simulatePriorityScheduling(StringBuilder statusLog, int totalTimeSlices) {
        // 按优先级排序，优先级值越小，优先级越高
        processList.sort(Comparator.comparingInt(PCB::getPriority));

        for (int slice = 0; slice < totalTimeSlices; slice++) {

            for (PCB process : processList) {
                if ("READY".equals(process.getState()) || "RUNNING".equals(process.getState())) {
                    process.setState("RUNNING");

                    // 等待直到当前进程的到达时间
                    while (process.getRemainingArrivalTime() > 0 && totalTimeSlices > 0) {
                        statusLog.append("时间片: 等待进程 ").append(process.getId())
                                .append(" 到达, 剩余时间片: ").append(totalTimeSlices)
                                .append(", 剩余到达时间: ").append(process.getRemainingArrivalTime()).append("\n");
                        process.setRemainingArrivalTime(process.getRemainingArrivalTime() - 1); // 减少剩余到达时间
                        totalTimeSlices--; // 总时间片减少

                        if (totalTimeSlices <= 0) {
                            statusLog.append("时间片已用完\n");
                            return; // 时间片用完后退出
                        }
                    }

                    // 如果当前进程的到达时间已到，更新其状态为 READY
                    if (process.getRemainingArrivalTime() == 0) {
                        process.setState("READY");
                        statusLog.append("进程 ").append(process.getId()).append(" 到达，状态变为 READY\n");
                    }

                    // 运行一个时间片
                    statusLog.append("时间片开始: 进程 ").append(process.getId())
                            .append("（优先级: ").append(process.getPriority()).append("）运行, 当前状态: ")
                            .append(process.getState()).append("\n");

                    process.runForTime(1); // 每次执行一个单位时间

                    // 打印时间片结束状态
                    statusLog.append("时间片结束: 进程 ").append(process.getId())
                            .append(" 当前状态: ").append(process.getState())
                            .append(" 剩余时间: ").append(process.getRemainingTime()).append("\n");

                    // 检查是否完成
                    if (process.getRemainingTime() == 0) {
                        process.setState("TERMINATED");
                        statusLog.append("进程 ").append(process.getId())
                                .append(" 完成, 当前状态: ").append(process.getState()).append("\n");
                    } else {
                        process.setState("READY");
                    }
                }
            }
        }
    }

    public List<PCB> getProcessList() {
        return processList;
    }

    public void clearAllProcesses() {
        processList.clear();
    }

}
