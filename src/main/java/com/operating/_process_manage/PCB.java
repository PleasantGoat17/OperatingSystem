package com.operating._process_manage;

public class PCB {
    // 常量定义状态
    public static final String NEW = "NEW";               // 刚创建的进程
    public static final String READY = "READY";           // 就绪状态
    public static final String RUNNING = "RUNNING";       // 运行状态
    public static final String BLOCKED = "BLOCKED";       // 阻塞状态
    public static final String TERMINATED = "TERMINATED"; // 完成状态

    private final String id;               // 进程 ID
    private final int burstTime;           // 总运行时间
    private final int priority;            // 优先级
    private final int arrivalTime;         // 到达时间 (用于调度算法)
    private int remainingTime;       // 剩余时间
    private String state;            // 当前状态
    private int remainingArrivalTime; // 剩余到达时间

    // 构造函数
    public PCB(String id, int burstTime, int priority, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime; // 初始化剩余时间等于总运行时间
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.remainingArrivalTime = arrivalTime; // 初始化剩余到达时间
        this.state = NEW; // 初始状态为 NEW
    }

    // Getter 和 Setter 方法
    public String getId() {
        return id;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRemainingArrivalTime() {
        return remainingArrivalTime;
    }

    public void setRemainingArrivalTime(int remainingArrivalTime) {
        this.remainingArrivalTime = remainingArrivalTime;
    }

    // 减少剩余时间
    public void runForTime(int timeSlice) {
        if (remainingTime > 0) {
            remainingTime -= timeSlice;
            if (remainingTime <= 0) {
                remainingTime = 0; // 防止负值
                state = TERMINATED; // 如果剩余时间为 0，设置状态为完成
            } else {
                state = RUNNING; // 正常运行
            }
        }
    }

    // 减少剩余到达时间
    public void decreaseRemainingArrivalTime(int timeSlice) {
        if (remainingArrivalTime > 0) {
            remainingArrivalTime -= timeSlice;
            if (remainingArrivalTime < 0) {
                remainingArrivalTime = 0;
            }
        }
    }

    // 重置进程状态为 READY
    public void resetToReady() {
        if (state.equals(NEW)) {
            state = READY;
        }
    }

    @Override
    public String toString() {
        return "进程ID: " + id +
                ", 总时间: " + burstTime +
                ", 剩余时间: " + remainingTime +
                ", 状态: " + state +
                ", 优先级: " + priority +
                ", 到达时间: " + arrivalTime +
                ", 剩余到达时间: " + remainingArrivalTime;
    }
}
